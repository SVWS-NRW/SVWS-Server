import type { FachDaten, Schulform, Schuljahresabschnitt } from "@core";

export interface FaecherAuswahlProps {
	schulform: Schulform;
	auswahl: FachDaten | undefined;
	mapKatalogeintraege: () => Map<number, FachDaten>;
	gotoEintrag: (eintrag: FachDaten) => Promise<void>;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	returnToKataloge: () => Promise<void>;
	setzeDefaultSortierungSekII: () => Promise<void>;
}