import type { EnmManager } from "./EnmManager";
import type { ENMv2Klasse } from "../../../../core/src/core/data/enm/v2/ENMv2Klasse";

export interface EnmKlassenleitungAuswahlProps {
	enmManager: () => EnmManager;
	setAuswahlEinzel: (value: ENMv2Klasse | null) => void;
	auswahlEinzel: () => ENMv2Klasse | null;
	setAuswahlMehrfach: (value: Array<ENMv2Klasse>) => void;
	auswahlMehrfach: () => Array<ENMv2Klasse>;
}
