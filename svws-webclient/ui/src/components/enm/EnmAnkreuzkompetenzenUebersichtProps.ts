import type { ENMv1Klasse } from "../../../../core/src/core/data/enm/v1/ENMv1Klasse";
import type { ENMv1Leistung } from "../../../../core/src/core/data/enm/v1/ENMv1Leistung";
import type { ENMv1Schueler } from "../../../../core/src/core/data/enm/v1/ENMv1Schueler";
import type { ENMv1SchuelerAnkreuzkompetenz } from "../../../../core/src/core/data/enm/v1/ENMv1SchuelerAnkreuzkompetenz";
import type { EnmManager } from "./EnmManager";

export interface EnmAnkreuzkompetenzenUebersichtProps {
	enmManager: () => EnmManager;
	auswahl: () => Array<ENMv1Klasse>;
	patchLeistung: (data: ENMv1Leistung, patch: Partial<ENMv1Leistung>) => Promise<void>;
	patchAnkreuzkompetenz: (data: ENMv1SchuelerAnkreuzkompetenz, patch: Partial<ENMv1SchuelerAnkreuzkompetenz>) => Promise<void>;
	focusFloskelEditor: (schueler: ENMv1Schueler | null, leistung: ENMv1Leistung | null, row: number | null, doFocus: boolean) => Promise<void>;
}
