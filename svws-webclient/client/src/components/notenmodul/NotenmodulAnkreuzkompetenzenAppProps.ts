import type { EnmLerngruppenAuswahlListeManager } from "@ui/components/enm/EnmLerngruppenAuswahlListeManager";
import type { EnmManager } from "@ui/components/enm/EnmManager";
import type { RouteAuswahlProps } from "~/router/RouteAuswahlNode";

export interface NotenmodulAnkreuzkompetenzenAppProps extends RouteAuswahlProps<EnmLerngruppenAuswahlListeManager> {
	enmManager: () => EnmManager;
}