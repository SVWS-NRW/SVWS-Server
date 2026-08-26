import type { GostKlausurplanungState } from "@ui";
import type { GostKlausurtermin, GostKursklausur, GostSchuelerklausur, GostSchuelerklausurtermin, SchuelerListeEintrag } from "@core";
import { DateUtils, Fach, GostHalbjahr } from "@core";

export type KlausurplanungKursBadge = {
	text: string;
	farbe: string | null;
};

function kursBadgeStyleByFarbe(farbe: string | null): string {
	return farbe === null ? "" : `color: var(--color-text-uistatic); background-color: ${farbe};`;
}

function schuelerName(schueler: SchuelerListeEintrag): string {
	return `${schueler.nachname}, ${schueler.vorname}`;
}

function datumGermanOrNN(datum: string | null): string {
	return datum === null ? "N.N." : DateUtils.gibDatumGermanFormat(datum);
}

function datumKurzGermanOrNN(datum: string | null): string {
	return datum === null ? "N.N." : DateUtils.gibDatumGermanFormat(datum).slice(0, 6);
}

function datumKurzJahrGermanOrNN(datum: string | null): string {
	return datum === null ? "N.N." : DateUtils.gibDatumGermanFormat(datum).replace(/\d{2}(\d{2})$/, "$1");
}

function terminQuartalText(termin: GostKlausurtermin): string {
	return termin.quartal > 0 ? `Q${termin.quartal}` : "Alle";
}

function terminQuartalLangText(termin: GostKlausurtermin): string {
	return termin.quartal > 0 ? `${termin.quartal}. Quartal` : "Alle Quartale";
}

