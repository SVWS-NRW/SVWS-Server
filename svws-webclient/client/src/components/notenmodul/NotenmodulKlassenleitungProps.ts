import type { ENMv2Klasse, ENMv2LeistungBemerkungen, ENMv2Lernabschnitt } from "@core";
import type { EnmManager } from "@ui";

export interface NotenmodulKlassenleitungProps {
	enmManager: () => EnmManager;
	auswahl: () => Array<ENMv2Klasse>;
	patchBemerkungen: (id: number, data: ENMv2LeistungBemerkungen, patch: Partial<ENMv2LeistungBemerkungen>) => Promise<void>;
	patchLernabschnitt: (data: ENMv2Lernabschnitt, patch: Partial<ENMv2Lernabschnitt>) => Promise<void>;
	columnsVisible: () => Map<string, boolean | null>;
	setColumnsVisible: (columns: Map<string, boolean | null>) => Promise<void>;
}
