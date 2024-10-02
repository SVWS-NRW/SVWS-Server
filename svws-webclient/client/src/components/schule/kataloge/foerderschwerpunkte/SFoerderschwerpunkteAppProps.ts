import type { FoerderschwerpunktEintrag } from "@core";
import type { TabData } from "@ui";

export interface FoerderschwerpunkteAppProps {
	auswahl: FoerderschwerpunktEintrag | undefined;
	setTab: (value: TabData) => Promise<void>;
	tab: TabData;
	tabs: TabData[];
	tabsHidden: boolean[];
}