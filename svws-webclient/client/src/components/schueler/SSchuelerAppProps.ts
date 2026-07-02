import type { Schulform } from "@core";
import type { SchuelerListeManager } from "~/states/schueler/SchuelerListeManager";

import type { RouteAuswahlProps } from "~/router/RouteAuswahlNode";

export interface SchuelerAppProps extends RouteAuswahlProps<SchuelerListeManager> {
	schulform: Schulform;
	gotoDefaultView: (id?: number | null) => Promise<void>;
}
