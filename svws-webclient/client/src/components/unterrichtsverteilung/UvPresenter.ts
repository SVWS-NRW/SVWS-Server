import type { Schuljahresabschnitt } from '@core/asd/data/schule/Schuljahresabschnitt';
import { LehrerAnrechnungsgrund } from '@core/asd/types/lehrer/LehrerAnrechnungsgrund';
import { LehrerMehrleistungsarten } from '@core/asd/types/lehrer/LehrerMehrleistungsarten';
import { LehrerMinderleistungsarten } from '@core/asd/types/lehrer/LehrerMinderleistungsarten';
import { AllgemeinbildendOrganisationsformen } from '@core/asd/types/schule/AllgemeinbildendOrganisationsformen';
import { Schulgliederung } from '@core/asd/types/schule/Schulgliederung';
import type { FachDaten } from '@core/core/data/fach/FachDaten';
import type { UvBlockungRegel } from '@core/core/data/uv/regel/UvBlockungRegel';
import { UvBlockungRegelPrioritaet } from '@core/core/data/uv/regel/UvBlockungRegelPrioritaet';
import type { UvFach } from '@core/core/data/uv/UvFach';
import type { UvKurs } from '@core/core/data/uv/UvKurs';
import type { UvKursCreateRequest } from '@core/core/data/uv/UvKursCreateRequest';
import type { UvLehrer } from '@core/core/data/uv/UvLehrer';
import type { UvLerngruppe } from '@core/core/data/uv/UvLerngruppe';
import type { UvLerngruppenSchiene } from '@core/core/data/uv/UvLerngruppenSchiene';
import type { UvPlanungsabschnitt } from '@core/core/data/uv/UvPlanungsabschnitt';
import type { UvSchiene } from '@core/core/data/uv/UvSchiene';
import type { UvSchuelergruppe } from '@core/core/data/uv/UvSchuelergruppe';
import { Wochentag } from '@core/core/types/Wochentag';
import type { UvManager } from '@core/core/utils/uv/UvManager';

import { istUvPlanungsAuswahlArt, type UvBereichArt, type UvPlanungsAuswahlArt } from './planung/UvPlanung';
import { uvRegelDefinitionen, type UvRegelFeldArt } from './planung/UvRegelFelder';

export interface UvFachAnzeige {
	id: number;
	kuerzel: string;
	kuerzelStatistik: string;
}

const uvObjektBezeichnungen: Record<UvPlanungsAuswahlArt, string> = {
	lerngruppen: 'Lerngruppe',
	klassen: 'Klasse',
	schueler: 'Schüler',
	schienen: 'Schiene',
	lehrer: 'Lehrkraft',
	regeln: 'Regel',
};

/** Einheitliche Auswahltexte aus den fachlich definierten Regelprioritäten. */
export const uvRegelPrioritaeten = UvBlockungRegelPrioritaet.values().map(prioritaet => {
	const text = prioritaet.bezeichnung ?? 'Unbekannt';
	return { id: prioritaet.nr, text: text.charAt(0).toLocaleUpperCase('de') + text.slice(1) };
});

export function uvObjektTitel(art: UvBereichArt): string {
	return istUvPlanungsAuswahlArt(art) ? uvObjektBezeichnungen[art] : 'Details';
}

export function formatUvRegelPrioritaet(prioritaet: number): string {
	return uvRegelPrioritaeten.find(item => item.id === prioritaet)?.text ?? 'Ungültige Priorität';
}

/** Berechnet den Füllstand der Lehrkraftkarte für die Anzeige. */
export function lehrerAuslastung(soll: number, rest: number): { wert: number; maximum: number; prozent: number } {
	const wert = Math.max(0, soll - rest);
	const prozentOhneSoll = wert > 0 ? 100 : 0;
	return { wert, maximum: Math.max(soll, wert), prozent: soll > 0 ? Math.min(100, wert / soll * 100) : prozentOhneSoll };
}

function kuerzelOderBezeichnung(kuerzel: string | null, bezeichnung: string | null, ersatz: string): string {
	const text = kuerzel?.trim() ?? '';
	if (text.length > 0) {
		return text;
	}
	const langtext = bezeichnung?.trim() ?? '';
	return langtext.length > 0 ? langtext : ersatz;
}

export function schuljahresabschnittText(abschnitt: Schuljahresabschnitt): string {
	return `${abschnitt.schuljahr}/${abschnitt.schuljahr + 1} – ${abschnitt.abschnitt}. Abschnitt`;
}

export const parallelitaetItems = ['', ...Array.from({ length: 26 }, (_, index) => String.fromCodePoint(65 + index))];

