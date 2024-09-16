import type { VermerkartenManager } from "@core";
import type { AuswahlChildData } from "~/components/AuswahlChildData";

export interface VermerkeAppProps {
	vermerkartenManager : () => VermerkartenManager;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}
