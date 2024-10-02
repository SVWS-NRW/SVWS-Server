import type { BenutzerListeEintrag } from "@core";
import type { TabData } from "@ui";

export interface BenutzerAppProps {
	auswahl: () => BenutzerListeEintrag | undefined;
	setTab: (value: TabData) => Promise<void>;
	tab: TabData;
	tabs: TabData[];
	tabsHidden: boolean[];
}