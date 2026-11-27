import { UvBlockungRegel } from '@core/core/data/uv/regel/UvBlockungRegel';
import type { UvKurs } from '@core/core/data/uv/UvKurs';
import type { UvKursCreateRequest } from '@core/core/data/uv/UvKursCreateRequest';
import type { UvKursImportDaten } from '@core/core/data/uv/UvKursImportDaten';
import type { UvLehrer } from '@core/core/data/uv/UvLehrer';
import type { UvLerngruppe } from '@core/core/data/uv/UvLerngruppe';
import type { UvLerngruppenLehrer } from '@core/core/data/uv/UvLerngruppenLehrer';
import type { UvPlanungsabschnitt } from '@core/core/data/uv/UvPlanungsabschnitt';
import type { UvPlanungsabschnittSchueler } from '@core/core/data/uv/UvPlanungsabschnittSchueler';
import type { UvSchuelergruppe } from '@core/core/data/uv/UvSchuelergruppe';
import { UvManager } from '@core/core/utils/uv/UvManager';
import { UvRegelManager } from '@core/core/utils/uv/UvRegelManager';
import { ArrayList } from '@core/java/util/ArrayList';

import { createUvPresenter, formatUvStunden, type UvFachAnzeige } from '../UvPresenter';

import { type UvAuswahlStatus, type UvBereichArt, type UvPlanungsAuswahl, type UvPlanungsAuswahlArt, type UvZuordnungBearbeitung } from './UvPlanung';
import { matrixDatenAufbereiten, type UvMatrixAnsicht, type UvMatrixEintrag, type UvMatrixFilter } from './UvPlanungsMatrixDaten';
import { getUvRegelReferenzen, uvRegelDefinitionen, type UvRegelFeldArt } from './UvRegelFelder';

/** Eingaben für die Prüfung einer neuen Klassen- oder Kurslerngruppe. */
export interface UvLerngruppeEntwurfPruefung {
	unterrichtsart: 'klasse' | 'kurs';
	kurs: Pick<UvKurs, 'idFach' | 'kursart' | 'kursnummer'>;
	schuljahr: number;
	hatKlasse: boolean;
	hatSchuljahresabschnitt: boolean;
	wochenstunden: number;
	wochenstundenUnterrichtet: number;
}

export interface UvPlanungsAuswahlEntscheidung {
	auswahl: UvPlanungsAuswahl | null;
	navigation: 'detail' | 'uebersicht' | null;
}
export interface UvPlanungsKandidat {
	id: number;
	text: string;
	zusatz: string;
}
export interface UvZuordnungsAuswahl {
	eintraege: UvPlanungsKandidat[];
	lehrer: UvLehrerAnsichtEintrag[];
	schueler: UvPlanungsabschnittSchueler[];
	zugeordnet: number;
	verfuegbareIds: Set<number>;
}
export interface UvSchuelerEntwurfAnsicht {
	mitglieder: UvPlanungsabschnittSchueler[];
	kandidaten: UvPlanungsabschnittSchueler[];
}
export interface UvKlassenbrettKarte {
	id: number;
	kuerzel: string;
	schueler: number;
	stunden: number;
}
export interface UvKlassenbrettStufe {
	id: number;
	name: string;
	sortierung: number;
	klassen: UvKlassenbrettKarte[];
	schueler: number;
	stunden: number;
}
export interface UvLerngruppenFilter {
	suche: string;
	auswahl: UvPlanungsAuswahl | null;
	idLehrer: number | null;
	nurLehrerEinsaetze: boolean;
	jahrgang: string | null;
	fach: string | null;
	klasseKurs: string | null;
	status: string | null;
}
export interface UvLehrerFilter {
	idLehrer: number | null;
	suche: string;
	auswahl: UvPlanungsAuswahl | null;
	idLerngruppe: number | null;
	nurGeeignete: boolean;
}
export interface UvLehrerAnsichtEintrag {
	id: number;
	text: string;
	name: string;
	verplant: number;
	anrechnung: number;
	soll: number | null;
	reststunden: number | null;
	faecher: UvFachAnzeige[];
	zugeordnet: boolean;
	zuweisbar: boolean;
}
export interface UvLehrerAnsicht {
	eintraege: UvLehrerAnsichtEintrag[];
	gesamtAnzahl: number;
	zugeordneteLehrer: string[];
}
export interface UvLerngruppenGruppe {
	jahrgang: string;
	lerngruppen: UvLerngruppe[];
	stunden: number;
	offen: number;
}
export interface UvLerngruppenAnsicht {
	gruppen: UvLerngruppenGruppe[];
	jahrgaenge: string[];
	faecher: string[];
	klassenKurse: { id: string; text: string }[];
	anzahl: number;
	besetzteAnzahl: number;
	gesamtStunden: number;
	besetzteStunden: number;
}
export interface UvPlanungsStatistik {
	lerngruppen: number;
	besetzteLerngruppen: number;
	geplanteStunden: number;
	offeneStunden: number;
}

type ReferenzArt = 'lerngruppe' | 'klasse' | 'lehrer' | 'schueler' | 'schiene' | 'fach' | 'jahrgang';
type Referenzen = Record<ReferenzArt, Set<number>>;

/**
 * Bereitet die Ansichten eines Planungsabschnitts auf und steuert ihre Auswahlbeziehungen.
 * Allgemeine Zulässigkeitsprüfungen und Einsatzwerte liefert der UvManager,
 * Regelprüfungen und Fixierungsentwürfe der UvRegelManager, Anzeigetexte der Presenter.
 */
export class UvPlanungsManager {

	// Eine Instanz gehört zu einem bestätigten State-Stand. Nach Änderungen wird sie neu erzeugt.
	// Nur Auswahlbeziehungen und die Regelprüfung werden innerhalb dieser Instanz zwischengespeichert.
	private readonly referenzenByAuswahl = new Map<string, Referenzen>();
	private readonly presenter: ReturnType<typeof createUvPresenter>;
	private regelPruefung: UvRegelManager | null = null;

	constructor(private readonly manager: UvManager, private readonly plan: UvPlanungsabschnitt, private readonly regeln: UvBlockungRegel[]) {
		this.presenter = createUvPresenter(manager);
	}

	// Formularprüfung und Importvorschau.

	/** Bereitet Kursbezeichnungen mit den tatsächlich zugeordneten Jahrgängen des Importentwurfs auf. */
	public static kursImportVorschau(manager: UvManager, daten: UvKursImportDaten): { kurs: UvKursCreateRequest; bezeichnung: string }[] {
		const jahrgaengeByGruppe = new Map<number, Set<number>>();
		for (const zuordnung of daten.schuelergruppenschueler) {
			const schueler = manager.planungsabschnittSchuelerGetByIdOrNull(zuordnung.idPlanungsabschnitt, zuordnung.idSchueler);
			if (schueler === null) {
				continue;
			}
			const ids = jahrgaengeByGruppe.get(zuordnung.idSchuelergruppe) ?? new Set<number>();
			ids.add(schueler.idJahrgang);
			jahrgaengeByGruppe.set(zuordnung.idSchuelergruppe, ids);
		}
		const presenter = createUvPresenter(manager);
		return [...daten.kurse].map(kurs => {
			const jahrgaenge = [...(jahrgaengeByGruppe.get(kurs.idSchuelergruppe) ?? [])]
				.map(id => manager.jahrgangsdatenGetById(id))
				.sort((a, b) => a.sortierung === b.sortierung ? a.id - b.id : a.sortierung - b.sortierung)
				.map(jahrgang => presenter.jahrgangKuerzel(jahrgang.id)).join('/');
			return { kurs, bezeichnung: `${jahrgaenge.length === 0 ? '' : jahrgaenge + ' '}${presenter.kursKurzbezeichnung(kurs)}` };
		});
	}

	/** Prüft die Kursnummer für Formular und Speicherprüfung. */
	public static kursnummerGueltig(value: number | null): boolean {
		return value !== null && UvManager.kursnummerIstGueltig(value);
	}

	/** Prüft die gemeinsamen Eingaben beim Anlegen und Bearbeiten eines UV-Kurses. */
	public static kursFehler(manager: UvManager, kurs: Pick<UvKurs, 'idFach' | 'kursart' | 'kursnummer'>, schuljahr: number): string[] {
		const fehler: string[] = [];
		if (manager.fachGetByIdOrNull(kurs.idFach) === null) {
			fehler.push('Bitte ein Fach auswählen.');
		}
		if (!UvManager.kursartIstGueltig(kurs.kursart, schuljahr)) {
			fehler.push('Bitte eine für das Schuljahr gültige Kursart auswählen.');
		}
		if (!UvPlanungsManager.kursnummerGueltig(kurs.kursnummer)) {
			fehler.push('Die Kursnummer muss eine ganze Zahl ab 1 sein.');
		}
		return fehler;
	}

