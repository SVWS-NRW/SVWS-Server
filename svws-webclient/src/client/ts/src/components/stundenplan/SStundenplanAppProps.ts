import type { Stundenplan } from "@svws-nrw/svws-core";
import type { AuswahlChildData } from "../AuswahlChildData";

export interface StundenplanAppProps {
	data: () => Stundenplan;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}
