import type { SchuelerListeManager } from "@core";
import type { TabManager } from "@ui";
import type { RouteType } from "~/router/RouteType";

export interface SchuelerAppProps {
	schuelerListeManager: () => SchuelerListeManager;
	tabManager: () => TabManager;
	activeRouteType: RouteType;
}
