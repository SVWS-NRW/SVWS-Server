import type { ENMv1Leistung, ENMv1Teilleistung } from "@core";
import type { EnmLerngruppenAuswahlEintrag, EnmManager } from "@ui";

export interface NotenmodulTeilleistungenProps {
	enmManager: () => EnmManager;
	auswahl: () => Array<EnmLerngruppenAuswahlEintrag>;
	patchLeistung: (data: ENMv1Leistung, patch: Partial<ENMv1Leistung>) => Promise<void>;
	patchTeilleistung: (data: ENMv1Teilleistung, patch: Partial<ENMv1Teilleistung>) => Promise<void>;
	columnsVisible: () => Map<string, boolean | null>;
	setColumnsVisible: (columns: Map<string, boolean | null>) => Promise<void>;
}
