import type { TabData } from "@ui";

export interface StundenplanKatalogeAppProps {
	setTab: (value: TabData) => Promise<void>;
	tab: TabData;
	tabs: TabData[];
	tabsHidden: boolean[];
}