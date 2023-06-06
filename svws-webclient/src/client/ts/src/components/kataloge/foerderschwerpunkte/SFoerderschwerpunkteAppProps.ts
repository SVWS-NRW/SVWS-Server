import type { FoerderschwerpunktEintrag  } from "@core";
import type { AuswahlChildData } from "~/components/AuswahlChildData";

export interface FoerderschwerpunkteAppProps {
	auswahl: FoerderschwerpunktEintrag | undefined;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}