import type { StundenplanListeEintrag } from "@core";
import type { TabData } from "@ui";

export interface StundenplanAppProps {
	auswahl: StundenplanListeEintrag | undefined;
	setTab: (value: TabData) => Promise<void>;
	tab: TabData;
	tabs: TabData[];
	tabsHidden: boolean[];
}
