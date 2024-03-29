import type { Schuljahresabschnitt, ServerMode, StundenplanListeEintrag } from "@core";

export interface StundenplanAuswahlProps {
	serverMode: ServerMode;
	auswahl: StundenplanListeEintrag | undefined;
	mapKatalogeintraege: () => Map<number, StundenplanListeEintrag>;
	gotoEintrag: (religion: StundenplanListeEintrag) => Promise<void>;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	removeEintraege: (eintraege: StundenplanListeEintrag[]) => Promise<void>;
	addEintrag: () => Promise<void>;
}