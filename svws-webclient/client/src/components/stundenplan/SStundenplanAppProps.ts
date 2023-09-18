import type { StundenplanListeEintrag } from "@core";
import type { AuswahlChildData } from "../AuswahlChildData";

export interface StundenplanAppProps {
	auswahl: StundenplanListeEintrag | undefined;
	setTab: (value: AuswahlChildData) => Promise<void>;
	tab: AuswahlChildData;
	tabs: AuswahlChildData[];
	tabsHidden: boolean[];
}
