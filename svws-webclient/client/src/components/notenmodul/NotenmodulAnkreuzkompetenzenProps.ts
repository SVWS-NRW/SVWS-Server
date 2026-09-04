import type { ENMv2Klasse } from "@core/core/data/enm/v2/ENMv2Klasse";
import type { ENMv2Leistung } from "@core/core/data/enm/v2/ENMv2Leistung";
import type { ENMv2LeistungBemerkungen } from "@core/core/data/enm/v2/ENMv2LeistungBemerkungen";
import type { ENMv2SchuelerAnkreuzkompetenz } from "@core/core/data/enm/v2/ENMv2SchuelerAnkreuzkompetenz";
import type { EnmManager } from "@ui/components/enm/EnmManager";

export interface NotenmodulAnkreuzkompetenzenProps {
	enmManager: () => EnmManager;
	auswahl: () => Array<ENMv2Klasse>;
	patchLeistung: (data: ENMv2Leistung, patch: Partial<ENMv2Leistung>) => Promise<void>;
	patchBemerkungen: (id: number, data: ENMv2LeistungBemerkungen, patch: Partial<ENMv2LeistungBemerkungen>) => Promise<void>;
	patchAnkreuzkompetenz: (data: ENMv2SchuelerAnkreuzkompetenz, patch: Partial<ENMv2SchuelerAnkreuzkompetenz>) => Promise<void>;
}
