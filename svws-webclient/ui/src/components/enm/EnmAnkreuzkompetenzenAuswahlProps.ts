import type { ENMKlasse } from "../../../../core/src/core/data/enm/ENMKlasse";
import type { EnmManager } from "./EnmManager";

export interface EnmAnkreuzkompetenzenAuswahlProps {
	enmManager: () => EnmManager;
	setAuswahlEinzel: (value: ENMKlasse | null) => void;
	auswahlEinzel: () => ENMKlasse | null;
	setAuswahlMehrfach: (value: Array<ENMKlasse>) => void;
	auswahlMehrfach: () => Array<ENMKlasse>;
}
