import type { RouteAuswahlListProps } from "~/router/RouteAuswahlNode";
import type { ENMv1Klasse } from "../../../../core/src/core/data/enm/v1/ENMv1Klasse";
import type { EnmManager } from "../../../../ui/src/components/enm/EnmManager";
import type { EnmLerngruppenAuswahlListeManager } from "../../../../ui/src/components/enm/EnmLerngruppenAuswahlListeManager";

export interface NotenmodulAnkreuzkompetenzenAuswahlProps extends RouteAuswahlListProps<EnmLerngruppenAuswahlListeManager> {
	enmManager: () => EnmManager;
	setAuswahlEinzel: (value: ENMv1Klasse | null) => void;
	auswahlEinzel: () => ENMv1Klasse | null;
	setAuswahlMehrfach: (value: Array<ENMv1Klasse>) => void;
	auswahlMehrfach: () => Array<ENMv1Klasse>;
}