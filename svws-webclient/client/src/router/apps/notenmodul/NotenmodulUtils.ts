import type { ENMAbteilung } from "../../../../../core/src/core/data/enm/ENMAbteilung";
import type { ENMJahrgang } from "../../../../../core/src/core/data/enm/ENMJahrgang";
import type { ENMKlasse } from "../../../../../core/src/core/data/enm/ENMKlasse";
import type { Comparator } from "../../../../../core/src/java/util/Comparator";

/** Ein Java-Comparator für den Vergleich zweier ENM-Klassen-Objekte zur Sortierung in Listen und anderen Datenstrukturen */
export const comparatorENMKlasse = <Comparator<ENMKlasse>>{ compare: (a: ENMKlasse, b: ENMKlasse): number => {
	// Vergleiche zuerst anhand der gesetzten Sortierung der Klasse...
	const tmp = a.sortierung - b.sortierung;
	if (tmp !== 0) {
		return tmp;
	}
	// ... und ansonsten anhand des Anzeige-Kürzels der Klassen
	if ((a.kuerzelAnzeige !== null) && (b.kuerzelAnzeige !== null)) {
		return a.kuerzelAnzeige.localeCompare(b.kuerzelAnzeige);
	}
	if (a.kuerzelAnzeige === null) {
		return -1;
	}
	if (b.kuerzelAnzeige === null) {
		return 1;
	}
	return 0;
} };

/** Ein Java-Comparator für den Vergleich zweier ENM-Abteilungs-Objekte zur Sortierung in Listen und anderen Datenstrukturen */
export const comparatorENMAbteilung = <Comparator<ENMAbteilung>>{ compare: (a: ENMAbteilung, b: ENMAbteilung): number => {
	// Vergleiche zuerst anhand der gesetzten Sortierung der Abteilung...
	const tmp = a.sortierung - b.sortierung;
	if (tmp !== 0) {
		return tmp;
	}
	return a.bezeichnung.localeCompare(b.bezeichnung);
} };

/** Ein Java-Comparator für den Vergleich zweier ENM-Jahrgangs-Objekte zur Sortierung in Listen und anderen Datenstrukturen */
export const comparatorENMJahrgang = <Comparator<ENMJahrgang>>{ compare: (a: ENMJahrgang, b: ENMJahrgang): number => {
	// Vergleiche zuerst anhand der gesetzten Sortierung der Jahrgänge...
	const tmp = a.sortierung - b.sortierung;
	if (tmp !== 0) {
		return tmp;
	}
	if ((a.kuerzelAnzeige !== null) && (b.kuerzelAnzeige !== null)) {
		return a.kuerzelAnzeige.localeCompare(b.kuerzelAnzeige);
	}
	if (a.kuerzelAnzeige === null) {
		return -1;
	}
	if (b.kuerzelAnzeige === null) {
		return 1;
	}
	return 0;
} };
