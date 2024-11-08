import type { TabManager, ViewType } from "@ui";
import type { JahrgangListeManager } from "@core";

export interface JahrgaengeAppProps {
	jahrgangListeManager: () => JahrgangListeManager;
	tabManager: () => TabManager;
	activeViewType: ViewType;
}
