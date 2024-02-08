import type { JahrgangsListeEintrag, Schuljahresabschnitt } from "@core";

export interface JahrgaengeAuswahlProps {
	auswahl: () => JahrgangsListeEintrag | undefined;
	mapKatalogeintraege: () => Map<number, JahrgangsListeEintrag>;
	gotoEintrag: (eintrag: JahrgangsListeEintrag) => Promise<void>;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	returnToKataloge: () => Promise<void>;
}