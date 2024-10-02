import type { SchulEintrag } from "@core";
import type { TabData } from "@ui";

export interface SchulenAppProps {
	auswahl: SchulEintrag | undefined;
	setTab: (value: TabData) => Promise<void>;
	tab: TabData;
	tabs: TabData[];
	tabsHidden: boolean[];
}