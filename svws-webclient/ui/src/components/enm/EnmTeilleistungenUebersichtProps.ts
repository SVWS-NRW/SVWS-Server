import type { ENMv2Leistung } from "../../../../core/src/core/data/enm/v2/ENMv2Leistung";
import type { EnmLerngruppenAuswahlEintrag, EnmManager } from "./EnmManager";
import type { ENMv2Teilleistung } from "../../../../core/src/core/data/enm/v2/ENMv2Teilleistung";

export interface EnmTeilleistungenUebersichtProps {
	enmManager: () => EnmManager;
	auswahl: () => Array<EnmLerngruppenAuswahlEintrag>;
	patchLeistung: (data: ENMv2Leistung, patch: Partial<ENMv2Leistung>) => Promise<void>;
	patchTeilleistung: (data: ENMv2Teilleistung, patch: Partial<ENMv2Teilleistung>) => Promise<void>;
	columnsVisible: () => Map<string, boolean | null>;
	setColumnsVisible: (columns: Map<string, boolean | null>) => Promise<void>;
}
