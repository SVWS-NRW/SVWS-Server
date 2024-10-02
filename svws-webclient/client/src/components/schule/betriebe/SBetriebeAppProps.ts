import type { TabData } from "@ui";
import type { BetriebListeEintrag } from "@core";

export interface BetriebeAppProps {
	auswahl: BetriebListeEintrag | undefined;
	setTab: (value: TabData) => Promise<void>;
	tab: TabData;
	tabs: TabData[];
	tabsHidden: boolean[];
}