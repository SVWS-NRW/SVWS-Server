import type { EnmKlassenleitungAuswahlListeManager, EnmManager } from "@ui";
import type { RouteAuswahlProps } from "~/router/RouteAuswahlNode";

export interface NotenmodulKlassenleitungAppProps extends RouteAuswahlProps<EnmKlassenleitungAuswahlListeManager> {
	enmManager: () => EnmManager;
}