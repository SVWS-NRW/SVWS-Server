import type { JahrgangsDaten, Schuljahresabschnitt } from "@core";

export interface JahrgaengeAuswahlProps {
	auswahl: () => JahrgangsDaten | undefined;
	mapKatalogeintraege: () => Map<number, JahrgangsDaten>;
	gotoEintrag: (eintrag: JahrgangsDaten) => Promise<void>;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	returnToKataloge: () => Promise<void>;
}