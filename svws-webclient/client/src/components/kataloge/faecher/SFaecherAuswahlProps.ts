import type { FaecherListeEintrag, Schulform, Schuljahresabschnitt } from "@core";

export interface FaecherAuswahlProps {
	schulform: Schulform;
	auswahl: FaecherListeEintrag | undefined;
	mapKatalogeintraege: () => Map<number, FaecherListeEintrag>;
	gotoEintrag: (eintrag: FaecherListeEintrag) => Promise<void>;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	returnToKataloge: () => Promise<void>;
	setzeDefaultSortierungSekII: () => Promise<void>;
}