import type { TabData } from "@ui";

export interface SchuleDatenaustauschKurs42Props {
	setTab: (value: TabData) => Promise<void>;
	tab: TabData;
	tabs: TabData[];
	tabsHidden: boolean[];
}