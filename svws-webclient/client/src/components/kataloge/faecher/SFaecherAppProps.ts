import type { FaecherListeEintrag  } from "@core";
import type { AuswahlChildData } from "~/components/AuswahlChildData";

export interface FaecherAppProps {
	auswahl: () => FaecherListeEintrag | undefined;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}