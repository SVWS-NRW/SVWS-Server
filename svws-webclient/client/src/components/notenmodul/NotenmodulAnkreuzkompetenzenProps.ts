import type { ENMKlasse, ENMLeistung, ENMLeistungBemerkungen, ENMSchuelerAnkreuzkompetenz } from "@core";
import type { EnmManager } from "@ui";

export interface NotenmodulAnkreuzkompetenzenProps {
	enmManager: () => EnmManager;
	auswahl: () => Array<ENMKlasse>;
	patchLeistung: (data: ENMLeistung, patch: Partial<ENMLeistung>) => Promise<void>;
	patchAnkreuzkompetenz: (data: ENMSchuelerAnkreuzkompetenz, patch: Partial<ENMSchuelerAnkreuzkompetenz>) => Promise<void>;
}
