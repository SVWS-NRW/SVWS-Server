import type { RouteAuswahlProps } from "~/router/RouteAuswahlNode";
import type { ErzieherartListeManager } from "@core";
import type { TabManager } from "@ui";

export interface ErzieherartenAppProps extends RouteAuswahlProps<ErzieherartListeManager> {
	tabManager: () => TabManager;
}
