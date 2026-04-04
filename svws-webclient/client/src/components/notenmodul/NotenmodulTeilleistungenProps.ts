import type { ENMv2Leistung, ENMv2Teilleistung } from "@core";
import type { EnmLerngruppenAuswahlEintrag, EnmManager } from "@ui";

export interface NotenmodulTeilleistungenProps {
	enmManager: () => EnmManager;
	auswahl: () => Array<EnmLerngruppenAuswahlEintrag>;
	patchLeistung: (data: ENMv2Leistung, patch: Partial<ENMv2Leistung>) => Promise<void>;
	patchTeilleistung: (data: ENMv2Teilleistung, patch: Partial<ENMv2Teilleistung>) => Promise<void>;
	columnsVisible: () => Map<string, boolean | null>;
	setColumnsVisible: (columns: Map<string, boolean | null>) => Promise<void>;
}
