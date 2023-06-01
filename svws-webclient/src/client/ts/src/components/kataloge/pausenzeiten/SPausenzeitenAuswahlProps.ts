import type { StundenplanPausenzeit, Schuljahresabschnitt } from "@svws-nrw/svws-core";

export interface PausenzeitenAuswahlProps {
	auswahl: StundenplanPausenzeit | undefined;
	mapKatalogeintraege: () => Map<number, StundenplanPausenzeit>;
	addEintrag: (religion: StundenplanPausenzeit) => Promise<void>;
	deleteEintraege: (eintraege: StundenplanPausenzeit[]) => Promise<void>;
	gotoEintrag: (eintrag: StundenplanPausenzeit) => Promise<void>;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	returnToKataloge: () => Promise<void>;
}