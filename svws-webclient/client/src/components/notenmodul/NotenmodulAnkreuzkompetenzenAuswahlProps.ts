import type { RouteAuswahlListProps } from "~/router/RouteAuswahlNode";
import type { ENMv2Klasse } from "../../../../core/src/core/data/enm/v2/ENMv2Klasse";
import type { EnmManager } from "../../../../ui/src/components/enm/EnmManager";
import type { EnmLerngruppenAuswahlListeManager } from "../../../../ui/src/components/enm/EnmLerngruppenAuswahlListeManager";

export interface NotenmodulAnkreuzkompetenzenAuswahlProps extends RouteAuswahlListProps<EnmLerngruppenAuswahlListeManager> {
	enmManager: () => EnmManager;
	setAuswahlEinzel: (value: ENMv2Klasse | null) => void;
	auswahlEinzel: () => ENMv2Klasse | null;
	setAuswahlMehrfach: (value: Array<ENMv2Klasse>) => void;
	auswahlMehrfach: () => Array<ENMv2Klasse>;
}