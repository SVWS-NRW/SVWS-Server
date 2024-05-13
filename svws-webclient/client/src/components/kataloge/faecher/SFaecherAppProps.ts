import type { FachListeManager  } from "@core";
import type { AuswahlChildData } from "~/components/AuswahlChildData";

export interface FaecherAppProps {
	fachListeManager: () => FachListeManager;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}