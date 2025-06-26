import type { ENMLeistung } from "../../../../core/src/core/data/enm/ENMLeistung";
import type { EnmLerngruppenAuswahlEintrag, EnmManager } from "./EnmManager";

export interface EnmLeistungenProps {
	enmManager: () => EnmManager;
	auswahl: () => Array<EnmLerngruppenAuswahlEintrag>;
	patchLeistung: (data: ENMLeistung, patch: Partial<ENMLeistung>) => Promise<void>;
	columnsVisible: () => Map<string, boolean|null>;
	setColumnsVisible: (columns: Map<string, boolean|null>) => Promise<void>;
	floskelEditorVisible: boolean;
	setFloskelEditorVisible: (value: boolean) => Promise<void>;
}