	/** Prüft die Eingaben beim Anlegen eines Klassen- oder Kursunterrichts. */
	public static lerngruppeNeuFehler(manager: UvManager, entwurf: UvLerngruppeEntwurfPruefung): string[] {
		const { unterrichtsart, kurs, schuljahr, hatKlasse, hatSchuljahresabschnitt, wochenstunden, wochenstundenUnterrichtet } = entwurf;
		const fehler = unterrichtsart === 'kurs' ? UvPlanungsManager.kursFehler(manager, kurs, schuljahr) : [];
		if (unterrichtsart === 'klasse' && manager.fachGetByIdOrNull(kurs.idFach) === null) {
			fehler.push('Bitte ein Fach auswählen.');
		}
		if (unterrichtsart === 'klasse' && !hatKlasse) {
			fehler.push('Bitte eine Klasse auswählen.');
		}
		if (unterrichtsart === 'kurs' && !hatSchuljahresabschnitt) {
			fehler.push('Bitte einen Schuljahresabschnitt auswählen.');
		}
		if (!UvPlanungsManager.lerngruppeStundenGueltig(wochenstunden, wochenstundenUnterrichtet)) {
			fehler.push('Bitte gültige Wochenstunden eintragen.');
		}
		return fehler;
	}

	/** Ermittelt die nächste Reihenfolge unabhängig von Lücken in vorhandenen Zuordnungen. */
	public static naechsteReihenfolge(zuordnungen: Iterable<{ reihenfolge: number }>): number {
		let maximum = 0;
		for (const zuordnung of zuordnungen) {
			maximum = Math.max(maximum, zuordnung.reihenfolge);
		}
		return maximum + 1;
	}

	/** Prüft die Stundenwerte einer Lerngruppe vor dem Speichern. */
	public static lerngruppeStundenGueltig(wochenstunden: number, wochenstundenUnterrichtet: number): boolean {
		return UvManager.lerngruppeStundenGueltig(wochenstunden, wochenstundenUnterrichtet);
	}

	/** Prüft eine Änderung an den Einsatzwerten einer Lehrkraft. */
	public static lehrerZuordnungswerteGueltig(reihenfolge: number, wochenstunden: number, maxStunden: number, angerechnet: number): boolean {
		return UvManager.lehrerZuordnungswerteGueltig(reihenfolge, wochenstunden, maxStunden, angerechnet);
	}

	// Zuordnungen und Einsatzwerte.

	/** Liefert Kürzel und Einsatzdaten einer Lerngruppe. */
	public lerngruppeLehrerText(idLerngruppe: number): string {
		return this.lerngruppeLehrerKuerzel(idLerngruppe).join(', ');
	}

	public lerngruppeLehrerKuerzel(idLerngruppe: number): string[] {
		return this.lerngruppeLehrerZuordnungen(idLerngruppe).map(item => this.presenter.lehrerKuerzel(item.idLehrer));
	}

	public lerngruppeKursart(lerngruppe: UvLerngruppe): string | null {
		return lerngruppe.idKurs === null ? null : this.manager.kursGetByIdOrNull(lerngruppe.idKurs)?.kursart ?? null;
	}

	public lehrerIstZugeordnet(idLerngruppe: number, idLehrer: number): boolean {
		const gruppe = this.manager.lerngruppeGetByIdOrNull(idLerngruppe);
		return gruppe !== null && gruppe.idPlanungsabschnitt === this.plan.id && this.manager.lerngruppeLehrerIstZugeordnet(gruppe, idLehrer);
	}

	public lehrerIstGeeignet(idLerngruppe: number, idLehrer: number): boolean {
		const gruppe = this.manager.lerngruppeGetByIdOrNull(idLerngruppe);
		const lehrer = this.manager.lehrerGetByIdOrNull(idLehrer);
		return gruppe !== null && gruppe.idPlanungsabschnitt === this.plan.id && lehrer !== null
			&& this.manager.lehrerIsInPlanungsabschnitt(lehrer, this.plan) && this.manager.lehrerHatLehrbefaehigungLerngruppe(lehrer, gruppe);
	}

	public lerngruppeIstOffen(idLerngruppe: number): boolean {
		return this.lerngruppeZuordnungsAnzahl(idLerngruppe) === 0;
	}

	public besetzteLerngruppenAnzahl(): number {
		return [...this.manager.lerngruppeGetMengeByPlanungsabschnitt(this.plan)].filter(item => !this.lerngruppeIstOffen(item.id)).length;
	}

	public lehrerVerplant(idLehrer: number): number {
		return this.manager.lehrerUnterrichtsstundenGetByPlanungsabschnitt(this.plan, idLehrer);
	}

	public lehrerStunden(idLehrer: number): { verplant: number; deputat: number; soll: number | null; anrechnung: number } {
		const lehrer = this.manager.lehrerGetByIdOrNull(idLehrer);
		return {
			verplant: this.lehrerVerplant(idLehrer),
			deputat: this.manager.lehrerAngerechneteStundenGetByPlanungsabschnitt(this.plan, idLehrer),
			soll: lehrer === null ? null : this.manager.lehrerPflichtstundensollGetDoubleByLehrerAndPlanungsabschnitt(lehrer, this.plan),
			anrechnung: lehrer === null ? 0 : this.manager.lehrerAnrechnungsstundenGetDoubleByLehrerAndPlanungsabschnitt(lehrer, this.plan),
		};
	}

	/** Liefert die Anzahl der Einsätze und deren Unterrichtsstunden für die Lehrkraftanzeige. */
	public lehrerEinsatzZusammenfassung(idLehrer: number): { anzahl: number; stunden: number } {
		return {
			anzahl: this.manager.lerngruppenLehrerGetMengeByPlanungsabschnittAndLehrer(this.plan, idLehrer).size(),
			stunden: this.lehrerVerplant(idLehrer),
		};
	}

	public lerngruppeZuordnungsStunden(idLerngruppe: number): number {
		const gruppe = this.manager.lerngruppeGetByIdOrNull(idLerngruppe);
		return gruppe?.idPlanungsabschnitt !== this.plan.id ? 0 : this.manager.lerngruppeLehrerstundenGet(gruppe);
	}

	public lerngruppeZuordnungsAnzahl(idLerngruppe: number): number {
		const gruppe = this.manager.lerngruppeGetByIdOrNull(idLerngruppe);
		return gruppe?.idPlanungsabschnitt !== this.plan.id ? 0 : this.manager.lerngruppenLehrerGetMengeByLerngruppe(gruppe).size();
	}

	public lerngruppeLehrerZuordnungen(idLerngruppe: number): UvLerngruppenLehrer[] {
		const gruppe = this.manager.lerngruppeGetByIdOrNull(idLerngruppe);
		if (gruppe?.idPlanungsabschnitt !== this.plan.id) {
			return [];
		}
		return [...this.manager.lerngruppenLehrerGetMengeByLerngruppe(gruppe)]
			.sort((a, b) => a.reihenfolge === b.reihenfolge ? a.id - b.id : a.reihenfolge - b.reihenfolge);
	}

	public lehrerAnzeigeText(idLehrer: number): string {
		return this.presenter.lehrerAnzeigeText(idLehrer);
	}

	public lerngruppenByLehrer(idLehrer: number): UvLerngruppe[] {
		const ids = this.lehrerEinsatzIds(idLehrer);
		return [...this.manager.lerngruppeGetMengeByPlanungsabschnitt(this.plan)].filter(item => ids.has(item.id));
	}

	public neueLehrerZuordnungGueltig(lerngruppe: UvLerngruppe, idLehrer: number, wochenstunden: number, angerechnet: number): boolean {
		return this.manager.lerngruppenLehrerNeuIstGueltig(this.plan, lerngruppe, idLehrer, wochenstunden, angerechnet);
	}

	public lehrerEinsatzIds(idLehrer: number): Set<number> {
		return new Set([...this.manager.lerngruppenLehrerGetMengeByPlanungsabschnittAndLehrer(this.plan, idLehrer)].map(item => item.idLerngruppe));
	}

