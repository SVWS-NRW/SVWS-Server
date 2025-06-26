import type { EnmManager } from "./EnmManager";
import type { ENMKlasse } from "../../../../core/src/core/data/enm/ENMKlasse";

export interface EnmKlassenleitungAuswahlProps {
	enmManager: () => EnmManager;
	setAuswahlEinzel: (value: ENMKlasse | null) => void;
	auswahlEinzel: () => ENMKlasse | null;
	setAuswahlMehrfach: (value: Array<ENMKlasse>) => void;
	auswahlMehrfach: () => Array<ENMKlasse>;
}
