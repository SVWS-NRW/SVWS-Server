import type { EnmManager } from "./EnmManager";
import type { ENMLeistungBemerkungen } from "../../../../core/src/core/data/enm/ENMLeistungBemerkungen";
import type { ENMLernabschnitt } from "../../../../core/src/core/data/enm/ENMLernabschnitt";
import type { ENMKlasse } from "../../../../core/src";

export interface EnmKlassenleitungProps {
	enmManager: () => EnmManager;
	auswahl: () => Array<ENMKlasse>;
	patchBemerkungen: (id: number, data: ENMLeistungBemerkungen, patch: Partial<ENMLeistungBemerkungen>) => Promise<void>;
	patchLernabschnitt: (data: ENMLernabschnitt, patch: Partial<ENMLernabschnitt>) => Promise<void>;
	columnsVisible: () => Map<string, boolean|null>;
	setColumnsVisible: (columns: Map<string, boolean|null>) => Promise<void>;
	floskelEditorVisible: boolean;
	setFloskelEditorVisible: (value: boolean) => Promise<void>;
}
