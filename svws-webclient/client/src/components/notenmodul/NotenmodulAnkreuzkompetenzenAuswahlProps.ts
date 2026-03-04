import type { RouteAuswahlListProps } from "~/router/RouteAuswahlNode";
import type { ENMKlasse } from "../../../../core/src/core/data/enm/ENMKlasse";
import type { EnmManager } from "../../../../ui/src/components/enm/EnmManager";
import type { EnmLerngruppenAuswahlListeManager } from "../../../../ui/src/components/enm/EnmLerngruppenAuswahlListeManager";

export interface NotenmodulAnkreuzkompetenzenAuswahlProps extends RouteAuswahlListProps<EnmLerngruppenAuswahlListeManager> {
	enmManager: () => EnmManager;
	setAuswahlEinzel: (value: ENMKlasse | null) => void;
	auswahlEinzel: () => ENMKlasse | null;
	setAuswahlMehrfach: (value: Array<ENMKlasse>) => void;
	auswahlMehrfach: () => Array<ENMKlasse>;
}