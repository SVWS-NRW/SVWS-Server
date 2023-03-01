import { LehrerStammdaten } from "@svws-nrw/svws-core-ts";
import { AuswahlChildData } from "../AuswahlChildData";

export interface LehrerAppProps {
	stammdaten: LehrerStammdaten | undefined;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}