	public lerngruppeJahrgang(lerngruppe: UvLerngruppe, fallbackKlasse = 'Weitere', fallbackKurs = fallbackKlasse): string {
		const fallback = lerngruppe.idKlasse === null ? fallbackKurs : fallbackKlasse;
		const gruppe = this.manager.schuelergruppeGetByLerngruppe(lerngruppe);
		if (gruppe === null) {
			return fallback;
		}
		const jahrgang = this.presenter.schuelergruppeSchuelerjahrgaengeText(gruppe);
		return jahrgang === '' ? fallback : jahrgang;
	}

	/** Gibt die für eine bestehende Zuordnung zulässigen Lehrkräfte zurück. */
	public lehrerFuerZuordnung(lerngruppe: UvLerngruppe, idZuordnung: number): UvLehrer[] {
		return [...this.manager.lehrerGetMengeByPlanungsabschnitt(this.plan)]
			.filter(item => this.manager.lerngruppenLehrerWechselIstGueltig(this.plan, lerngruppe, idZuordnung, item.id))
			.sort((a, b) => a.kuerzel.localeCompare(b.kuerzel, 'de'));
	}

	public lehrerZuordnungGueltig(
		lerngruppe: UvLerngruppe,
		idLehrer: number,
		idZuordnung: number,
		reihenfolge: number,
		wochenstunden: number,
		angerechnet: number
	): boolean {
		return this.manager.lerngruppenLehrerAenderungIstGueltig(this.plan, lerngruppe, idZuordnung, idLehrer, reihenfolge, wochenstunden, angerechnet);
	}

	/** Prüft Stundenänderungen einer Lerngruppe gegen ihre vorhandenen Lehrkrafteinsätze. */
	public lerngruppeStundenAenderungGueltig(lerngruppe: UvLerngruppe): boolean {
		return this.manager.lerngruppeStundenAenderungIstGueltig(this.plan, lerngruppe);
	}

	public lehrerRestNachZuordnung(idLehrer: number, idZuordnung: number, angerechnet: number): number | null {
		return this.manager.lehrerReststundenGetNachZuordnung(this.plan, idLehrer, idZuordnung, angerechnet);
	}

	public lerngruppeSchuelerzahl(lerngruppe: UvLerngruppe): number {
		const gruppe = this.manager.schuelergruppeGetByLerngruppe(lerngruppe);
		return gruppe === null ? 0 : this.manager.planungsabschnittSchuelerGetMengeBySchuelergruppe(gruppe).size();
	}

	public lehrerFaecher(idLehrer: number): UvFachAnzeige[] {
		return this.presenter.lehrerFaecher(idLehrer);
	}

	// Aufbereitung der Planungsansichten.

	/** Liefert die vollständige, gefilterte Ansicht der Planungsmatrix. */
	public matrixAnsicht(filter: UvMatrixFilter, auswahl: UvPlanungsAuswahl | null): UvMatrixAnsicht {
		const bereichLerngruppen = this.matrixBereichLerngruppen(filter.bereich);
		const ausgewaehlterLehrer = auswahl?.art === 'lehrer' ? this.manager.lehrerGetByIdOrNull(auswahl.id) : null;
		const eintraege: UvMatrixEintrag[] = bereichLerngruppen.map(lerngruppe => {
			const prioritaet = this.auswahlPrioritaet(auswahl, 'lerngruppen', lerngruppe.id);
			const hatAuswahlbezug = auswahl !== null && prioritaet === 0;
			const geeignet = !hatAuswahlbezug && ausgewaehlterLehrer !== null
				&& this.manager.lehrerHatLehrbefaehigungLerngruppe(ausgewaehlterLehrer, lerngruppe);
			const prioritaetOhneBezug = geeignet ? 1 : 2;
			return {
				lerngruppe,
				jahrgang: this.lerngruppeJahrgang(lerngruppe, 'Weitere Klassen', 'Weitere Kurse'),
				fach: this.presenter.lerngruppeFach(lerngruppe),
				bezeichnung: this.presenter.lerngruppeBezeichnung(lerngruppe),
				offen: this.lerngruppeIstOffen(lerngruppe.id),
				prioritaet,
				fachPrioritaet: hatAuswahlbezug ? 0 : prioritaetOhneBezug,
			};
		});
		const fachDaten = new Map<string, { id: number; reihenfolge: number }>();
		for (const fach of this.manager.fachGetMengeAsList()) {
			const kuerzel = this.manager.fachdatenGetByFach(fach).kuerzel;
			if (!fachDaten.has(kuerzel)) {
				fachDaten.set(kuerzel, { id: fach.id, reihenfolge: fachDaten.size });
			}
		}
		const einsatzIds = filter.idLehrer === null ? new Set<number>() : this.lehrerEinsatzIds(filter.idLehrer);
		return matrixDatenAufbereiten(eintraege, filter, einsatzIds, fachDaten);
	}

	private matrixBereichLerngruppen(bereich: UvMatrixFilter['bereich']): UvLerngruppe[] {
		return [...this.manager.lerngruppeGetMengeByPlanungsabschnitt(this.plan)]
			.filter(item => bereich === 'alle' || (bereich === 'klassen' ? item.idKlasse !== null : item.idKurs !== null));
	}

	/** Bereitet ausschließlich die Lehrkräfte der aktuellen Planung auf. */
	public lehrerAnsicht(filter: UvLehrerFilter): UvLehrerAnsicht {
		const lehrer = [...this.manager.lehrerGetMengeByPlanungsabschnitt(this.plan)];
		const suche = filter.suche.trim().toLocaleLowerCase('de');
		const idLerngruppe = filter.idLerngruppe !== null && this.planungsObjektGueltig('lerngruppen', filter.idLerngruppe) ? filter.idLerngruppe : null;
		const eintraege = lehrer.map(item => {
			const { verplant, soll, anrechnung } = this.lehrerStunden(item.id);
			const name = `${item.nachname}, ${item.vorname}`;
			const zugeordnet = idLerngruppe !== null && this.lehrerIstZugeordnet(idLerngruppe, item.id);
			const geeignet = idLerngruppe !== null && this.lehrerIstGeeignet(idLerngruppe, item.id);
			const prioritaetOhneZuordnung = geeignet ? 1 : 2;
			const lerngruppenPrioritaet = zugeordnet ? 0 : prioritaetOhneZuordnung;
			const prioritaet = filter.auswahl?.art === 'lerngruppen' && filter.auswahl.id === idLerngruppe
				? lerngruppenPrioritaet : this.auswahlPrioritaet(filter.auswahl, 'lehrer', item.id);
			return { id: item.id, text: item.kuerzel, name, verplant, anrechnung, soll, prioritaet,
				reststunden: this.manager.lehrerReststundenGetNachZuordnung(this.plan, item.id, -1, 0),
				faecher: this.lehrerFaecher(item.id),
				zugeordnet, geeignet, zuweisbar: geeignet && !zugeordnet };
		}).filter(item => this.presenter.lehrerSuchtext(item.text, item.name, item.verplant, item.anrechnung, item.soll).includes(suche))
			.filter(item => !filter.nurGeeignete || idLerngruppe === null || item.id === filter.idLehrer || item.geeignet)
			.sort((a, b) => {
				const prioritaetA = a.prioritaet;
				const prioritaetB = b.prioritaet;
				if (prioritaetA !== prioritaetB) {
					return prioritaetA - prioritaetB;
				}
				if (filter.auswahl?.art === 'lerngruppen' && prioritaetA === 1) {
					const restA = a.reststunden ?? Number.NEGATIVE_INFINITY;
					const restB = b.reststunden ?? Number.NEGATIVE_INFINITY;
					if (restA !== restB) {
						return restB - restA;
					}
				}
				return a.text.localeCompare(b.text, 'de', { numeric: true });
			});
		return { eintraege, gesamtAnzahl: lehrer.length,
			zugeordneteLehrer: idLerngruppe === null ? [] : this.lerngruppeLehrerKuerzel(idLerngruppe) };
	}

