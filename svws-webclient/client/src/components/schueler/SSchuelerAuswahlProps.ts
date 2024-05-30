import type { SchuelerListeEintrag, SchuelerListeManager, ServerMode } from "@core";
import type { AbschnittAuswahlDaten } from "@comp";

export interface SchuelerAuswahlProps {
	serverMode: ServerMode;
	schuelerListeManager: () => SchuelerListeManager;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	gotoSchueler: (value: SchuelerListeEintrag | null) => Promise<void>;
	setFilter: () => Promise<void>;
}

