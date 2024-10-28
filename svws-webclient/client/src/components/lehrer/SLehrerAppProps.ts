import type { LehrerListeManager } from "@core";
import type { TabManager, ViewType } from "@ui";

export interface LehrerAppProps {
	lehrerListeManager: () => LehrerListeManager;
	tabManager: () => TabManager;
	activeViewType: ViewType;
}