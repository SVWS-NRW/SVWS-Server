import type { KlassenDaten, KlassenListeManager, ServerMode } from "@core";
import type { AbschnittAuswahlDaten } from "@comp";

export interface KlassenAuswahlProps {
	serverMode: ServerMode;
	klassenListeManager: () => KlassenListeManager;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	gotoEintrag: (eintrag: KlassenDaten) => Promise<void>;
	setGruppenprozess: (value : boolean) => Promise<void>;
	setFilter: () => Promise<void>;
	setzeDefaultSortierung: () => Promise<void>;
}
