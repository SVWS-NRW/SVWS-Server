import type { ENMv2Klasse } from "@core/core/data/enm/v2/ENMv2Klasse";
import type { ENMv2Leistung } from "@core/core/data/enm/v2/ENMv2Leistung";
import type { ENMv2Schueler } from "@core/core/data/enm/v2/ENMv2Schueler";
import type { ENMv2SchuelerAnkreuzkompetenz } from "@core/core/data/enm/v2/ENMv2SchuelerAnkreuzkompetenz";
import type { EnmManager } from "./EnmManager";

export interface EnmAnkreuzkompetenzenUebersichtProps {
	enmManager: () => EnmManager;
	auswahl: () => Array<ENMv2Klasse>;
	patchLeistung: (data: ENMv2Leistung, patch: Partial<ENMv2Leistung>) => Promise<void>;
	patchAnkreuzkompetenz: (data: ENMv2SchuelerAnkreuzkompetenz, patch: Partial<ENMv2SchuelerAnkreuzkompetenz>) => Promise<void>;
	focusFloskelEditor: (schueler: ENMv2Schueler | null, leistung: ENMv2Leistung | null, row: number | null, doFocus: boolean) => Promise<void>;
}
