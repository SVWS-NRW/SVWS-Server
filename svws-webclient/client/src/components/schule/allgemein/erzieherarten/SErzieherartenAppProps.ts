import type { RouteAuswahlProps } from "~/router/RouteAuswahlNode";
import type { TabManager, ErzieherartListeManager } from "@ui";

export interface ErzieherartenAppProps extends RouteAuswahlProps<ErzieherartListeManager> {
	tabManager: () => TabManager;
}
