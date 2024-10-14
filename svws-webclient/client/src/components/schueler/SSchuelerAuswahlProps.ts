import type { SchuelerListeManager, ServerMode } from "@core";
import type { AbschnittAuswahlDaten } from "@comp";
import type { RouteType } from "~/router/RouteType";

export interface SchuelerAuswahlProps {
	serverMode: ServerMode;
	schuelerListeManager: () => SchuelerListeManager;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	gotoDefaultRoute: (idSchueler?: number | null) => Promise<void>;
	gotoHinzufuegenRoute: (navigate: boolean) => Promise<void>;
	gotoGruppenprozessRoute: (navigate: boolean) => Promise<void>;
	setFilter: () => Promise<void>;
	activeRouteType: RouteType;
}

