import type { TabManager, ViewType, FachListeManager } from "@ui";

export interface FaecherAppProps {
	manager: () => FachListeManager;
	tabManager: () => TabManager;
	activeViewType: ViewType;
}
