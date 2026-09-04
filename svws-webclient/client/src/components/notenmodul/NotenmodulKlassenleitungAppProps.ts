import type { EnmKlassenleitungAuswahlListeManager } from "@ui/components/enm/EnmKlassenleitungAuswahlListeManager";
import type { EnmManager } from "@ui/components/enm/EnmManager";
import type { RouteAuswahlProps } from "~/router/RouteAuswahlNode";

export interface NotenmodulKlassenleitungAppProps extends RouteAuswahlProps<EnmKlassenleitungAuswahlListeManager> {
	enmManager: () => EnmManager;
}