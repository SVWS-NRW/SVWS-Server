import type { Raum  } from "@svws-nrw/svws-core";
import type { AuswahlChildData } from "~/components/AuswahlChildData";

export interface RaeumeAppProps {
	auswahl: Raum | undefined;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}