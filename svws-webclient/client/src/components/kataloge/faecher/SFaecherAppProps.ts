import type { FachDaten  } from "@core";
import type { AuswahlChildData } from "~/components/AuswahlChildData";

export interface FaecherAppProps {
	auswahl: () => FachDaten | undefined;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}