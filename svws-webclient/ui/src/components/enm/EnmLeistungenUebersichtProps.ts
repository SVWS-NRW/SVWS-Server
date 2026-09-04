import type { ENMv2Leistung } from "@core/core/data/enm/v2/ENMv2Leistung";
import type { ENMv2Schueler } from "@core/core/data/enm/v2/ENMv2Schueler";
import type { EnmLerngruppenAuswahlEintrag, EnmManager } from "./EnmManager";

export interface EnmLeistungenUebersichtProps {
	enmManager: () => EnmManager;
	auswahl: () => Array<EnmLerngruppenAuswahlEintrag>;
	patchLeistung: (data: ENMv2Leistung, patch: Partial<ENMv2Leistung>) => Promise<void>;
	columnsVisible: () => Map<string, boolean | null>;
	setColumnsVisible: (columns: Map<string, boolean | null>) => Promise<void>;
	focusFloskelEditor: (schueler: ENMv2Schueler | null, leistung: ENMv2Leistung | null, row: number | null, doFocus: boolean) => Promise<void>;
}
