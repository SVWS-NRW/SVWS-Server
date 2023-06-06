import type { LehrerStammdaten } from "@core";
import type { AuswahlChildData } from "../AuswahlChildData";

export interface LehrerAppProps {
	stammdaten: LehrerStammdaten | undefined;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}