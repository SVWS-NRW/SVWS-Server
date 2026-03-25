import type { EnmManager } from "./EnmManager";
import type { ENMv1Klasse } from "../../../../core/src/core/data/enm/v1/ENMv1Klasse";

export interface EnmKlassenleitungAuswahlProps {
	enmManager: () => EnmManager;
	setAuswahlEinzel: (value: ENMv1Klasse | null) => void;
	auswahlEinzel: () => ENMv1Klasse | null;
	setAuswahlMehrfach: (value: Array<ENMv1Klasse>) => void;
	auswahlMehrfach: () => Array<ENMv1Klasse>;
}
