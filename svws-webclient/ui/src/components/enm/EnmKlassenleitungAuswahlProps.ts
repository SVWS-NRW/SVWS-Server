import type { ENMv2Klasse } from "@core/core/data/enm/v2/ENMv2Klasse";
import type { EnmManager } from "./EnmManager";

export interface EnmKlassenleitungAuswahlProps {
	enmManager: () => EnmManager;
	setAuswahlEinzel: (value: ENMv2Klasse | null) => void;
	auswahlEinzel: () => ENMv2Klasse | null;
	setAuswahlMehrfach: (value: Array<ENMv2Klasse>) => void;
	auswahlMehrfach: () => Array<ENMv2Klasse>;
}
