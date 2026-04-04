import type { EnmManager } from "./EnmManager";
import type { ENMv2LeistungBemerkungen } from "../../../../core/src/core/data/enm/v2/ENMv2LeistungBemerkungen";
import type { ENMv2Lernabschnitt } from "../../../../core/src/core/data/enm/v2/ENMv2Lernabschnitt";
import type { ENMv2Klasse } from "../../../../core/src/core/data/enm/v2/ENMv2Klasse";

export interface EnmKlassenleitungProps {
	enmManager: () => EnmManager;
	auswahl: () => Array<ENMv2Klasse>;
	patchBemerkungen: (id: number, data: ENMv2LeistungBemerkungen, patch: Partial<ENMv2LeistungBemerkungen>) => Promise<void>;
	patchLernabschnitt: (data: ENMv2Lernabschnitt, patch: Partial<ENMv2Lernabschnitt>) => Promise<void>;
	columnsVisible: () => Map<string, boolean | null>;
	setColumnsVisible: (columns: Map<string, boolean | null>) => Promise<void>;
}
