import type { Schulform } from "@core";
import type { SchuelerListeManager } from "@ui";
import type { RouteAuswahlListProps } from "~/router/RouteAuswahlNode";

export interface SchuelerAuswahlProps extends RouteAuswahlListProps<SchuelerListeManager> {
	schulform: Schulform;
}
