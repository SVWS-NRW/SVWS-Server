import type { EnmLerngruppenAuswahlListeManager } from "@ui/components/enm/EnmLerngruppenAuswahlListeManager";
import type { EnmManager } from "@ui/components/enm/EnmManager";
import type { RouteAuswahlProps } from "~/router/RouteAuswahlNode";

export interface NotenmodulLeistungenAppProps extends RouteAuswahlProps<EnmLerngruppenAuswahlListeManager> {
	enmManager: () => EnmManager;
}