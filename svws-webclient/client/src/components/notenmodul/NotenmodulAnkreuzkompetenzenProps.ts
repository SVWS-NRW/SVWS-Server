import type { ENMv1Klasse, ENMv1Leistung, ENMv1SchuelerAnkreuzkompetenz } from "@core";
import type { EnmManager } from "@ui";

export interface NotenmodulAnkreuzkompetenzenProps {
	enmManager: () => EnmManager;
	auswahl: () => Array<ENMv1Klasse>;
	patchLeistung: (data: ENMv1Leistung, patch: Partial<ENMv1Leistung>) => Promise<void>;
	patchAnkreuzkompetenz: (data: ENMv1SchuelerAnkreuzkompetenz, patch: Partial<ENMv1SchuelerAnkreuzkompetenz>) => Promise<void>;
}
