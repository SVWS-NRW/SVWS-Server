import type { TelefonartenListeManager } from "@ui/ui/manager/kataloge/TelefonartenListeManager";
import type { TabManager } from "@ui/ui/nav/TabManager";
import type { RouteAuswahlProps } from "~/router/RouteAuswahlNode";

export interface TelefonartenAppProps extends RouteAuswahlProps<TelefonartenListeManager> {
	tabManager: () => TabManager;
}
