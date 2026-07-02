import type { Schulform } from "@core";
import type { RouteAuswahlListProps } from "~/router/RouteAuswahlNode";
import type { SchuelerListeManager } from "~/states/schueler/SchuelerListeManager";

export interface SchuelerAuswahlProps extends RouteAuswahlListProps<SchuelerListeManager> {
	schulform: Schulform;
}
