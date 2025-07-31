import type { RouteAuswahlProps } from "~/router/RouteAuswahlNode";
import type { TabManager, TelefonArtListeManager } from "@ui";

export interface TelefonArtenAppProps extends RouteAuswahlProps<TelefonArtListeManager> {
	tabManager: () => TabManager;
}
