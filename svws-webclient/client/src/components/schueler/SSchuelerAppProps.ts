import type { Schulform } from "@core/asd/types/schule/Schulform";

import type { RouteAuswahlProps } from "~/router/RouteAuswahlNode";
import type { SchuelerListeManager } from "~/states/schueler/SchuelerListeManager";

export interface SchuelerAppProps extends RouteAuswahlProps<SchuelerListeManager> {
	schulform: Schulform;
	gotoDefaultView: (id?: number | null) => Promise<void>;
}
