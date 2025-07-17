import type { LehrerListeManager } from "@core";
import type { RouteAuswahlProps } from "~/router/RouteAuswahlNode";

export interface LehrerAppProps extends RouteAuswahlProps<LehrerListeManager> {
	gotoDefaultView: (id?: number | null) => Promise<void>;
}
