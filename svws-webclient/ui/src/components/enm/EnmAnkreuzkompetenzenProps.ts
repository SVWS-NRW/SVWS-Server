import type { ENMKlasse } from "../../../../core/src/core/data/enm/ENMKlasse";
import type { ENMLeistung } from "../../../../core/src/core/data/enm/ENMLeistung";
import type { ENMSchuelerAnkreuzkompetenz } from "../../../../core/src/core/data/enm/ENMSchuelerAnkreuzkompetenz";
import type { EnmManager } from "./EnmManager";

export interface EnmAnkreuzkompetenzenProps {
	enmManager: () => EnmManager;
	auswahl: () => Array<ENMKlasse>;
	patchLeistung: (data: ENMLeistung, patch: Partial<ENMLeistung>) => Promise<void>;
	patchAnkreuzkompetenz: (data: ENMSchuelerAnkreuzkompetenz, patch: Partial<ENMSchuelerAnkreuzkompetenz>) => Promise<void>;
}
