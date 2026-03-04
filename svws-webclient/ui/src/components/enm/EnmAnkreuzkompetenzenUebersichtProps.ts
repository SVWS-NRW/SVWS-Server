import type { ENMKlasse } from "../../../../core/src/core/data/enm/ENMKlasse";
import type { ENMLeistung } from "../../../../core/src/core/data/enm/ENMLeistung";
import type { ENMSchueler } from "../../../../core/src/core/data/enm/ENMSchueler";
import type { ENMSchuelerAnkreuzkompetenz } from "../../../../core/src/core/data/enm/ENMSchuelerAnkreuzkompetenz";
import type { EnmManager } from "./EnmManager";

export interface EnmAnkreuzkompetenzenUebersichtProps {
	enmManager: () => EnmManager;
	auswahl: () => Array<ENMKlasse>;
	patchLeistung: (data: ENMLeistung, patch: Partial<ENMLeistung>) => Promise<void>;
	patchAnkreuzkompetenz: (data: ENMSchuelerAnkreuzkompetenz, patch: Partial<ENMSchuelerAnkreuzkompetenz>) => Promise<void>;
	focusFloskelEditor: (schueler: ENMSchueler | null, leistung: ENMLeistung | null, row: number | null, doFocus: boolean) => Promise<void>;
}
