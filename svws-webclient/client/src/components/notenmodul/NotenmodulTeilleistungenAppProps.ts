import type { EnmLerngruppenAuswahlListeManager, EnmManager } from "@ui";
import type { RouteAuswahlProps } from "~/router/RouteAuswahlNode";

export interface NotenmodulTeilleistungenAppProps extends RouteAuswahlProps<EnmLerngruppenAuswahlListeManager> {
	enmManager: () => EnmManager;
}