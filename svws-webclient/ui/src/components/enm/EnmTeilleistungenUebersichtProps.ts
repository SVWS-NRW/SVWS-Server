import type { ENMLeistung } from "../../../../core/src/core/data/enm/ENMLeistung";
import type { EnmLerngruppenAuswahlEintrag, EnmManager } from "./EnmManager";
import type { ENMTeilleistung } from "../../../../core/src/core/data/enm/ENMTeilleistung";

export interface EnmTeilleistungenUebersichtProps {
	enmManager: () => EnmManager;
	auswahl: () => Array<EnmLerngruppenAuswahlEintrag>;
	patchLeistung: (data: ENMLeistung, patch: Partial<ENMLeistung>) => Promise<void>;
	patchTeilleistung: (data: ENMTeilleistung, patch: Partial<ENMTeilleistung>) => Promise<void>;
	columnsVisible: () => Map<string, boolean|null>;
	setColumnsVisible: (columns: Map<string, boolean|null>) => Promise<void>;
}
