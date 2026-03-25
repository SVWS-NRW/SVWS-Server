import type { ENMv1Leistung } from "../../../../core/src/core/data/enm/v1/ENMv1Leistung";
import type { EnmLerngruppenAuswahlEintrag, EnmManager } from "./EnmManager";
import type { ENMv1Teilleistung } from "../../../../core/src/core/data/enm/v1/ENMv1Teilleistung";

export interface EnmTeilleistungenProps {
	enmManager: () => EnmManager;
	auswahl: () => Array<EnmLerngruppenAuswahlEintrag>;
	patchLeistung: (data: ENMv1Leistung, patch: Partial<ENMv1Leistung>) => Promise<void>;
	patchTeilleistung: (data: ENMv1Teilleistung, patch: Partial<ENMv1Teilleistung>) => Promise<void>;
	columnsVisible: () => Map<string, boolean | null>;
	setColumnsVisible: (columns: Map<string, boolean | null>) => Promise<void>;
}