	/** Filtert und gruppiert Lerngruppen ohne Daten anderer Ansichten aufzubereiten. */
	public lerngruppenAnsicht(filter: UvLerngruppenFilter): UvLerngruppenAnsicht {
		const lerngruppen = [...this.manager.lerngruppeGetMengeByPlanungsabschnitt(this.plan)];
		const jahrgaenge = [...new Set(lerngruppen.map(item => this.lerngruppeJahrgang(item)))].sort((a, b) => a.localeCompare(b, 'de', { numeric: true }));
		const faecher = [...new Set(lerngruppen.map(item => this.presenter.lerngruppeFach(item)))].sort((a, b) => a.localeCompare(b, 'de'));
		const kursIds = new Set(lerngruppen.map(item => item.idKurs).filter((id): id is number => id !== null));
		const klassenKurse = [
			...[...this.manager.klasseGetMengeByPlanungsabschnitt(this.plan)].map(item => ({ id: `k-${item.id}`, text: `Klasse ${item.kuerzel}` })),
			...[...this.manager.kursGetMengeByPlanungsabschnitt(this.plan)].filter(item => kursIds.has(item.id))
				.map(item => ({ id: `u-${item.id}`, text: `Kurs ${this.presenter.kursBezeichnung(item)}` })),
		];
		const einsatzIds = filter.idLehrer === null ? new Set<number>() : this.lehrerEinsatzIds(filter.idLehrer);
		const suche = filter.suche.trim().toLocaleLowerCase('de');
		const sichtbar = lerngruppen.filter(item => `${this.presenter.lerngruppeBezeichnung(item)} · ${this.presenter.lerngruppeFach(item)} ${formatUvStunden(item.wochenstunden)} h · ${this.lerngruppeIstOffen(item.id) ? 'Ohne Lehrkraft' : this.lerngruppeLehrerText(item.id)}`.toLocaleLowerCase('de').includes(suche))
			.filter(item => this.passtLerngruppeFilter(item.id, filter))
			.filter(item => !filter.nurLehrerEinsaetze || filter.idLehrer === null || einsatzIds.has(item.id));
		const gruppenMap = new Map<string, UvLerngruppe[]>();
		for (const item of sichtbar) {
			const jahrgang = this.lerngruppeJahrgang(item);
			const liste = gruppenMap.get(jahrgang) ?? [];
			liste.push(item);
			gruppenMap.set(jahrgang, liste);
		}
		const gruppen = [...gruppenMap.entries()].sort(([aName, a], [bName, b]) => {
			const aRelevant = a.some(item => this.auswahlPrioritaet(filter.auswahl, 'lerngruppen', item.id) === 0);
			const bRelevant = b.some(item => this.auswahlPrioritaet(filter.auswahl, 'lerngruppen', item.id) === 0);
			return aRelevant === bRelevant ? aName.localeCompare(bName, 'de', { numeric: true }) : Number(bRelevant) - Number(aRelevant);
		}).map(([jahrgang, liste]) => {
			liste.sort((a, b) => this.vergleicheAuswahl(filter.auswahl, 'lerngruppen', a.id, b.id,
				this.presenter.lerngruppeBezeichnung(a), this.presenter.lerngruppeBezeichnung(b)));
			return {
				jahrgang,
				lerngruppen: liste,
				stunden: liste.reduce((summe, item) => summe + item.wochenstunden, 0),
				offen: liste.filter(item => this.lerngruppeIstOffen(item.id)).length,
			};
		});
		return { gruppen, jahrgaenge, faecher, klassenKurse, anzahl: sichtbar.length,
			besetzteAnzahl: sichtbar.filter(item => !this.lerngruppeIstOffen(item.id)).length,
			gesamtStunden: sichtbar.reduce((summe, item) => summe + item.wochenstunden, 0),
			besetzteStunden: sichtbar.filter(item => !this.lerngruppeIstOffen(item.id)).reduce((summe, item) => summe + item.wochenstunden, 0) };
	}

	private passtLerngruppeFilter(id: number, filter: UvLerngruppenFilter): boolean {
		const item = this.manager.lerngruppeGetByIdOrNull(id);
		if (item === null || (filter.jahrgang !== null && this.lerngruppeJahrgang(item) !== filter.jahrgang)
			|| (filter.fach !== null && this.presenter.lerngruppeFach(item) !== filter.fach)) {
			return false;
		}
		const klasseKurs = item.idKlasse === null ? `u-${item.idKurs}` : `k-${item.idKlasse}`;
		if (filter.klasseKurs !== null && filter.klasseKurs !== klasseKurs) {
			return false;
		}
		const besetzt = !this.lerngruppeIstOffen(id);
		const schueler = this.lerngruppeSchuelerzahl(item);
		return !((filter.status === 'offen' && besetzt) || (filter.status === 'ohneSchueler' && schueler > 0)
			|| (filter.status === 'vollstaendig' && (!besetzt || schueler === 0)));
	}

	/** Berechnet die Kennzahlen ausschließlich für die Statistikansicht. */
	public statistik(): UvPlanungsStatistik {
		const lerngruppen = [...this.manager.lerngruppeGetMengeByPlanungsabschnitt(this.plan)];
		const besetzt = lerngruppen.filter(item => !this.lerngruppeIstOffen(item.id));
		return { lerngruppen: lerngruppen.length, besetzteLerngruppen: besetzt.length,
			geplanteStunden: lerngruppen.reduce((summe, item) => summe + item.wochenstunden, 0),
			offeneStunden: lerngruppen.filter(item => this.lerngruppeIstOffen(item.id)).reduce((summe, item) => summe + item.wochenstunden, 0) };
	}

	/** Liefert Schüler und ihre Beziehungen; Tabellenfilter und Spaltensortierung bleiben in der gemeinsamen Schülerliste. */
	public schuelerAnsicht(auswahl: UvPlanungsAuswahl | null): { schueler: UvPlanungsabschnittSchueler[]; relevanteIds: Set<number> | undefined } {
		const schueler = [...this.manager.planungsabschnittSchuelerGetMengeByPlanungsabschnitt(this.plan)];
		return { schueler, relevanteIds: auswahl === null ? undefined
			: new Set(schueler.filter(item => this.auswahlStatus(auswahl, 'schueler', item.idSchueler) !== 'unbeteiligt').map(item => item.idSchueler)) };
	}

	/** Liefert die Schienenliste in der für diese Ansicht relevanten Reihenfolge. */
	public schienenAnsicht(suchtext: string, auswahl: UvPlanungsAuswahl | null): UvPlanungsKandidat[] {
		const eintraege = [...this.manager.schieneGetMengeByPlanungsabschnitt(this.plan)]
			.map(item => ({ id: item.id, text: `${item.nummer} · ${item.bezeichnung ?? 'Ohne Bezeichnung'}`, zusatz: '' }));
		const suche = suchtext.trim().toLocaleLowerCase('de');
		return eintraege.filter(item => `${item.text} ${item.zusatz}`.toLocaleLowerCase('de').includes(suche))
			.sort((a, b) => this.vergleicheAuswahl(auswahl, 'schienen', a.id, b.id, a.text, b.text));
	}

	/** Berechnet die Karten und Summen des Klassenbretts für eine Suche. */
	public klassenAnsicht(suchtext: string, auswahl: UvPlanungsAuswahl | null): { stufen: UvKlassenbrettStufe[]; anzahl: number } {
		const suche = suchtext.trim().toLocaleLowerCase('de');
		const result = new Map<number, UvKlassenbrettStufe>();
		const stundenByKlasse = new Map<number, number>();
		for (const gruppe of this.manager.lerngruppeGetMengeByPlanungsabschnitt(this.plan)) {
			if (gruppe.idKlasse !== null) {
				stundenByKlasse.set(gruppe.idKlasse, (stundenByKlasse.get(gruppe.idKlasse) ?? 0) + gruppe.wochenstunden);
			}
		}
		const getStufe = (id: number, name: string, sortierung: number): UvKlassenbrettStufe => {
			let stufe = result.get(id);
			if (stufe === undefined) {
				stufe = { id, name, sortierung, klassen: [], schueler: 0, stunden: 0 };
				result.set(id, stufe);
			}
			return stufe;
		};
		const jahrgang = (gruppe: UvSchuelergruppe): { id: number; name: string; sortierung: number } | null => {
			const jahrgaenge = [...this.manager.jahrgangsdatenGetMengeBySchuelergruppe(gruppe)];
			if (jahrgaenge.length !== 1) {
				return null;
			}
			const j = jahrgaenge[0];
			return { id: j.id, name: j.kuerzel ?? `Jahrgang ${j.id}`, sortierung: j.sortierung };
		};
		for (const klasse of this.manager.klasseGetMengeByPlanungsabschnitt(this.plan)) {
			if (!`${klasse.kuerzel} ${klasse.bezeichnung ?? ''}`.toLocaleLowerCase('de').includes(suche)) {
				continue;
			}
			const gruppe = this.manager.schuelergruppeGetByKlasse(klasse);
			const j = jahrgang(gruppe);
			const stufe = j === null ? getStufe(-1, 'Weitere', Number.MAX_SAFE_INTEGER) : getStufe(j.id, j.name, j.sortierung);
			const stunden = stundenByKlasse.get(klasse.id) ?? 0;
			const schueler = this.manager.planungsabschnittSchuelerGetMengeBySchuelergruppe(gruppe).size();
			stufe.klassen.push({ id: klasse.id, kuerzel: klasse.kuerzel, schueler, stunden });
			stufe.schueler += schueler;
			stufe.stunden += stunden;
		}
		for (const stufe of result.values()) {
			stufe.klassen.sort((a, b) => this.vergleicheAuswahl(auswahl, 'klassen', a.id, b.id, a.kuerzel, b.kuerzel));
		}
		const stufen = [...result.values()].sort((a, b) => {
			const aRelevant = a.klassen.some(item => this.auswahlPrioritaet(auswahl, 'klassen', item.id) === 0);
			const bRelevant = b.klassen.some(item => this.auswahlPrioritaet(auswahl, 'klassen', item.id) === 0);
			if (aRelevant !== bRelevant) {
				return Number(bRelevant) - Number(aRelevant);
			}
			const vergleich = a.sortierung - b.sortierung;
			return vergleich === 0 ? a.name.localeCompare(b.name, 'de', { numeric: true }) : vergleich;
		});
		return { stufen, anzahl: stufen.reduce((summe, stufe) => summe + stufe.klassen.length, 0) };
	}

