import type { ENMLeistung } from "@core/core/data/enm/ENMLeistung";
import type { EnmManager } from "./EnmManager";
import type { ENMTeilleistung } from "@core/core/data/enm/ENMTeilleistung";

export interface EnmTeilleistungenProps {
	manager: EnmManager;
	patchLeistung: (patch: Partial<ENMLeistung>) => Promise<boolean>;
	patchTeilleistung: (patch: Partial<ENMTeilleistung>) => Promise<boolean>;
	columnsVisible: () => Map<string, boolean|null>;
	setColumnsVisible: (columns: Map<string, boolean|null>) => Promise<void>;
}
