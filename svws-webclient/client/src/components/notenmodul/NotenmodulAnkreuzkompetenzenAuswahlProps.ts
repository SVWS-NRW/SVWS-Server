import type { ENMv2Klasse } from "@core/core/data/enm/v2/ENMv2Klasse";
import type { EnmLerngruppenAuswahlListeManager } from "@ui/components/enm/EnmLerngruppenAuswahlListeManager";
import type { EnmManager } from "@ui/components/enm/EnmManager";
import type { RouteAuswahlListProps } from "~/router/RouteAuswahlNode";

export interface NotenmodulAnkreuzkompetenzenAuswahlProps extends RouteAuswahlListProps<EnmLerngruppenAuswahlListeManager> {
	enmManager: () => EnmManager;
	setAuswahlEinzel: (value: ENMv2Klasse | null) => void;
	auswahlEinzel: () => ENMv2Klasse | null;
	setAuswahlMehrfach: (value: Array<ENMv2Klasse>) => void;
	auswahlMehrfach: () => Array<ENMv2Klasse>;
}