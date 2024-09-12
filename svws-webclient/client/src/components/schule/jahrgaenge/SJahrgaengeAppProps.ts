import type { JahrgangsDaten } from "@core";
import type { AuswahlChildData } from "~/components/AuswahlChildData";

export interface JahrgaengeAppProps {
	auswahl: () => JahrgangsDaten | undefined;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}