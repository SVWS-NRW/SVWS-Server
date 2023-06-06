import type { GostJahrgang } from "@core";
import type { AuswahlChildData } from "../AuswahlChildData";

export interface GostAppProps {
	auswahl: GostJahrgang | undefined;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}