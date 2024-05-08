import type { KlassenDaten, Schuljahresabschnitt, KlassenListeManager, ServerMode } from "@core";

export interface KlassenAuswahlProps {
	serverMode: ServerMode;
	klassenListeManager: () => KlassenListeManager;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	gotoEintrag: (eintrag: KlassenDaten) => Promise<void>;
	setGruppenprozess: (value : boolean) => Promise<void>;
	setFilter: () => Promise<void>;
	setzeDefaultSortierung: () => Promise<void>;
}
