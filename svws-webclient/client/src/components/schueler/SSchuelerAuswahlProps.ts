import type { Schulform, SchuelerListeManager } from "@core";
import type { RouteAuswahlListProps } from "~/router/RouteAuswahlNode";

export interface SchuelerAuswahlProps extends RouteAuswahlListProps<SchuelerListeManager> {
	schulform: Schulform;
}
