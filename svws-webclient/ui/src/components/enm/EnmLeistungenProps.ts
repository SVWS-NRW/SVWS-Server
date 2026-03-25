import type { ENMv1Leistung } from "../../../../core/src/core/data/enm/v1/ENMv1Leistung";
import type { EnmLerngruppenAuswahlEintrag, EnmManager } from "./EnmManager";

export interface EnmLeistungenProps {
	enmManager: () => EnmManager;
	auswahl: () => Array<EnmLerngruppenAuswahlEintrag>;
	patchLeistung: (data: ENMv1Leistung, patch: Partial<ENMv1Leistung>) => Promise<void>;
	columnsVisible: () => Map<string, boolean | null>;
	setColumnsVisible: (columns: Map<string, boolean | null>) => Promise<void>;
}
