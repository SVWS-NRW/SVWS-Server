import type { SchuelerListeManager, Schulform } from "@core";
import type { RouteAuswahlProps } from "~/router/RouteAuswahlNode";

export interface SchuelerAppProps extends RouteAuswahlProps<SchuelerListeManager> {
	schulform: Schulform;
	gotoDefaultView: (id?: number | null) => Promise<void>;
}
