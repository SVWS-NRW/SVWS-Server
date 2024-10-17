import type { KlassenListeManager } from "@core";
import type { TabManager, ViewType } from "@ui";

export interface KlassenAppProps {
	klassenListeManager: () => KlassenListeManager;
	tabManager: () => TabManager;
	activeRouteType: ViewType;
}
