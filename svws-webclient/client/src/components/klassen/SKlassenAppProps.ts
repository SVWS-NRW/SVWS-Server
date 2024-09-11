import type { KlassenListeManager } from "@core";
import type { AuswahlChildData } from "../AuswahlChildData";

export interface KlassenAppProps {
	klassenListeManager: () => KlassenListeManager;
	setSelectedTab: (value: AuswahlChildData) => Promise<void>;
	selectedTab: AuswahlChildData;
	tabs: () => AuswahlChildData[];
	tabsHidden: boolean[];
	gruppenprozesseEnabled: boolean;
	creationModeEnabled: boolean;
}
