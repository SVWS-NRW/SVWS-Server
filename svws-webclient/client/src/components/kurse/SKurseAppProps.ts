import type { KursListeManager } from "@core";
import type { TabData } from "@ui";

export interface KurseAppProps {
	kursListeManager: () => KursListeManager;
	setTab: (value: TabData) => Promise<void>;
	tab: TabData;
	tabs: TabData[];
	tabsHidden: boolean[];
}