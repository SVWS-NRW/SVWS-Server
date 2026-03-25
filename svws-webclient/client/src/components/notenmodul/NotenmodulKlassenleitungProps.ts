import type { ENMv1Klasse, ENMv1LeistungBemerkungen, ENMv1Lernabschnitt } from "@core";
import type { EnmManager } from "@ui";

export interface NotenmodulKlassenleitungProps {
	enmManager: () => EnmManager;
	auswahl: () => Array<ENMv1Klasse>;
	patchBemerkungen: (id: number, data: ENMv1LeistungBemerkungen, patch: Partial<ENMv1LeistungBemerkungen>) => Promise<void>;
	patchLernabschnitt: (data: ENMv1Lernabschnitt, patch: Partial<ENMv1Lernabschnitt>) => Promise<void>;
	columnsVisible: () => Map<string, boolean | null>;
	setColumnsVisible: (columns: Map<string, boolean | null>) => Promise<void>;
}
