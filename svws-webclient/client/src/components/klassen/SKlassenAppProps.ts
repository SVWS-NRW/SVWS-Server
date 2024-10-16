import type { KlassenListeManager } from "@core";
import type { TabManager } from "@ui";
import type { RouteType } from "~/router/RouteType";

export interface KlassenAppProps {
	klassenListeManager: () => KlassenListeManager;
	tabManager: () => TabManager;
	activeRouteType: RouteType;
}
