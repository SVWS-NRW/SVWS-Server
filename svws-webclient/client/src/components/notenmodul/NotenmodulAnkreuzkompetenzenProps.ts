import type { ENMv2Klasse, ENMv2Leistung, ENMv2LeistungBemerkungen, ENMv2SchuelerAnkreuzkompetenz } from "@core";
import type { EnmManager } from "@ui";

export interface NotenmodulAnkreuzkompetenzenProps {
	enmManager: () => EnmManager;
	auswahl: () => Array<ENMv2Klasse>;
	patchLeistung: (data: ENMv2Leistung, patch: Partial<ENMv2Leistung>) => Promise<void>;
	patchBemerkungen: (id: number, data: ENMv2LeistungBemerkungen, patch: Partial<ENMv2LeistungBemerkungen>) => Promise<void>;
	patchAnkreuzkompetenz: (data: ENMv2SchuelerAnkreuzkompetenz, patch: Partial<ENMv2SchuelerAnkreuzkompetenz>) => Promise<void>;
}
