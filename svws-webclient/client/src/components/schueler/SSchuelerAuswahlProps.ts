import type { Schulform } from "@core/asd/types/schule/Schulform";
import type { RouteAuswahlListProps } from "~/router/RouteAuswahlNode";
import type { SchuelerListeManager } from "~/states/schueler/SchuelerListeManager";

export interface SchuelerAuswahlProps extends RouteAuswahlListProps<SchuelerListeManager> {
	schulform: Schulform;
}
