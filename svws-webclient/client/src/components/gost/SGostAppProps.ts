import type { GostJahrgang } from "@core";
import type { TabData } from "@ui";
export interface GostAppProps {
	auswahl: GostJahrgang | undefined;
	setTab: (value: TabData) => Promise<void>;
	tab: TabData;
	tabs: TabData[];
	tabsHidden: boolean[];
}