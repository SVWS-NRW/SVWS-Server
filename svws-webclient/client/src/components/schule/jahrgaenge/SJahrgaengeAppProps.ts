import type { JahrgangsDaten } from "@core";
import type { TabData } from "@ui";

export interface JahrgaengeAppProps {
	auswahl: () => JahrgangsDaten | undefined;
	setTab: (value: TabData) => Promise<void>;
	tab: TabData;
	tabs: TabData[];
	tabsHidden: boolean[];
}