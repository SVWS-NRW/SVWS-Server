import type { RouteAuswahlProps } from "~/router/RouteAuswahlNode";
import type { TabManager, TelefonartenListeManager } from "@ui";

export interface TelefonartenAppProps extends RouteAuswahlProps<TelefonartenListeManager> {
	tabManager: () => TabManager;
}
