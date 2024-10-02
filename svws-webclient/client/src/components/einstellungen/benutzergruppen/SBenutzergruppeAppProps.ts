import type { BenutzergruppeListeEintrag} from "@core";
import type { TabData } from "@ui";

export interface BenutzergruppeAppProps {
	auswahl:() => BenutzergruppeListeEintrag | undefined;
	setTab: (value: TabData) => Promise<void>;
	tab: TabData;
	tabs: TabData[];
	tabsHidden: boolean[];
}