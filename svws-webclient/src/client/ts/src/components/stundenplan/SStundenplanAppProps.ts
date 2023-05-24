import type { Stundenplan } from "@svws-nrw/svws-core";
import type { AuswahlChildData } from "../AuswahlChildData";

export interface StundenplanAppProps {
	daten: Stundenplan;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}
