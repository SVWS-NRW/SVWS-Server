import type { SchulEintrag, Schuljahresabschnitt } from "@core";

export interface SchulenAuswahlProps {
	auswahl: SchulEintrag | undefined;
	mapKatalogeintraege: () => Map<number, SchulEintrag>;
	removeEintraege: (list: Iterable<SchulEintrag>) => Promise<void>;
	addEintrag: (data: Partial<SchulEintrag>) => Promise<void>;
	gotoEintrag: (eintrag: SchulEintrag) => Promise<void>;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	returnToKataloge: () => Promise<void>;
}