import type { LehrerListeManager } from "@core";
import type { TabManager } from "@ui";

export interface LehrerAppProps {
	lehrerListeManager: () => LehrerListeManager;
	tabManager: () => TabManager;
}