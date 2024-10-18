import type { TabManager } from "@ui";
import type { BetriebListeEintrag } from "@core";

export interface BetriebeAppProps {
	auswahl: BetriebListeEintrag | undefined;
	tabManager: () => TabManager;
}