	// Gültigkeit und Löschsperren.

	/** Prüft Existenz und Zugehörigkeit eines Detailobjekts zum aktuellen Planungsabschnitt. */
	public planungsObjektGueltig(art: UvBereichArt, id: number): boolean {
		if (this.plan.id === -1) {
			return false;
		}

		switch (art) {
			case 'lerngruppen': {
				return this.manager.lerngruppeGetByIdOrNull(id)?.idPlanungsabschnitt === this.plan.id;
			}
			case 'klassen': {
				return this.manager.klasseGetByIdOrNull(id)?.idPlanungsabschnitt === this.plan.id;
			}
			case 'schueler': {
				return this.manager.planungsabschnittSchuelerGetByIdOrNull(this.plan.id, id) !== null;
			}
			case 'schienen': {
				return this.manager.schieneGetByIdOrNull(id)?.idPlanungsabschnitt === this.plan.id;
			}
			case 'lehrer': {
				const lehrer = this.manager.lehrerGetByIdOrNull(id);
				return lehrer !== null && this.manager.lehrerIsInPlanungsabschnitt(lehrer, this.plan);
			}
			case 'regeln': {
				return this.regeln.some(regel => regel.id === id);
			}
			default: {
				return false;
			}
		}
	}

	/** Verwendet für die Einzellöschung dieselben fachlichen Löschsperren wie für die Sammelauswahl. */
	public planungsObjektLoeschbar(art: UvBereichArt, id: number): boolean {
		return this.planungsObjektGueltig(art, id) && this.sammelauswahlIds(art).has(id);
	}

	/** Prüft die gesamte Löschanforderung vor dem ersten Schreibzugriff und entfernt doppelte IDs. */
	public planungsLoeschauswahlPruefen(art: UvBereichArt, ids: number[]): Set<number> {
		const auswahl = new Set(ids);
		const erlaubt = this.sammelauswahlIds(art);
		for (const id of auswahl) {
			if (!erlaubt.has(id)) {
				throw new Error('Die Auswahl enthält nicht mehr vorhandene oder verwendete Einträge.');
			}
		}

		return auswahl;
	}

	/** Liefert alle für Sammelaktionen zulässigen IDs unabhängig von Such- und Ansichtsfiltern. */
	public sammelauswahlIds(art: UvBereichArt): Set<number> {
		switch (art) {
			case 'lerngruppen': {
				return new Set([...this.manager.lerngruppeGetMengeByPlanungsabschnitt(this.plan)].map(item => item.id));
			}
			case 'schueler': {
				return new Set([...this.manager.planungsabschnittSchuelerGetMengeByPlanungsabschnitt(this.plan)].map(item => item.idSchueler));
			}
			case 'klassen': {
				const klassen = this.manager.klasseGetMengeByPlanungsabschnitt(this.plan);
				const verwendet = new Set([...this.manager.klasseGetMengeVerwendetByKlasseMenge(klassen)].map(item => item.id));
				return new Set([...klassen].filter(item => !verwendet.has(item.id)).map(item => item.id));
			}
			case 'schienen': {
				const schienen = this.manager.schieneGetMengeByPlanungsabschnitt(this.plan);
				const verwendet = new Set([...this.manager.schieneGetMengeVerwendetBySchieneMenge(schienen)].map(item => item.id));
				return new Set([...schienen].filter(item => !verwendet.has(item.id)).map(item => item.id));
			}
			default: {
				return new Set<number>();
			}
		}
	}

	// Regelanzeige und Fixierungen.

	/** Prüft den aktuellen Regelbestand für die Anzeige. */
	public regelStatus(): { fehlerhafteIds: Set<number>; konflikt: string } {
		try {
			const manager = this.regelManager();
			return { fehlerhafteIds: new Set([...manager.regelnGetMengeFehlerhaftAsList()].map(regel => regel.id)), konflikt: '' };
		} catch (error) {
			return { fehlerhafteIds: new Set<number>(), konflikt: error instanceof Error ? error.message : 'Die Regeln enthalten einen Konflikt.' };
		}
	}

	public regelGruppen(kategorie: string | null, suchtext: string, auswahl: UvPlanungsAuswahl | null): { name: string; regeln: UvBlockungRegel[] }[] {
		const suche = suchtext.trim().toLocaleLowerCase('de');
		const definitionen = new Map(uvRegelDefinitionen.map(item => [item.typ.nr, item]));
		const gruppen = new Map<string, UvBlockungRegel[]>();
		const beschreibungen = new Map<number, string>();
		const prioritaeten = new Map<number, number>();
		for (const regel of this.regeln) {
			const name = definitionen.get(regel.typ)?.kategorie ?? 'Unbekannte Regeltypen';
			if (kategorie !== null && kategorie !== name) {
				continue;
			}
			const beschreibung = this.presenter.regelBeschreibung(regel, this.plan);
			if (!beschreibung.toLocaleLowerCase('de').includes(suche)) {
				continue;
			}
			beschreibungen.set(regel.id, beschreibung);
			prioritaeten.set(regel.id, this.auswahlPrioritaet(auswahl, 'regeln', regel.id));
			const gruppe = gruppen.get(name) ?? [];
			gruppe.push(regel);
			gruppen.set(name, gruppe);
		}
		return [...gruppen.entries()].map(([name, regeln]) => {
			regeln.sort((a, b) => {
				const prioritaet = (prioritaeten.get(a.id) ?? 2) - (prioritaeten.get(b.id) ?? 2);
				return prioritaet !== 0 ? prioritaet : (beschreibungen.get(a.id) ?? '').localeCompare(beschreibungen.get(b.id) ?? '', 'de', { numeric: true });
			});
			return { name, regeln };
		}).sort((a, b) => {
			const aRelevant = a.regeln.some(item => prioritaeten.get(item.id) === 0);
			const bRelevant = b.regeln.some(item => prioritaeten.get(item.id) === 0);
			return aRelevant === bRelevant ? a.name.localeCompare(b.name, 'de') : Number(bRelevant) - Number(aRelevant);
		});
	}

	/** Liefert alle Fixierungsregeln für eine Lehrkraft in einer Lerngruppe, auch deaktivierte. */
	public lehrerFixierungsRegeln(idLerngruppe: number, idLehrer: number): UvBlockungRegel[] {
		return [...UvRegelManager.lehrerFixierungsRegeln(ArrayList.of(...this.regeln), idLerngruppe, idLehrer)];
	}

	/** Prüft, ob eine wirksame Fixierung für die Zuordnung besteht. */
	public lehrerIstFixiert(idLerngruppe: number, idLehrer: number): boolean {
		return UvRegelManager.lehrerIstFixiert(ArrayList.of(...this.regeln), idLerngruppe, idLehrer);
	}

	/** Erstellt eine fachlich geprüfte Fixierungsregel für eine bestehende Zuordnung. */
	public lehrerFixierungsEntwurf(idLerngruppe: number, idLehrer: number): UvBlockungRegel {
		return this.regelManager().lehrerFixierungsEntwurf(this.plan, idLerngruppe, idLehrer);
	}

	/** Liefert Fixierungsregeln für eine Klassenleitung oder Stellvertretung, auch deaktivierte. */
	public klassenlehrerFixierungsRegeln(idKlasse: number, idLehrer: number): UvBlockungRegel[] {
		return [...UvRegelManager.klassenlehrerFixierungsRegeln(ArrayList.of(...this.regeln), idKlasse, idLehrer)];
	}

