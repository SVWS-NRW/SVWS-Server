import type { FaecherListeEintrag  } from "@svws-nrw/svws-core";
import type { AuswahlChildData } from "~/components/AuswahlChildData";

export interface FaecherAppProps {
	auswahl: FaecherListeEintrag | undefined;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}