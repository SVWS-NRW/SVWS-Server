import type { EnmManager } from "./EnmManager";
import type { ENMLeistungBemerkungen } from "@core/core/data/enm/ENMLeistungBemerkungen";
import type { ENMLernabschnitt } from "@core/core/data/enm/ENMLernabschnitt";

export interface EnmKlassenleitungProps {
	manager: EnmManager;
	patchBemerkungen: (id: number, patch: Partial<ENMLeistungBemerkungen>) => Promise<boolean>;
	patchLernabschnitt: (patch: Partial<ENMLernabschnitt>) => Promise<boolean>;
	columnsVisible: () => Map<string, boolean|null>;
	setColumnsVisible: (columns: Map<string, boolean|null>) => Promise<void>;
	floskelEditorVisible: boolean;
	setFloskelEditorVisible: (value: boolean) => Promise<void>;
}
