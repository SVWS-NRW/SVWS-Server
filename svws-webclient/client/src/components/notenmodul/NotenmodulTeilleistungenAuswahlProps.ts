import type { EnmLerngruppenAuswahlListeManager } from "@ui/components/enm/EnmLerngruppenAuswahlListeManager";
import type { EnmManager, EnmLerngruppenAuswahlEintrag } from "@ui/components/enm/EnmManager";
import type { RouteAuswahlListProps } from "~/router/RouteAuswahlNode";

export interface NotenmodulTeilleistungenAuswahlProps extends RouteAuswahlListProps<EnmLerngruppenAuswahlListeManager> {
	enmManager: () => EnmManager;
	setAuswahlEinzel: (value: EnmLerngruppenAuswahlEintrag | null) => void;
	auswahlEinzel: () => EnmLerngruppenAuswahlEintrag | null;
	setAuswahlMehrfach: (value: Array<EnmLerngruppenAuswahlEintrag>) => void;
	auswahlMehrfach: () => Array<EnmLerngruppenAuswahlEintrag>;
}