import type { SchuelerListeManager } from "@core";
import type { TabManager } from "@ui";

export interface SchuelerAppProps {
	schuelerListeManager: () => SchuelerListeManager;
	tabManager: () => TabManager;
}
