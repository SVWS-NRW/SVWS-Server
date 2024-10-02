import type { LehrerListeManager } from "@core";
import type { TabData } from "@ui";

export interface LehrerAppProps {
	lehrerListeManager: () => LehrerListeManager;
	setTab: (value: TabData) => Promise<void>;
	tab: TabData;
	tabs: TabData[];
	tabsHidden: boolean[];
}