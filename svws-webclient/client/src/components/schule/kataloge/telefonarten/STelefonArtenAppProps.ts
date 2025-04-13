import type { RouteAuswahlProps } from "~/router/RouteAuswahlNode";
import type { TelefonArtListeManager } from "@core";
import type { TabManager } from "@ui";

export interface TelefonArtenAppProps extends RouteAuswahlProps<TelefonArtListeManager> {
	tabManager: () => TabManager;
}
