import type { RaumListeManager  } from "@core";
import type { AuswahlChildData } from "~/components/AuswahlChildData";

export interface RaeumeAppProps {
	raumListeManager: () => RaumListeManager;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}