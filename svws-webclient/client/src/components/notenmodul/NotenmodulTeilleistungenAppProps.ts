import type { EnmLerngruppenAuswahlListeManager } from "@ui/components/enm/EnmLerngruppenAuswahlListeManager";
import type { EnmManager } from "@ui/components/enm/EnmManager";
import type { RouteAuswahlProps } from "~/router/RouteAuswahlNode";

export interface NotenmodulTeilleistungenAppProps extends RouteAuswahlProps<EnmLerngruppenAuswahlListeManager> {
	enmManager: () => EnmManager;
}