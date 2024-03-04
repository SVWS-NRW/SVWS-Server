import type { KlassenDaten, Schuljahresabschnitt, KlassenListeManager } from "@core";

export interface KlassenAuswahlProps {
	klassenListeManager: () => KlassenListeManager;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	gotoEintrag: (eintrag: KlassenDaten) => Promise<void>;
	setFilter: () => Promise<void>;
	setzeDefaultSortierung: () => Promise<void>;
}