	/** Prüft, ob eine Klassenlehrer-Zuordnung durch eine wirksame Regel fixiert ist. */
	public klassenlehrerIstFixiert(idKlasse: number, idLehrer: number): boolean {
		return UvRegelManager.klassenlehrerIstFixiert(ArrayList.of(...this.regeln), idKlasse, idLehrer);
	}

	/** Erstellt die Fixierungsregel passend zur Reihenfolge der bestehenden Klassenlehrer-Zuordnung. */
	public klassenlehrerFixierungsEntwurf(idKlasse: number, idLehrer: number, reihenfolge: number): UvBlockungRegel {
		return this.regelManager().klassenlehrerFixierungsEntwurf(this.plan, idKlasse, idLehrer, reihenfolge);
	}

	/** Ermittelt die Regeländerungen zum Umschalten einer Lehrkraftfixierung, ohne bestehende Regeln zu verändern. */
	public lehrerFixierungUmschaltenRegeln(art: 'klasse' | 'lerngruppe', idZiel: number, idLehrer: number, reihenfolge: number): UvBlockungRegel[] {
		return [...UvRegelManager.lehrerFixierungUmschaltenRegeln(
			this.manager, ArrayList.of(...this.regeln), this.plan, art === 'klasse', idZiel, idLehrer, reihenfolge
		)];
	}

	/** Prüft einen Regelentwurf zusammen mit allen übrigen Regeln. */
	public regelIstGueltig(regel: UvBlockungRegel): boolean {
		return this.regelManager().regelIstGueltig(regel);
	}

	/** Erstellt und prüft eine Regel aus den Eingaben des Regel-Dialogs. */
	public regelEntwurf(vorlage: UvBlockungRegel, werte: (number | null)[], listenwerte: number[]): UvBlockungRegel {
		const definition = uvRegelDefinitionen.find(item => item.typ.nr === vorlage.typ);
		if (werte.length !== definition?.felder.length || werte.some(wert => wert === null || !Number.isInteger(wert))) {
			throw new Error('Bitte alle Felder ausfüllen.');
		}
		const regel = Object.assign(new UvBlockungRegel(), vorlage);
		regel.parameter = new ArrayList<number>();
		for (const wert of [...werte, ...listenwerte]) {
			if (wert === null) {
				throw new Error('Bitte alle Felder ausfüllen.');
			}
			regel.parameter.add(wert);
		}
		if (!this.regelIstGueltig(regel)) {
			throw new Error('Die Parameter dieser Regel sind fachlich ungültig.');
		}
		return regel;
	}

	private regelManager(): UvRegelManager {
		this.regelPruefung ??= new UvRegelManager(this.manager, ArrayList.of(...this.regeln));
		return this.regelPruefung;
	}

	// Mitgliedschaften und Zuordnungsauswahl.

	/** Sortiert und filtert die verfügbaren Zuordnungskandidaten. */
	public kandidatenSuchen(eintraege: UvPlanungsKandidat[], suchtext: string): UvPlanungsKandidat[] {
		const suche = suchtext.trim().toLocaleLowerCase('de');
		return eintraege.filter(item => `${item.text} ${item.zusatz}`.toLocaleLowerCase('de').includes(suche))
			.sort((a, b) => a.text.localeCompare(b.text, 'de', { numeric: true }));
	}

	/** Filtert bereits aufbereitete Lehrkraft-Kandidaten mit derselben Suche wie die Lehrkräfteansicht. */
	public lehrerKandidatenSuchen(eintraege: UvLehrerAnsichtEintrag[], suchtext: string): UvLehrerAnsichtEintrag[] {
		const suche = suchtext.trim().toLocaleLowerCase('de');
		return eintraege.filter(item => this.presenter.lehrerSuchtext(item.text, item.name, item.verplant, item.anrechnung, item.soll).includes(suche));
	}

	/** Zeigt Mitglieder und verfügbare Schüler für eine noch nicht gespeicherte Lerngruppe. */
	public schuelerEntwurf(idSchuelergruppe: number | null, hinzugefuegte: UvPlanungsabschnittSchueler[]): UvSchuelerEntwurfAnsicht {
		const gruppe = idSchuelergruppe === null ? null : this.manager.schuelergruppeGetByIdOrNull(idSchuelergruppe);
		if (idSchuelergruppe !== null && (gruppe?.idPlanungsabschnitt !== this.plan.id)) {
			return { mitglieder: [], kandidaten: [] };
		}
		const vorhandene = gruppe === null ? [] : [...this.manager.planungsabschnittSchuelerGetMengeBySchuelergruppe(gruppe)];
		const vorhandeneIds = new Set(vorhandene.map(item => item.idSchueler));
		const neue = hinzugefuegte.filter(item => !vorhandeneIds.has(item.idSchueler));
		const mitglieder = [...vorhandene, ...neue];
		const mitgliederIds = new Set(mitglieder.map(item => item.idSchueler));
		const kandidaten = [...this.manager.planungsabschnittSchuelerGetMengeByPlanungsabschnitt(this.plan)]
			.filter(item => !mitgliederIds.has(item.idSchueler));
		return { mitglieder, kandidaten };
	}

	/** Liefert die Lehrkräfte für die Aufnahme in den Planungsabschnitt und die geschützten Mitglieder. */
	public lehrerPlanungsabschnittAuswahl(): { kandidaten: { id: number; text: string; zusatz: string }[]; verwendet: Set<number>; anzahl: number } {
		const mitglieder = this.manager.lehrerGetMengeByPlanungsabschnitt(this.plan);
		const verwendet = new Set([...this.manager.lehrerGetMengeVerwendetInPlanungsabschnittByLehrerMenge(this.plan, mitglieder)].map(item => item.id));
		const kandidaten = [...this.manager.lehrerGetMengeTaetigAberNichtInPlanungsabschnitt(this.plan)]
			.map(item => ({ id: item.id, text: `${item.nachname}, ${item.vorname}`, zusatz: item.kuerzel }))
			.sort((a, b) => a.zusatz.localeCompare(b.zusatz, 'de'));
		return { kandidaten, verwendet, anzahl: mitglieder.size() };
	}

	/** Prüft die gesamte Auswahl vor dem Hinzufügen von Lehrkräften zum Planungsabschnitt. */
	public lehrerPlanungsabschnittHinzufuegenPruefen(ids: number[]): UvLehrer[] {
		return [...this.manager.lehrerPlanungsabschnittHinzufuegenPruefen(this.plan, ArrayList.of(...ids))];
	}

	/** Prüft Mitgliedschaft und Verwendung vor dem Entfernen von Lehrkräften aus dem Planungsabschnitt. */
	public lehrerPlanungsabschnittEntfernenPruefen(ids: number[]): UvLehrer[] {
		return [...this.manager.lehrerPlanungsabschnittEntfernenPruefen(this.plan, ArrayList.of(...ids))];
	}

	/** Prüft den gemeinsamen Planungsabschnitt für Änderungen der Schülergruppen-Mitgliedschaft. */
	public schuelergruppenAenderungPruefen(idSchuelergruppe: number, idsSchueler: number[]): UvSchuelergruppe {
		return this.manager.schuelergruppenAenderungPruefen(this.plan, idSchuelergruppe, ArrayList.of(...idsSchueler));
	}

	/** Liefert die aufbereiteten Zuordnungskandidaten und ihre ungefilterten IDs. */
	public zuordnungsAuswahl(bearbeitung: UvZuordnungBearbeitung, nurGeeignete: boolean): UvZuordnungsAuswahl {
		const daten = this.zuordnungsKandidaten(bearbeitung, nurGeeignete);
		const verfuegbareIds = new Set([
			...daten.eintraege.map(item => item.id),
			...daten.lehrer.map(item => item.id),
			...daten.schueler.map(item => item.idSchueler),
		]);
		return { ...daten, verfuegbareIds };
	}

