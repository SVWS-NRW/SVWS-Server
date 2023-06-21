import type { AuswahlChildData } from "~/components/AuswahlChildData";

export interface ZeitrasterAppProps {
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}