import type { SchuelerListeManager } from "@core";
import type { TabData } from "@ui";

export interface SchuelerAppProps {
	schuelerListeManager: () => SchuelerListeManager;
	setTab: (value: TabData) => Promise<void>;
	tab: TabData;
	tabs: TabData[];
	tabsHidden: boolean[];
}