	private lehrerZuordnungsKandidaten(
		art: 'lerngruppe-lehrer' | 'klasse-lehrer',
		idZiel: number,
		nurGeeignete: boolean
	): Pick<UvZuordnungsAuswahl, 'lehrer' | 'zugeordnet'> {
		const istLerngruppe = art === 'lerngruppe-lehrer';
		const gruppe = istLerngruppe ? this.manager.lerngruppeGetByIdOrNull(idZiel) : null;
		const klasse = istLerngruppe ? null : this.manager.klasseGetByIdOrNull(idZiel);
		let zuordnungen: Iterable<{ idLehrer: number }>;
		if (gruppe?.idPlanungsabschnitt === this.plan.id) {
			zuordnungen = this.manager.lerngruppenLehrerGetMengeByLerngruppe(gruppe);
		} else if (klasse?.idPlanungsabschnitt === this.plan.id) {
			zuordnungen = this.manager.klassenLehrerGetMengeByKlasse(klasse);
		} else {
			return { lehrer: [], zugeordnet: 0 };
		}

		const ids = new Set([...zuordnungen].map(item => item.idLehrer));
		const lehrer = this.lehrerAnsicht({
			idLehrer: null,
			suche: '',
			auswahl: { art: istLerngruppe ? 'lerngruppen' : 'klassen', id: idZiel },
			idLerngruppe: gruppe?.id ?? null,
			nurGeeignete: istLerngruppe && nurGeeignete,
		}).eintraege.filter(item => !ids.has(item.id));
		return { lehrer, zugeordnet: ids.size };
	}

	/** Liefert die noch verfügbaren Kandidaten für eine Zuordnung. */
	private zuordnungsKandidaten(bearbeitung: UvZuordnungBearbeitung, nurGeeignete: boolean): Omit<UvZuordnungsAuswahl, 'verfuegbareIds'> {
		const ziel = bearbeitung.idZiel;
		const leer: Omit<UvZuordnungsAuswahl, 'verfuegbareIds'> = { eintraege: [], lehrer: [], schueler: [], zugeordnet: 0 };
		switch (bearbeitung.art) {
			case 'lerngruppe-lehrer':
			case 'klasse-lehrer': {
				return { ...leer, ...this.lehrerZuordnungsKandidaten(bearbeitung.art, ziel, nurGeeignete) };
			}
			case 'lerngruppe-schiene': {
				const gruppe = this.manager.lerngruppeGetByIdOrNull(ziel);
				if (gruppe?.idPlanungsabschnitt !== this.plan.id) {
					return leer;
				}
				const ids = new Set([...this.manager.lerngruppenSchieneGetMengeByLerngruppe(gruppe)].map(item => item.idSchiene));
				return { ...leer, eintraege: [...this.manager.schieneGetMengeByPlanungsabschnitt(this.plan)].filter(item => !ids.has(item.id))
					.map(item => ({ id: item.id, text: `${item.nummer} ${item.bezeichnung ?? ''}`.trim(), zusatz: '' })), zugeordnet: ids.size };
			}
			case 'schiene-lerngruppe': {
				const schiene = this.manager.schieneGetByIdOrNull(ziel);
				if (schiene?.idPlanungsabschnitt !== this.plan.id) {
					return leer;
				}
				const ids = new Set([...this.manager.lerngruppenSchieneGetMengeBySchiene(schiene)].map(item => item.idLerngruppe));
				return { ...leer, eintraege: [...this.manager.lerngruppeGetMengeByPlanungsabschnitt(this.plan)].filter(item => !ids.has(item.id))
					.map(item => ({ id: item.id, text: this.presenter.lerngruppeBezeichnung(item), zusatz: `${this.presenter.lerngruppeFach(item)} · ${item.wochenstunden} h` })), zugeordnet: ids.size };
			}
			case 'gruppe-schueler': {
				const gruppe = this.manager.schuelergruppeGetByIdOrNull(ziel);
				if (gruppe?.idPlanungsabschnitt !== this.plan.id) {
					return leer;
				}
				const ids = new Set([...this.manager.planungsabschnittSchuelerGetMengeBySchuelergruppe(gruppe)].map(item => item.idSchueler));
				const schueler = [...this.manager.planungsabschnittSchuelerGetMengeByPlanungsabschnitt(this.plan)]
					.filter(item => !ids.has(item.idSchueler));
				return { ...leer, schueler, zugeordnet: ids.size };
			}
			case 'abschnitt-zeitraster': {
				if (ziel !== this.plan.id) {
					return leer;
				}
				const raster = [...this.manager.zeitrasterGetMengeGueltigByPlanungsabschnitt(this.plan)];
				const ids = new Set(raster.filter(item => this.manager.zeitrasterIsInPlanungsabschnitt(item, this.plan)).map(item => item.id));
				return { ...leer, eintraege: raster.filter(item => !ids.has(item.id)).map(item => ({ id: item.id, text: item.bezeichnung ?? `Zeitraster ${item.id}`, zusatz: '' })), zugeordnet: ids.size };
			}
			case 'schiene-jahrgang':
			case 'gruppe-jahrgang': {
				const objekt = bearbeitung.art === 'schiene-jahrgang' ? this.manager.schieneGetByIdOrNull(ziel) : this.manager.schuelergruppeGetByIdOrNull(ziel);
				if (objekt?.idPlanungsabschnitt !== this.plan.id) {
					return leer;
				}
				const ids = new Set(objekt.idsJahrgaengeErlaubt);
				return { ...leer, eintraege: [...this.manager.jahrgangsdatenGetMenge()].filter(item => !ids.has(item.id))
					.map(item => ({ id: item.id, text: item.kuerzel ?? `Jahrgang ${item.id}`, zusatz: '' })), zugeordnet: ids.size };
			}
			case 'zeitraster-jahrgang': {
				const raster = this.manager.zeitrasterGetByIdOrNull(ziel);
				if (raster === null) {
					return leer;
				}
				const ids = new Set([...this.manager.jahrgangsdatenGetMengeByPlanungsabschnittAndZeitraster(this.plan, raster)].map(item => item.id));
				return { ...leer, eintraege: [...this.manager.jahrgangsdatenGetMenge()].filter(item => !ids.has(item.id))
					.map(item => ({ id: item.id, text: item.kuerzel ?? `Jahrgang ${item.id}`, zusatz: '' })), zugeordnet: ids.size };
			}
		}
	}

	// Auswahlsteuerung und fachliche Bezüge.

	/** Bestimmt Auswahl und Navigation nach einem Klick in einem Arbeitsbereich. */
	public auswaehlen(
		aktuell: UvPlanungsAuswahl | null,
		art: UvPlanungsAuswahlArt,
		id: number,
		quelle: 'links' | 'rechts',
		links: UvBereichArt,
		rechts: UvBereichArt,
		detailGeoeffnet: boolean
	): UvPlanungsAuswahlEntscheidung {
		if (aktuell?.art === art && aktuell.id === id && !detailGeoeffnet) {
			return { auswahl: null, navigation: null };
		}
		const zielIstDetails = quelle === 'links' ? rechts === 'details' : links === 'details';
		const quelleIstDetails = quelle === 'links' ? links === 'details' : rechts === 'details';
		const rueckweg = detailGeoeffnet ? 'uebersicht' : null;
		return {
			auswahl: { art, id },
			navigation: zielIstDetails || quelleIstDetails ? 'detail' : rueckweg,
		};
	}

	/** Bestimmt die Navigation bei einem Wechsel der Arbeitsbereichsansicht. */
	public ansichtWechseln(
		art: UvBereichArt,
		links: UvBereichArt,
		rechts: UvBereichArt,
		auswahl: UvPlanungsAuswahl | null,
		detailGeoeffnet: boolean
	): 'detail' | 'uebersicht' | null {
		if (art === 'details' && auswahl !== null) {
			return 'detail';
		}
		return links !== 'details' && rechts !== 'details' && detailGeoeffnet ? 'uebersicht' : null;
	}

