import type { SchuelerListeEintrag, SchuelerStammdaten, SchuelerListeManager } from "@core";
import type { AuswahlChildData } from "../AuswahlChildData";

export interface SchuelerAppProps {
	auswahl: SchuelerListeEintrag | null;
	stammdaten: () => SchuelerStammdaten | null;
	schuelerListeManager: () => SchuelerListeManager;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}
