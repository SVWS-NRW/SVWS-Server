import type { ENMv2Leistung } from "@core/core/data/enm/v2/ENMv2Leistung";
import type { EnmManager, EnmLerngruppenAuswahlEintrag } from "@ui/components/enm/EnmManager";

export interface NotenmodulLeistungenProps {
	enmManager: () => EnmManager;
	auswahl: () => Array<EnmLerngruppenAuswahlEintrag>;
	patchLeistung: (data: ENMv2Leistung, patch: Partial<ENMv2Leistung>) => Promise<void>;
	columnsVisible: () => Map<string, boolean | null>;
	setColumnsVisible: (columns: Map<string, boolean | null>) => Promise<void>;
}
