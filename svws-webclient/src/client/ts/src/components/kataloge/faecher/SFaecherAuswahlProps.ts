import type { FaecherListeEintrag, Schuljahresabschnitt } from "@svws-nrw/svws-core";

export interface FaecherAuswahlProps {
	auswahl: FaecherListeEintrag | undefined;
	mapKatalogeintraege: Map<number, FaecherListeEintrag>;
	gotoEintrag: (eintrag: FaecherListeEintrag) => Promise<void>;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	returnToKataloge: () => Promise<void>;
}