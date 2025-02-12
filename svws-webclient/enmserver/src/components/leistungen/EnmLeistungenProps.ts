import type { ENMLeistung } from "@core/core/data/enm/ENMLeistung";
import type { EnmManager } from "./EnmManager";

export interface EnmLeistungenProps {
	manager: EnmManager;
	patchLeistung: (patch: Partial<ENMLeistung>) => Promise<boolean>;
	columnsVisible: () => Map<string, boolean|null>;
	setColumnsVisible: (columns: Map<string, boolean|null>) => Promise<void>;
	floskelEditorVisible: boolean;
	setFloskelEditorVisible: (value: boolean) => Promise<void>;
}
