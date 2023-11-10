import type { SchuelerListeManager } from "@core";
import type { AuswahlChildData } from "../AuswahlChildData";

export interface SchuelerAppProps {
	schuelerListeManager: () => SchuelerListeManager;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}
