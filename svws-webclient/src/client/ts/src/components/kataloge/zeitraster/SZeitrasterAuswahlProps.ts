import type { StundenplanZeitraster, Schuljahresabschnitt } from "@core";

export interface ZeitrasterAuswahlProps {
	auswahl: StundenplanZeitraster | undefined;
	mapKatalogeintraege: () => Map<number, StundenplanZeitraster>;
	addEintrag: (religion: StundenplanZeitraster) => Promise<void>;
	deleteEintraege: (eintraege: StundenplanZeitraster[]) => Promise<void>;
	gotoEintrag: (eintrag: StundenplanZeitraster) => Promise<void>;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	returnToKataloge: () => Promise<void>;
}