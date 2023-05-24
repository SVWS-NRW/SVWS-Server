import type { Schuljahresabschnitt, StundenplanListeEintrag } from "@svws-nrw/svws-core";

export interface StundenplanAuswahlProps {
	auswahl: StundenplanListeEintrag | undefined;
	mapKatalogeintraege: Map<number, StundenplanListeEintrag>;
	gotoEintrag: (religion: StundenplanListeEintrag) => Promise<void>;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
}