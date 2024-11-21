import type { KursListeManager } from "@core";
import type { TabManager, ViewType } from "@ui";

export interface KurseAppProps {
	manager: () => KursListeManager;
	tabManager: () => TabManager;
	activeViewType: ViewType;
}