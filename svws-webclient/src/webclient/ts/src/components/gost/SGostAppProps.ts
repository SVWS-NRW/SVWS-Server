import { GostJahrgang } from "@svws-nrw/svws-core-ts";
import { AuswahlChildData } from "../AuswahlChildData";

export interface GostAppProps {
	auswahl: GostJahrgang | undefined;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}