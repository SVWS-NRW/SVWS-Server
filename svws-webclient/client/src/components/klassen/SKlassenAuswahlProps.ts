import type { KlassenListeManager, ServerMode } from "@core";
import type { AbschnittAuswahlDaten } from "@comp";

export interface KlassenAuswahlProps {
	serverMode: ServerMode;
	klassenListeManager: () => KlassenListeManager;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	gotoEintrag: (eintragId?: number | null) => Promise<void>;
	gotoGruppenprozess: () => Promise<void>;
	setFilter: () => Promise<void>;
	setzeDefaultSortierung: () => Promise<void>;
}
