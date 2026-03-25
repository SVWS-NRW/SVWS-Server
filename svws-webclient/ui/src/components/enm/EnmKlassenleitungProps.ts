import type { EnmManager } from "./EnmManager";
import type { ENMv1LeistungBemerkungen } from "../../../../core/src/core/data/enm/v1/ENMv1LeistungBemerkungen";
import type { ENMv1Lernabschnitt } from "../../../../core/src/core/data/enm/v1/ENMv1Lernabschnitt";
import type { ENMv1Klasse } from "../../../../core/src/core/data/enm/v1/ENMv1Klasse";

export interface EnmKlassenleitungProps {
	enmManager: () => EnmManager;
	auswahl: () => Array<ENMv1Klasse>;
	patchBemerkungen: (id: number, data: ENMv1LeistungBemerkungen, patch: Partial<ENMv1LeistungBemerkungen>) => Promise<void>;
	patchLernabschnitt: (data: ENMv1Lernabschnitt, patch: Partial<ENMv1Lernabschnitt>) => Promise<void>;
	columnsVisible: () => Map<string, boolean | null>;
	setColumnsVisible: (columns: Map<string, boolean | null>) => Promise<void>;
}
