import type { Raum, Schuljahresabschnitt } from "@svws-nrw/svws-core";

export interface RaeumeAuswahlProps {
	auswahl: Raum | undefined;
	mapKatalogeintraege: Map<number, Raum>;
	addEintrag: (religion: Raum) => Promise<void>;
	gotoEintrag: (eintrag: Raum) => Promise<void>;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	returnToKataloge: () => Promise<void>;
}