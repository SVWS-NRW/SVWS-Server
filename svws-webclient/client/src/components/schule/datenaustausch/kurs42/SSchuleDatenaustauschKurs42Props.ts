import type { AuswahlChildData } from "~/components/AuswahlChildData";

export interface SchuleDatenaustauschKurs42Props {
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}