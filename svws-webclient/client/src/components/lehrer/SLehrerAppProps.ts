import type { LehrerListeManager } from "@core";
import type { AuswahlChildData } from "../AuswahlChildData";

export interface LehrerAppProps {
	lehrerListeManager: () => LehrerListeManager;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}