import type { AuswahlChildData } from "../AuswahlChildData";

export interface StundenplanAppProps {
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}