	/** Ermittelt direkte und abhängige Bezüge einmal pro Auswahl und Managerstand. */
	private referenzen(auswahl: UvPlanungsAuswahl): Referenzen {
		const key = `${auswahl.art}-${auswahl.id}`;
		const cached = this.referenzenByAuswahl.get(key);
		if (cached !== undefined) {
			return cached;
		}
		const refs: Referenzen = { lerngruppe: new Set(), klasse: new Set(), lehrer: new Set(), schueler: new Set(), schiene: new Set(), fach: new Set(), jahrgang: new Set() };
		const art: Partial<Record<UvPlanungsAuswahlArt, ReferenzArt>> = { lerngruppen: 'lerngruppe', klassen: 'klasse', lehrer: 'lehrer', schueler: 'schueler', schienen: 'schiene' };
		const direkt = art[auswahl.art];
		if (direkt !== undefined) {
			refs[direkt].add(auswahl.id);
		} else if (auswahl.art === 'regeln') {
			this.regelAuswahlReferenzen(auswahl.id, refs);
		}
		const quellen: Referenzen = { lerngruppe: new Set(refs.lerngruppe), klasse: new Set(refs.klasse), lehrer: new Set(refs.lehrer), schueler: new Set(refs.schueler), schiene: new Set(refs.schiene), fach: new Set(refs.fach), jahrgang: new Set(refs.jahrgang) };
		const schueler = auswahl.art === 'schueler' ? this.manager.planungsabschnittSchuelerGetByIdOrNull(this.plan.id, auswahl.id) : null;
		if (schueler !== null) {
			refs.jahrgang.add(schueler.idJahrgang);
			if (schueler.idKlasse !== null) {
				refs.klasse.add(schueler.idKlasse);
			}
		}
		for (const gruppe of this.manager.lerngruppeGetMengeByPlanungsabschnitt(this.plan)) {
			const schuelergruppe = this.manager.schuelergruppeGetByLerngruppe(gruppe);
			const kurs = gruppe.idKurs === null ? null : this.manager.kursGetByIdOrNull(gruppe.idKurs);
			const idFach = kurs?.idFach ?? gruppe.idFach;
			const idsJahrgaenge = schuelergruppe === null ? [] : [...this.manager.jahrgangsdatenGetMengeBySchuelergruppe(schuelergruppe)].map(item => item.id);
			const idsSchueler = schuelergruppe === null ? [] : [...this.manager.planungsabschnittSchuelerGetMengeBySchuelergruppe(schuelergruppe)].map(item => item.idSchueler);
			const idsLehrer = [...this.manager.lerngruppenLehrerGetMengeByLerngruppe(gruppe)].map(item => item.idLehrer);
			const idsSchienen = [...this.manager.lerngruppenSchieneGetMengeByLerngruppe(gruppe)].map(item => item.idSchiene);
			if (!quellen.lerngruppe.has(gruppe.id) && (gruppe.idKlasse === null || !quellen.klasse.has(gruppe.idKlasse))
				&& (idFach === null || !quellen.fach.has(idFach)) && !idsJahrgaenge.some(id => quellen.jahrgang.has(id))
				&& !idsSchueler.some(id => quellen.schueler.has(id)) && !idsLehrer.some(id => quellen.lehrer.has(id))
				&& !idsSchienen.some(id => quellen.schiene.has(id))) {
				continue;
			}
			this.gruppeErgaenzen(gruppe, refs, idFach, idsJahrgaenge, idsSchueler, idsLehrer, idsSchienen);
		}
		for (const zuordnung of this.manager.klassenLehrerGetMengeByPlanungsabschnitt(this.plan)) {
			if (refs.klasse.has(zuordnung.idKlasse)) {
				refs.lehrer.add(zuordnung.idLehrer);
			}
		}
		this.referenzenByAuswahl.set(key, refs);
		return refs;
	}

	private regelAuswahlReferenzen(idRegel: number, refs: Referenzen): void {
		const regel = this.regeln.find(item => item.id === idRegel);
		if (regel === undefined) {
			return;
		}

		this.regelReferenzen(regel, refs);
		const definition = uvRegelDefinitionen.find(item => item.typ.nr === regel.typ);
		if (definition?.kategorie === 'Klassenleitung' && Object.values(refs).every(ids => ids.size === 0)) {
			for (const klasse of this.manager.klasseGetMengeByPlanungsabschnitt(this.plan)) {
				refs.klasse.add(klasse.id);
			}
		} else if (definition?.kategorie === 'Soll-Ist-Abweichung' && refs.lehrer.size === 0) {
			for (const lehrer of this.manager.lehrerGetMengeByPlanungsabschnitt(this.plan)) {
				refs.lehrer.add(lehrer.id);
			}
		}
	}

	private gruppeErgaenzen(
		gruppe: UvLerngruppe,
		refs: Referenzen,
		idFach: number | null,
		jahrgaenge: number[],
		schueler: number[],
		lehrer: number[],
		schienen: number[]
	): void {
		refs.lerngruppe.add(gruppe.id);
		if (gruppe.idKlasse !== null) {
			refs.klasse.add(gruppe.idKlasse);
		}
		if (idFach !== null) {
			refs.fach.add(idFach);
		}
		for (const id of jahrgaenge) {
			refs.jahrgang.add(id);
		}
		for (const id of schueler) {
			refs.schueler.add(id);
		}
		for (const id of lehrer) {
			refs.lehrer.add(id);
		}
		for (const id of schienen) {
			refs.schiene.add(id);
		}
	}

	private regelReferenzen(regel: UvBlockungRegel, refs: Referenzen): void {
		for (const { art, id } of getUvRegelReferenzen(regel)) {
			this.referenzHinzufuegen(refs, art, id);
		}
	}

	private referenzHinzufuegen(refs: Referenzen, art: UvRegelFeldArt, id: number | undefined): void {
		if (id === undefined) {
			return;
		}
		if (art === 'lerngruppe' || art === 'klasse' || art === 'lehrer' || art === 'fach' || art === 'jahrgang') {
			refs[art].add(id);
		}
	}

	private regelBetrifft(regel: UvBlockungRegel, refs: Referenzen): boolean {
		const definition = uvRegelDefinitionen.find(item => item.typ.nr === regel.typ);
		if (definition === undefined) {
			return false;
		}
		if (definition.kategorie === 'Klassenleitung' && !definition.felder.some(feld => feld.art === 'klasse' || feld.art === 'lehrer') && refs.klasse.size > 0) {
			return true;
		}
		if (definition.kategorie === 'Soll-Ist-Abweichung' && !definition.felder.some(feld => feld.art === 'lehrer') && refs.lehrer.size > 0) {
			return true;
		}
		return getUvRegelReferenzen(regel).some(({ art, id }) => this.feldBetrifft(art, id, refs));
	}

	private feldBetrifft(art: UvRegelFeldArt, id: number | undefined, refs: Referenzen): boolean {
		return id !== undefined && (art === 'lerngruppe' || art === 'klasse' || art === 'lehrer' || art === 'fach' || art === 'jahrgang') && refs[art].has(id);
	}

	/** Prüft, ob ein Eintrag direkt oder über seine Zuordnungen zur Auswahl gehört. */
	private betrifft(auswahl: UvPlanungsAuswahl | null, art: UvPlanungsAuswahlArt, id: number): boolean {
		if (auswahl === null) {
			return false;
		}
		if (auswahl.art === art) {
			return auswahl.id === id;
		}
		const refs = this.referenzen(auswahl);
		if (art === 'regeln') {
			const regel = this.regeln.find(item => item.id === id);
			return regel !== undefined && this.regelBetrifft(regel, refs);
		}
		const key: Record<Exclude<UvPlanungsAuswahlArt, 'regeln'>, ReferenzArt> = { lerngruppen: 'lerngruppe', klassen: 'klasse', lehrer: 'lehrer', schueler: 'schueler', schienen: 'schiene' };
		return refs[key[art]].has(id);
	}

	/** Unterscheidet die direkte Auswahl von fachlichen Bezügen und übrigen Einträgen. */
	public auswahlStatus(auswahl: UvPlanungsAuswahl | null, art: UvPlanungsAuswahlArt, id: number): UvAuswahlStatus {
		if (auswahl === null) {
			return 'neutral';
		}
		if (auswahl.art === art && auswahl.id === id) {
			return 'direkt';
		}
		if (art === 'lehrer' && auswahl.art === 'lerngruppen') {
			if (this.lehrerIstZugeordnet(auswahl.id, id)) {
				return 'bezogen';
			}
			return this.lehrerIstGeeignet(auswahl.id, id) ? 'geeignet' : 'unbeteiligt';
		}
		if (this.betrifft(auswahl, art, id)) {
			return 'bezogen';
		}
		return 'unbeteiligt';
	}

	/** Ordnet Einträge nach direktem Bezug, geeigneten Lehrkräften und übrigen Einträgen. */
	public auswahlPrioritaet(auswahl: UvPlanungsAuswahl | null, art: UvPlanungsAuswahlArt, id: number): number {
		const status = this.auswahlStatus(auswahl, art, id);
		if (status === 'unbeteiligt') {
			return 2;
		}
		return status === 'geeignet' ? 1 : 0;
	}

	private vergleicheAuswahl(auswahl: UvPlanungsAuswahl | null, art: UvPlanungsAuswahlArt,
		idA: number, idB: number, textA: string, textB: string): number {
		const relevanz = this.auswahlPrioritaet(auswahl, art, idA) - this.auswahlPrioritaet(auswahl, art, idB);
		return relevanz === 0 ? textA.localeCompare(textB, 'de', { numeric: true }) : relevanz;
	}

}
