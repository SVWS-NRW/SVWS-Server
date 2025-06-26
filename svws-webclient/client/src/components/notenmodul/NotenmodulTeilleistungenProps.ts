import type { ENMLeistung, ENMTeilleistung } from "@core";
import type { EnmLerngruppenAuswahlEintrag, EnmManager } from "@ui";

export interface NotenmodulTeilleistungenProps {
	enmManager: () => EnmManager;
	auswahl: () => Array<EnmLerngruppenAuswahlEintrag>;
	patchLeistung: (patch: Partial<ENMLeistung>) => Promise<boolean>;
	patchTeilleistung: (patch: Partial<ENMTeilleistung>) => Promise<boolean>;
	columnsVisible: () => Map<string, boolean|null>;
	setColumnsVisible: (columns: Map<string, boolean|null>) => Promise<void>;
}
