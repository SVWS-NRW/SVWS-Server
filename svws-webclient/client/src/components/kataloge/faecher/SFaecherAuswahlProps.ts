import type { FachDaten, FachListeManager, Schuljahresabschnitt } from "@core";

export interface FaecherAuswahlProps {
	fachListeManager: () => FachListeManager;
	gotoEintrag: (eintrag: FachDaten) => Promise<void>;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	returnToKataloge: () => Promise<void>;
	setFilter: () => Promise<void>;
	setzeDefaultSortierungSekII: () => Promise<void>;
}