import type { SchuelerListeManager } from "@core";
import type { TabManager, ViewType } from "@ui";

export interface SchuelerAppProps {
	schuelerListeManager: () => SchuelerListeManager;
	tabManager: () => TabManager;
	activeRouteType: ViewType;
}
