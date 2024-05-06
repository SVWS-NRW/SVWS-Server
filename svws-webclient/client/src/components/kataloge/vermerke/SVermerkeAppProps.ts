import type { VermerkartEintrag } from "@core";
import type { AuswahlChildData } from "~/components/AuswahlChildData";

export interface VermerkeAppProps {
	auswahl: VermerkartEintrag | undefined;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}
