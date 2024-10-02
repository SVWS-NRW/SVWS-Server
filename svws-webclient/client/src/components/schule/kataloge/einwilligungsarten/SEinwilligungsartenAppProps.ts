import type { TabData } from "@ui";
import type { Einwilligungsart } from "@core";

export interface SEinwilligungsartenAppProps {
	auswahl: () => Einwilligungsart | undefined;
	setTab: (value: TabData) => Promise<void>;
	tab: TabData;
	tabs: TabData[];
	tabsHidden: boolean[];
}
