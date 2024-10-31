import type { FachListeManager, ServerMode } from "@core";
import type { AbschnittAuswahlDaten } from "@comp";
import type { ViewType } from "@ui";

export interface FaecherAuswahlProps {
	mode: ServerMode;
	fachListeManager: () => FachListeManager;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	setFilter: () => Promise<void>;
	setzeDefaultSortierungSekII: () => Promise<void>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
	gotoGruppenprozessView: (navigate: boolean) => Promise<void>;
	gotoHinzufuegenView: (navigate: boolean) => Promise<void>;
	activeViewType: ViewType;
}