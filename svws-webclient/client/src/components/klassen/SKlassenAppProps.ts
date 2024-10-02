import type { KlassenListeManager } from "@core";
import type { TabData } from "@ui";

export interface KlassenAppProps {
	klassenListeManager: () => KlassenListeManager;
	setSelectedTab: (value: TabData) => Promise<void>;
	selectedTab: TabData;
	tabs: () => TabData[];
	tabsHidden: boolean[];
	gruppenprozesseEnabled: boolean;
	creationModeEnabled: boolean;
}
