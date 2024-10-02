import type { FachListeManager } from "@core";
import type { TabData } from "@ui";

export interface FaecherAppProps {
	fachListeManager: () => FachListeManager;
	setTab: (value: TabData) => Promise<void>;
	tab: TabData;
	tabs: TabData[];
	tabsHidden: boolean[];
}