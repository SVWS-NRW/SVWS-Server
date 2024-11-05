import type { SchuelerListeManager, SchuelerStammdaten } from "@core";
import type { TabManager, ViewType } from "@ui";

export interface SchuelerAppProps {
	patch: (data: Partial<SchuelerStammdaten>) => Promise<void>;
	schuelerListeManager: () => SchuelerListeManager;
	tabManager: () => TabManager;
	activeViewType: ViewType;
}
