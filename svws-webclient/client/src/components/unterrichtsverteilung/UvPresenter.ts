import type { Schuljahresabschnitt } from '@core/asd/data/schule/Schuljahresabschnitt';
import { LehrerAnrechnungsgrund } from '@core/asd/types/lehrer/LehrerAnrechnungsgrund';
import { LehrerMehrleistungsarten } from '@core/asd/types/lehrer/LehrerMehrleistungsarten';
import { LehrerMinderleistungsarten } from '@core/asd/types/lehrer/LehrerMinderleistungsarten';
import type { UvKurs } from '@core/core/data/uv/UvKurs';
import type { UvLerngruppe } from '@core/core/data/uv/UvLerngruppe';
import type { UvLerngruppenSchiene } from '@core/core/data/uv/UvLerngruppenSchiene';
import type { UvSchuelergruppe } from '@core/core/data/uv/UvSchuelergruppe';
import type { UvManager } from '@core/core/utils/uv/UvManager';

function schuljahresabschnittText(abschnitt: Schuljahresabschnitt): string {
	return `${abschnitt.schuljahr}/${abschnitt.schuljahr + 1} – ${abschnitt.abschnitt}. Abschnitt`;
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
	let katalogEintrag = LehrerAnrechnungsgrund.data().getEintragBySchuljahrUndSchluessel(schuljahr, grund);
	if (grund.startsWith('1')) {
		katalogEintrag = LehrerMehrleistungsarten.data().getEintragBySchuljahrUndSchluessel(schuljahr, grund);
	} else if (grund.startsWith('2')) {
		katalogEintrag = LehrerMinderleistungsarten.data().getEintragBySchuljahrUndSchluessel(schuljahr, grund);
	}
	return katalogEintrag === null ? grund : `${katalogEintrag.kuerzel} - ${katalogEintrag.text}`;
}

/** Liefert einheitliche, ausschließlich für die Oberfläche bestimmte UV-Anzeigetexte. */
export function useUvPresenter(manager: UvManager) {
	function kursKurzbezeichnung(kurs: Pick<UvKurs, 'idFach' | 'kursart' | 'kursnummer'>): string {
		const fach = manager.fachGetByIdOrNull(kurs.idFach);
		const fachKuerzel = fach === null ? 'Fach nicht verfügbar' : manager.fachdatenGetByFach(fach).kuerzel;
		return `${fachKuerzel}-${kurs.kursart}${kurs.kursnummer}`;
	}

	function kursBezeichnung(kurs: Pick<UvKurs, 'idFach' | 'kursart' | 'kursnummer'>, schuelergruppe?: UvSchuelergruppe): string {
		const gruppe = schuelergruppe ?? manager.schuelergruppeGetByKurs(kurs as UvKurs);
		const jahrgaenge = [...manager.jahrgangsdatenGetMengeBySchuelergruppe(gruppe)]
			.sort((a, b) => a.sortierung === b.sortierung ? a.id - b.id : a.sortierung - b.sortierung)
			.map(jahrgang => jahrgang.kuerzel ?? `JG ${jahrgang.id}`).join('/');
		return `${jahrgaenge.length === 0 ? '' : jahrgaenge + ' '}${kursKurzbezeichnung(kurs)}`;
	}

	function fachText(fach: { id: number }): string {
		const daten = manager.fachdatenGetByFach(manager.fachGetByIdOrException(fach.id));
		return `${daten.bezeichnung} (${daten.kuerzel})`;
	}

	function lerngruppeBezeichnung(lerngruppe: UvLerngruppe): string {
		if (lerngruppe.idKurs !== null) {
			const kurs = manager.kursGetByIdOrNull(lerngruppe.idKurs);
			return kurs === null ? `Kurs-ID: ${lerngruppe.idKurs}` : kursKurzbezeichnung(kurs);
		}
		if (lerngruppe.idKlasse !== null) {
			const klasse = manager.klasseGetByIdOrNull(lerngruppe.idKlasse);
			return klasse === null ? `Klasse-ID: ${lerngruppe.idKlasse}` : klasse.kuerzel + klasse.parallelitaet;
		}
		return `ID: ${lerngruppe.id}`;
	}

	function lerngruppeFach(lerngruppe: UvLerngruppe): string {
		const idFach = lerngruppe.idKurs === null ? lerngruppe.idFach : manager.kursGetByIdOrNull(lerngruppe.idKurs)?.idFach;
		if (idFach === null || idFach === undefined) {
			return '—';
		}
		const fach = manager.fachGetByIdOrNull(idFach);
		return fach === null ? '—' : manager.fachdatenGetByFach(fach).kuerzel;
	}

	function jahrgangKuerzel(idJahrgang: number): string {
		for (const jahrgang of manager.jahrgangsdatenGetMenge()) {
			if (jahrgang.id === idJahrgang) {
				return jahrgang.kuerzel ?? `ID: ${idJahrgang}`;
			}
		}
		return `ID: ${idJahrgang}`;
	}

	function schuelergruppeJahrgangsbezeichnung(schuelergruppe: UvSchuelergruppe): string {
		return [...schuelergruppe.idsJahrgaengeErlaubt].map(jahrgangKuerzel).join(', ');
	}

	function klasseKuerzel(idKlasse: number | null): string {
		if (idKlasse === null || idKlasse < 0) {
			return '-';
		}
		const klasse = manager.klasseGetByIdOrNull(idKlasse);
		return klasse === null ? `ID: ${idKlasse}` : klasse.kuerzel + klasse.parallelitaet;
	}

	function lerngruppeBezeichnungByLerngruppenSchiene(schiene: UvLerngruppenSchiene): string {
		const lerngruppe = manager.lerngruppeGetByLerngruppenSchiene(schiene);
		if (lerngruppe.idKurs !== null) {
			const kurs = manager.kursGetByIdOrNull(lerngruppe.idKurs);
			if (kurs !== null) {
				return `${schuelergruppeJahrgangsbezeichnung(manager.schuelergruppeGetByKurs(kurs))} ${kursKurzbezeichnung(kurs)}`;
			}
		}
		if (lerngruppe.idKlasse !== null) {
			const klasse = manager.klasseGetByIdOrNull(lerngruppe.idKlasse);
			if (klasse !== null) {
				return klasse.bezeichnung ?? klasse.kuerzel + klasse.parallelitaet;
			}
		}
		return `Lerngruppe ID${lerngruppe.id}`;
	}

	return { anrechnungsgrundText, fachText, jahrgangKuerzel, klasseKuerzel, kursBezeichnung, kursKurzbezeichnung, lerngruppeBezeichnungByLerngruppenSchiene, schuljahresabschnittText, schuelergruppeJahrgangsbezeichnung, lerngruppeBezeichnung, lerngruppeFach, lerngruppeTyp };
}
