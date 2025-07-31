import type { Schulform } from "@core";
import type { SchuelerListeManager } from "@ui";

import type { RouteAuswahlProps } from "~/router/RouteAuswahlNode";

export interface SchuelerAppProps extends RouteAuswahlProps<SchuelerListeManager> {
	schulform: Schulform;
	gotoDefaultView: (id?: number | null) => Promise<void>;
}