export function useKlausurplanungPresenter(state: GostKlausurplanungState) {

	function fachFarbeByKuerzel(kuerzel: string | null): string {
		if (kuerzel === null) {
			return "rgb(220,220,220)";
		}
		return Fach.getBySchluesselOrDefault(kuerzel).getHMTLFarbeRGBA(state.jahrgangsdaten.abiturjahr - 1, 1);
	}

	function fachFarbeById(idFach: number): string {
		return fachFarbeByKuerzel(state.manager.getFaecherManager(state.jahrgangsdaten.abiturjahr).get(idFach)?.kuerzel ?? null);
	}

	function kursBadge(klausur: GostKursklausur): KlausurplanungKursBadge {
		return {
			text: state.manager.kursKurzbezeichnungByKursklausur(klausur),
			farbe: state.manager.fachHTMLFarbeRgbaByKursklausur(klausur),
		};
	}

	function kursBadgeStyle(klausur: GostKursklausur): string {
		return kursBadgeStyleByFarbe(kursBadge(klausur).farbe);
	}

	function kursBadgeBySchuelerklausurtermin(schuelerklausurtermin: GostSchuelerklausurtermin): KlausurplanungKursBadge {
		return kursBadge(state.manager.kursklausurBySchuelerklausurtermin(schuelerklausurtermin));
	}

	function schuelerNameBySchuelerklausurtermin(schuelerklausurtermin: GostSchuelerklausurtermin): string {
		const schueler = state.manager.schuelerGetBySchuelerklausurtermin(schuelerklausurtermin);
		return `${schueler.nachname}, ${schueler.vorname}`;
	}

	function schuelerklausurterminNachname(schuelerklausurtermin: GostSchuelerklausurtermin): string {
		return state.manager.schuelerGetBySchuelerklausurtermin(schuelerklausurtermin).nachname;
	}

	function schuelerklausurterminVorname(schuelerklausurtermin: GostSchuelerklausurtermin): string {
		return state.manager.schuelerGetBySchuelerklausurtermin(schuelerklausurtermin).vorname;
	}

	function schuelerklausurterminJahrgangText(schuelerklausurtermin: GostSchuelerklausurtermin): string {
		return GostHalbjahr.fromIDorException(state.manager.vorgabeBySchuelerklausurtermin(schuelerklausurtermin).halbjahr).jahrgang;
	}

	function schuelerklausurterminLehrerKuerzel(schuelerklausurtermin: GostSchuelerklausurtermin): string {
		return state.manager.kursLehrerKuerzelByKursklausur(state.manager.kursklausurBySchuelerklausurtermin(schuelerklausurtermin)) ?? "-";
	}

	function schuelerklausurterminDatumText(schuelerklausurtermin: GostSchuelerklausurtermin): string {
		const termin = state.manager.terminOrNullBySchuelerklausurtermin(schuelerklausurtermin);
		return datumGermanOrNN(termin?.datum ?? null);
	}

	function schuelerklausurterminRaumText(schuelerklausurtermin: GostSchuelerklausurtermin): string {
		return state.manager.stundenplanraumGetBySchuelerklausurtermin(schuelerklausurtermin)?.kuerzel ?? "-";
	}

	function schuelerklausurterminDauerText(schuelerklausurtermin: GostSchuelerklausurtermin): string {
		return `${state.manager.vorgabeBySchuelerklausurtermin(schuelerklausurtermin).dauer}`;
	}

	function schuelerklausurterminVorgaengerDatumText(schuelerklausurtermin: GostSchuelerklausurtermin): string {
		return datumGermanOrNN(state.manager.datumSchuelerklausurVorgaenger(schuelerklausurtermin));
	}

	function schuelerklausurterminVorgaengerDatumKurzJahrText(schuelerklausurtermin: GostSchuelerklausurtermin): string {
		return datumKurzJahrGermanOrNN(state.manager.datumSchuelerklausurVorgaenger(schuelerklausurtermin));
	}

	function schuelerklausurterminVorgaengerBemerkung(schuelerklausurtermin: GostSchuelerklausurtermin): string | null {
		const bemerkung = state.manager.schuelerklausurterminVorgaengerBySchuelerklausurtermin(schuelerklausurtermin)?.bemerkung;
		return ((bemerkung === undefined) || (bemerkung === null) || (bemerkung.trim().length === 0)) ? null : bemerkung;
	}

	function kursSchieneText(klausur: GostKursklausur): string {
		const schienen = state.manager.kursSchieneByKursklausur(klausur);
		return schienen.isEmpty() ? "-" : [...schienen].join(", ");
	}

	function kursLehrerNameText(klausur: GostKursklausur): string {
		const lehrer = state.manager.kursLehrerByKursklausur(klausur);
		return lehrer === null ? "-" : `${lehrer.nachname}, ${lehrer.vorname}`;
	}

	function kursklausurVorterminDatumText(klausur: GostKursklausur): string {
		const vorklausur = state.manager.kursklausurVorterminByKursklausur(klausur);
		if (vorklausur === null) {
			return "-";
		}
		const termin = state.manager.terminOrNullByKursklausur(vorklausur);
		return ((termin?.datum === undefined) || (termin.datum === null)) ? "-" : DateUtils.gibDatumGermanFormat(termin.datum);
	}

	function schuelerNameById(idSchueler: number): string {
		const schueler = state.manager.schuelerGetByIdOrException(idSchueler);
		return `${schueler.nachname}, ${schueler.vorname}`;
	}

	function schuelerNameBySchuelerklausur(schuelerklausur: GostSchuelerklausur): string {
		const schueler = state.manager.schuelerGetBySchuelerklausur(schuelerklausur);
		return `${schueler.nachname}, ${schueler.vorname}`;
	}

	function startzeitBySchuelerklausurtermin(schuelerklausurtermin: GostSchuelerklausurtermin): string {
		const termin = state.manager.terminOrExceptionBySchuelerklausurtermin(schuelerklausurtermin);
		return DateUtils.getStringOfUhrzeitFromMinuten(schuelerklausurtermin.startzeit ?? termin.startzeit!);
	}

	function terminTitel(termin: GostKlausurtermin, emptyTitle = "Klausurtermin"): string {
		if ((termin.bezeichnung !== null) && (termin.bezeichnung.length > 0)) {
			return termin.bezeichnung;
		}
		if (!termin.istHaupttermin) {
			return "Nachschreibtermin";
		}
		const klausuren = state.manager.kursklausurGetMengeByTermin(termin);
		if (klausuren.size() > 0) {
			return [...klausuren].map(klausur => state.manager.kursKurzbezeichnungByKursklausur(klausur)).join(", ");
		}
		return emptyTitle;
	}

	function terminDatumText(termin: GostKlausurtermin): string {
		return datumGermanOrNN(termin.datum);
	}

	function terminDatumKurzText(termin: GostKlausurtermin): string {
		return datumKurzGermanOrNN(termin.datum);
	}

	function terminDauerText(termin: GostKlausurtermin, inklusiveNachschreiber = true): string {
		const min = state.manager.minKlausurdauerGetByTermin(termin, inklusiveNachschreiber);
		const max = state.manager.maxKlausurdauerGetByTermin(termin, inklusiveNachschreiber);
		return min < max ? `${min} - ${max} Min.` : `${max} Min.`;
	}

	function terminTitelShort(termin: GostKlausurtermin, maxKurse = 3): string {
		const klausuren = [...state.manager.kursklausurGetMengeByTermin(termin)].map(klausur => state.manager.kursKurzbezeichnungByKursklausur(klausur));
		return klausuren.length > maxKurse ? klausuren.slice(0, maxKurse).join(", ") + "..." : klausuren.join(", ");
	}

	function terminKursBadges(termin: GostKlausurtermin): KlausurplanungKursBadge[] {
		const wrap = (text: string): KlausurplanungKursBadge => ({ text, farbe: null });
		if ((termin.bezeichnung !== null) && (termin.bezeichnung.length > 0)) {
			return [wrap(termin.bezeichnung)];
		}
		if (!termin.istHaupttermin) {
			return [wrap("Nachschreibtermin")];
		}
		const klausuren = state.manager.kursklausurGetMengeByTermin(termin);
		if (klausuren.size() > 0) {
			return [...klausuren].map(kursBadge);
		}
		return [wrap("Leerer Klausurtermin")];
	}

	function compareSchuelerklausurterminNachname(a: GostSchuelerklausurtermin, b: GostSchuelerklausurtermin): number {
		return schuelerklausurterminNachname(a).localeCompare(schuelerklausurterminNachname(b), "de-DE");
	}

	function compareSchuelerklausurterminVorname(a: GostSchuelerklausurtermin, b: GostSchuelerklausurtermin): number {
		return schuelerklausurterminVorname(a).localeCompare(schuelerklausurterminVorname(b), "de-DE");
	}

	function compareSchuelerklausurterminKurs(a: GostSchuelerklausurtermin, b: GostSchuelerklausurtermin): number {
		const kursA = state.manager.kursklausurBySchuelerklausurtermin(a);
		const kursB = state.manager.kursklausurBySchuelerklausurtermin(b);
		return state.manager.kursKurzbezeichnungByKursklausur(kursA).localeCompare(state.manager.kursKurzbezeichnungByKursklausur(kursB), "de-DE");
	}

	function compareSchuelerklausurterminDatum(a: GostSchuelerklausurtermin, b: GostSchuelerklausurtermin): number {
		const terminA = state.manager.terminGetByIdOrException(a.idTermin!);
		const terminB = state.manager.terminGetByIdOrException(b.idTermin!);
		const result = terminA.datum!.localeCompare(terminB.datum!, "de-DE");
		return result === 0 ? compareSchuelerklausurterminKurs(a, b) : result;
	}

	return {
		compareSchuelerklausurterminDatum,
		compareSchuelerklausurterminKurs,
		compareSchuelerklausurterminNachname,
		compareSchuelerklausurterminVorname,
		datumGermanOrNN,
		datumKurzGermanOrNN,
		datumKurzJahrGermanOrNN,
		fachFarbeById,
		fachFarbeByKuerzel,
		kursBadge,
		kursBadgeBySchuelerklausurtermin,
		kursBadgeStyle,
		kursBadgeStyleByFarbe,
		kursLehrerNameText,
		kursSchieneText,
		kursklausurVorterminDatumText,
		schuelerName,
		schuelerNameById,
		schuelerNameBySchuelerklausur,
		schuelerNameBySchuelerklausurtermin,
		schuelerklausurterminDatumText,
		schuelerklausurterminDauerText,
		schuelerklausurterminJahrgangText,
		schuelerklausurterminLehrerKuerzel,
		schuelerklausurterminNachname,
		schuelerklausurterminRaumText,
		schuelerklausurterminVorname,
		schuelerklausurterminVorgaengerBemerkung,
		schuelerklausurterminVorgaengerDatumKurzJahrText,
		schuelerklausurterminVorgaengerDatumText,
		startzeitBySchuelerklausurtermin,
		terminDatumKurzText,
		terminDatumText,
		terminDauerText,
		terminKursBadges,
		terminQuartalLangText,
		terminQuartalText,
		terminTitel,
		terminTitelShort,
	};
}
