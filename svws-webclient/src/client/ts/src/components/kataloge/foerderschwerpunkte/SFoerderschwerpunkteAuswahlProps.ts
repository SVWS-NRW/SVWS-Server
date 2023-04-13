import type { FoerderschwerpunktEintrag, Schuljahresabschnitt } from "@svws-nrw/svws-core";

export interface FoerderschwerpunkteAuswahlProps {
	auswahl: FoerderschwerpunktEintrag | undefined;
	mapKatalogeintraege: Map<number, FoerderschwerpunktEintrag>;
	gotoEintrag: (eintrag: FoerderschwerpunktEintrag) => Promise<void>;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	returnToKataloge: () => Promise<void>;
}