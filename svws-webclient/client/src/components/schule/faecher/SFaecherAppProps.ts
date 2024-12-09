import type { FachListeManager } from "@core";
import type { TabManager, ViewType } from "@ui";

export interface FaecherAppProps {
	manager: () => FachListeManager;
	tabManager: () => TabManager;
	activeViewType: ViewType;
}
