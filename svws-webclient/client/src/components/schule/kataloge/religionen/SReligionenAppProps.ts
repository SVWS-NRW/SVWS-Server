import type { ReligionListeManager } from "@core";
import type { AuswahlChildData } from "~/components/AuswahlChildData";

export interface ReligionenAppProps {
	religionListeManager: () => ReligionListeManager;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}