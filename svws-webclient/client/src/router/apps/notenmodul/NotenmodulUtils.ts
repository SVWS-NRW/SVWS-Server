import type { ENMv2Abteilung } from "@core/core/data/enm/v2/ENMv2Abteilung";
import type { ENMv2Jahrgang } from "@core/core/data/enm/v2/ENMv2Jahrgang";
import type { ENMv2Klasse } from "@core/core/data/enm/v2/ENMv2Klasse";
import type { Comparator } from "@core/java/util/Comparator";

/** Ein Java-Comparator für den Vergleich zweier ENM-Klassen-Objekte zur Sortierung in Listen und anderen Datenstrukturen */
export const comparatorENMKlasse = <Comparator<ENMv2Klasse>>{ compare: (a: ENMv2Klasse, b: ENMv2Klasse): number => {
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
export const comparatorENMAbteilung = <Comparator<ENMv2Abteilung>>{ compare: (a: ENMv2Abteilung, b: ENMv2Abteilung): number => {
	// Vergleiche zuerst anhand der gesetzten Sortierung der Abteilung...
	const tmp = a.sortierung - b.sortierung;
	if (tmp !== 0) {
		return tmp;
	}
	return a.bezeichnung.localeCompare(b.bezeichnung);
} };

/** Ein Java-Comparator für den Vergleich zweier ENM-Jahrgangs-Objekte zur Sortierung in Listen und anderen Datenstrukturen */
export const comparatorENMJahrgang = <Comparator<ENMv2Jahrgang>>{ compare: (a: ENMv2Jahrgang, b: ENMv2Jahrgang): number => {
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
