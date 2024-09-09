import type{ AuswahlChildData } from "~/components/AuswahlChildData";

export interface SchuleDatenaustauschUntisProps {
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}
