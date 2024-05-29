import type { KlassenListeManager } from "@core";
import type { AuswahlChildData } from "../AuswahlChildData";

export interface KlassenAppProps {
	klassenListeManager: () => KlassenListeManager;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsGruppenprozesse: AuswahlChildData[];
	tabsHidden: boolean[];
	gruppenprozesseEnabled: boolean;
}
