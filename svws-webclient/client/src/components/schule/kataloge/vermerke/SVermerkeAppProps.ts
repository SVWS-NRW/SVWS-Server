import type { VermerkartenManager } from "@core";
import type { TabData } from "@ui";

export interface VermerkeAppProps {
	vermerkartenManager : () => VermerkartenManager;
	setTab: (value: TabData) => Promise<void>;
	tab: TabData;
	tabs: TabData[];
	tabsHidden: boolean[];
}
