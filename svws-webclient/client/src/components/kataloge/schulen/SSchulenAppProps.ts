import type { SchulEintrag } from "@core";
import type { AuswahlChildData } from "~/components/AuswahlChildData";

export interface SchulenAppProps {
	auswahl: SchulEintrag | undefined;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}