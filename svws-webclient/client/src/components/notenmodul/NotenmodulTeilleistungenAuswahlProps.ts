import type { EnmLerngruppenAuswahlEintrag, EnmLerngruppenAuswahlListeManager, EnmManager } from "@ui";
import type { RouteAuswahlListProps } from "~/router/RouteAuswahlNode";

export interface NotenmodulTeilleistungenAuswahlProps extends RouteAuswahlListProps<EnmLerngruppenAuswahlListeManager> {
	enmManager: () => EnmManager;
	setAuswahlEinzel: (value: EnmLerngruppenAuswahlEintrag | null) => void;
	auswahlEinzel: () => EnmLerngruppenAuswahlEintrag | null;
	setAuswahlMehrfach: (value: Array<EnmLerngruppenAuswahlEintrag>) => void;
	auswahlMehrfach: () => Array<EnmLerngruppenAuswahlEintrag>;
}