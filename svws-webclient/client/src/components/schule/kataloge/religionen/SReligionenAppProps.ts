import type { ReligionListeManager } from "@core";
import type { TabData } from "@ui";

export interface ReligionenAppProps {
	religionListeManager: () => ReligionListeManager;
	setTab: (value: TabData) => Promise<void>;
	tab: TabData;
	tabs: TabData[];
	tabsHidden: boolean[];
}