function klassenKatalogEintraege(eintraege: ({ kuerzel: string; text: string } | null)[], bestand: string | null) {
	const result: { kuerzel: string | null; text: string }[] = [{ kuerzel: null, text: 'Keine' }];
	for (const eintrag of eintraege) {
		if (eintrag !== null) {
			result.push({ kuerzel: eintrag.kuerzel, text: `${eintrag.kuerzel} – ${eintrag.text}` });
		}
	}
	if (bestand !== null && !result.some(option => option.kuerzel === bestand)) {
		result.push({ kuerzel: bestand, text: `${bestand} (in diesem Schuljahr nicht verfügbar)` });
	}
	return result;
}

export function klassenKatalogOptionen(schuljahr: number, organisationsform: string | null, schulgliederung: string | null) {
	return {
		organisationsformen: klassenKatalogEintraege(AllgemeinbildendOrganisationsformen.values().map(form => form.daten(schuljahr)), organisationsform),
		schulgliederungen: klassenKatalogEintraege(Schulgliederung.values().map(gliederung => gliederung.daten(schuljahr)), schulgliederung),
	};
}

function lerngruppeTyp(lerngruppe: UvLerngruppe): string {
	if (lerngruppe.idKurs !== null) {
		return 'Kurs';
	}
	if (lerngruppe.idKlasse !== null) {
		return 'Klasse';
	}
	return '—';
}

function anrechnungsgrundText(schuljahr: number, grund: string): string {
	const art = uvAnrechnungsart(grund);
	const kataloge = {
		Mehrleistung: LehrerMehrleistungsarten,
		Minderleistung: LehrerMinderleistungsarten,
		Anrechnung: LehrerAnrechnungsgrund,
	};
	const katalog = art === 'Mehrleistung' || art === 'Minderleistung' ? kataloge[art] : kataloge.Anrechnung;
	const katalogEintrag = katalog.data().getEintragBySchuljahrUndSchluessel(schuljahr, grund);
	return katalogEintrag === null ? grund : `${katalogEintrag.kuerzel} - ${katalogEintrag.text}`;
}

function lehrerSuchtext(kuerzel: string, name: string, verplant: number, anrechnung: number, soll: number | null): string {
	const sollText = soll === null ? '' : ` · ${formatUvStunden(soll)} h Soll`;
	return `${kuerzel} ${name} · ${formatUvStunden(verplant)} h Unterricht · ${formatUvStunden(anrechnung)} h Anrechnung${sollText}`.toLocaleLowerCase('de');
}

function lehrerName(lehrer: UvLehrer): string {
	return [lehrer.nachname?.trim() ?? '', lehrer.vorname?.trim() ?? ''].filter(text => text !== '').join(', ');
}

/** Formatiert Stundenwerte einheitlich für die UV-Oberfläche. */
export function formatUvStunden(value: number): string {
	return value.toLocaleString('de-DE', { maximumFractionDigits: 2 });
}

/** Benennt die Art einer Lehreranrechnung für die UV. */
export function uvAnrechnungsart(grund: string): 'Mehrleistung' | 'Minderleistung' | 'Anrechnung' {
	if (grund.startsWith('1')) {
		return 'Mehrleistung';
	}
	if (grund.startsWith('2')) {
		return 'Minderleistung';
	}
	return 'Anrechnung';
}

/**
 * Bindet Anzeigefunktionen an eine konkrete Manager-Instanz.
 * Komponenten erzeugen den Presenter über computed neu, wenn der State den Manager austauscht.
 */
