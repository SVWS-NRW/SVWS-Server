import type { StundenplanPausenzeit  } from "@core";
import type { AuswahlChildData } from "~/components/AuswahlChildData";

export interface PausenzeitenAppProps {
	auswahl: StundenplanPausenzeit | undefined;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}