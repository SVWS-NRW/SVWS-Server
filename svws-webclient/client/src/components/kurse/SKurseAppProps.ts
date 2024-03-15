import type { KursListeManager } from "@core";
import type { AuswahlChildData } from "../AuswahlChildData";

export interface KurseAppProps {
	kursListeManager: () => KursListeManager;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}