import type { FachListeManager } from "@core";
import type { TabManager, ViewType } from "@ui";

export interface FaecherAppProps {
	fachListeManager: () => FachListeManager;
	tabManager: () => TabManager;
	activeViewType: ViewType;
}