export function createUvPresenter(manager: UvManager) {

	// Lehrkräfte.

	function lehrerKuerzel(idLehrer: number): string {
		const lehrer = manager.lehrerGetByIdOrNull(idLehrer);
		return kuerzelOderBezeichnung(lehrer?.kuerzel ?? null, lehrer === null ? null : lehrerName(lehrer), `Lehrkraft (ID: ${idLehrer})`);
	}

	function lehrerAnzeigeText(idLehrer: number): string {
		const lehrer = manager.lehrerGetByIdOrNull(idLehrer);
		if (lehrer === null) {
			return lehrerKuerzel(idLehrer);
		}
		const kuerzel = lehrer.kuerzel.trim();
		const name = lehrerName(lehrer);
		return kuerzel !== '' && name !== '' ? `${kuerzel} · ${name}` : kuerzelOderBezeichnung(kuerzel, name, `Lehrkraft (ID: ${idLehrer})`);
	}

	function lehrerFaecher(idLehrer: number): UvFachAnzeige[] {
		const lehrer = manager.lehrerGetByIdOrNull(idLehrer);
		if (lehrer === null) {
			return [];
		}
		const faecherById = new Map<number, FachDaten>();
		for (const unterrichtsfach of manager.lehrerUnterrichtsfachGetMengeByLehrer(lehrer)) {
			const fach = manager.fachdatenGetByLehrerUnterrichtsfach(unterrichtsfach);
			faecherById.set(fach.id, fach);
		}
		return [...faecherById.values()]
			.sort((a, b) => {
				const sortierung = a.sortierung - b.sortierung;
				if (sortierung !== 0) {
					return sortierung;
				}
				const kuerzel = a.kuerzel.localeCompare(b.kuerzel, 'de', { numeric: true });
				return kuerzel !== 0 ? kuerzel : a.id - b.id;
			})
			.map(fach => ({
				id: fach.id,
				kuerzel: kuerzelOderBezeichnung(fach.kuerzel, fach.bezeichnung, `Fach (ID: ${fach.id})`),
				kuerzelStatistik: fach.kuerzelStatistik,
			}));
	}

	// Fächer, Klassen und Jahrgänge.

	function fachText(fach: UvFach): string {
		const daten = manager.fachdatenGetByFach(fach);
		const bezeichnung = daten.bezeichnung.trim();
		const kuerzel = daten.kuerzel.trim();
		return bezeichnung !== '' && kuerzel !== '' ? `${bezeichnung} (${kuerzel})`
			: kuerzelOderBezeichnung(kuerzel, bezeichnung, `Fach (ID: ${fach.id})`);
	}

	function fachKuerzel(idFach: number): string {
		const fach = manager.fachGetByIdOrException(idFach);
		const daten = manager.fachdatenGetByFach(fach);
		return kuerzelOderBezeichnung(daten.kuerzel, daten.bezeichnung, `Fach (ID: ${idFach})`);
	}

	function klasseKuerzel(idKlasse: number | null): string {
		if (idKlasse === null || idKlasse < 0) {
			return '—';
		}
		const klasse = manager.klasseGetByIdOrException(idKlasse);
		return kuerzelOderBezeichnung(klasse.kuerzel, klasse.bezeichnung, `Klasse (ID: ${idKlasse})`);
	}

	function jahrgangKuerzel(idJahrgang: number): string {
		const jahrgang = manager.jahrgangsdatenGetById(idJahrgang);
		return kuerzelOderBezeichnung(jahrgang.kuerzel, jahrgang.bezeichnung, `Jahrgang (ID: ${idJahrgang})`);
	}

	function schuelergruppeSchuelerjahrgaengeText(schuelergruppe: UvSchuelergruppe): string {
		return [...manager.jahrgangsdatenGetMengeBySchuelergruppe(schuelergruppe)]
			.map(jahrgang => jahrgangKuerzel(jahrgang.id)).join('/');
	}

	// Kurse, Lerngruppen und Schienen.

	function kursKurzbezeichnung(kurs: UvKurs | UvKursCreateRequest): string {
		return `${fachKuerzel(kurs.idFach)}-${kurs.kursart}${kurs.kursnummer}`;
	}

	function kursBezeichnung(kurs: UvKurs): string {
		const gruppe = manager.schuelergruppeGetByKurs(kurs);
		const jahrgaenge = schuelergruppeSchuelerjahrgaengeText(gruppe);
		return `${jahrgaenge.length === 0 ? '' : jahrgaenge + ' '}${kursKurzbezeichnung(kurs)}`;
	}

	function lerngruppeBezeichnung(lerngruppe: UvLerngruppe): string {
		if (lerngruppe.idKurs !== null) {
			return kursBezeichnung(manager.kursGetByIdOrException(lerngruppe.idKurs));
		}
		if (lerngruppe.idKlasse !== null) {
			const klasse = manager.klasseGetByIdOrException(lerngruppe.idKlasse);
			const bezeichnung = klasse.bezeichnung?.trim() ?? '';
			return bezeichnung.length > 0 ? bezeichnung : klasseKuerzel(klasse.id);
		}
		return '—';
	}

	function lerngruppeKurzbezeichnung(lerngruppe: UvLerngruppe): string {
		const kurs = lerngruppe.idKurs === null ? null : manager.kursGetByIdOrNull(lerngruppe.idKurs);
		return kurs === null ? lerngruppeBezeichnung(lerngruppe) : kursKurzbezeichnung(kurs);
	}

	function lerngruppeFach(lerngruppe: UvLerngruppe): string {
		const fach = manager.fachGetByLerngruppe(lerngruppe);
		return fach === null ? '—' : fachKuerzel(fach.id);
	}

	function lerngruppeFachId(lerngruppe: UvLerngruppe): number | null {
		return manager.fachGetByLerngruppe(lerngruppe)?.id ?? null;
	}

	function schienenLerngruppenZeilen(schiene: UvSchiene): { lerngruppe: UvLerngruppe; zuordnung: UvLerngruppenSchiene; typ: string; bezeichnung: string; wochenstunden: number }[] {
		return [...manager.lerngruppenSchieneGetMengeBySchiene(schiene)].map(zuordnung => {
			const lerngruppe = manager.lerngruppeGetByLerngruppenSchiene(zuordnung);
			return { lerngruppe, zuordnung, typ: lerngruppeTyp(lerngruppe), bezeichnung: lerngruppeBezeichnung(lerngruppe), wochenstunden: lerngruppe.wochenstunden };
		});
	}

	// Zeitraster.

	function zeitrasterAnsicht(idZeitraster: number): { maxStunde: number; maxWochentag: number; wochentage: Wochentag[]; stunden: number[]; naechsterWochentag: string } {
		const maxStunde = manager.zeitrasterEintragGetMaxStunde(idZeitraster);
		const maxWochentag = manager.zeitrasterEintragGetMaxWochentag(idZeitraster);
		const minWochentag = manager.zeitrasterEintragGetMinWochentag(idZeitraster);
		const wochentage: Wochentag[] = [];
		for (let tag = minWochentag; tag > 0 && tag <= maxWochentag; tag++) {
			wochentage.push(Wochentag.fromIDorException(tag));
		}
		return {
			maxStunde,
			maxWochentag,
			wochentage,
			stunden: Array.from({ length: maxStunde }, (_, index) => index + 1),
			naechsterWochentag: maxWochentag < 7 ? Wochentag.fromIDorException(maxWochentag + 1).kuerzel : '',
		};
	}

	// Regeln.

	function regelOptionen(art: UvRegelFeldArt, plan: UvPlanungsabschnitt): { id: number; text: string }[] {
		switch (art) {
			case 'lehrer': {
				return [...manager.lehrerGetMengeByPlanungsabschnitt(plan)].map(item => ({ id: item.id, text: lehrerAnzeigeText(item.id) }));
			}
			case 'klasse': {
				return [...manager.klasseGetMengeByPlanungsabschnitt(plan)].map(item => ({ id: item.id, text: klasseKuerzel(item.id) }));
			}
			case 'lerngruppe': {
				return [...manager.lerngruppeGetMengeByPlanungsabschnitt(plan)].map(item => ({
					id: item.id, text: item.idKlasse === null ? lerngruppeBezeichnung(item) : `${lerngruppeBezeichnung(item)} · ${lerngruppeFach(item)}`,
				}));
			}
			case 'fach': {
				return [...manager.fachGetMengeAsList()].map(item => ({ id: item.id, text: fachText(item) }));
			}
			case 'jahrgang': {
				return [...manager.jahrgangsdatenGetMenge()].map(item => ({ id: item.id, text: jahrgangKuerzel(item.id) }));
			}
			case 'aktivierung': {
				return [{ id: 0, text: 'Nein' }, { id: 1, text: 'Ja' }];
			}
			default: {
				return [];
			}
		}
	}

	function regelBeschreibung(regel: UvBlockungRegel, plan: UvPlanungsabschnitt): string {
		const definition = uvRegelDefinitionen.find(item => item.typ.nr === regel.typ);
		if (definition === undefined) {
			return `Unbekannter Regeltyp ${regel.typ}`;
		}
		let text = definition.typ.bezeichnung ?? `Regeltyp ${regel.typ}`;
		const parameter = [...regel.parameter];
		for (let i = 0; i < definition.felder.length; i++) {
			const art = definition.felder[i].art;
			const wert = parameter[i];
			const name = regelOptionen(art, plan).find(item => item.id === wert)?.text ?? String(wert);
			text = text.replace(`<${String.fromCodePoint(65 + i)}>`, name);
		}
		const listenArt = definition.liste;
		if (listenArt !== undefined) {
			const namen = parameter.slice(definition.felder.length).map(id => regelOptionen(listenArt, plan).find(item => item.id === id)?.text ?? String(id));
			text = text.replace('<...>', namen.join(', '));
		}
		return text;
	}

	return {
		lehrerKuerzel,
		lehrerAnzeigeText,
		lehrerSuchtext,
		lehrerFaecher,
		fachText,
		klasseKuerzel,
		jahrgangKuerzel,
		schuelergruppeSchuelerjahrgaengeText,
		kursBezeichnung,
		kursKurzbezeichnung,
		lerngruppeBezeichnung,
		lerngruppeKurzbezeichnung,
		lerngruppeFach,
		lerngruppeFachId,
		schienenLerngruppenZeilen,
		zeitrasterAnsicht,
		schuljahresabschnittText,
		anrechnungsgrundText,
		regelOptionen,
		regelBeschreibung,
	};
}
