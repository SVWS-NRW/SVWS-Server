import type { GostFach } from "@core/core/data/gost/GostFach";
import type { GostJahrgangFachkombination } from "@core/core/data/gost/GostJahrgangFachkombination";
import type { GostJahrgangsdaten } from "@core/core/data/gost/GostJahrgangsdaten";
import type { GostLaufbahnplanungFachkombinationTyp } from "@core/core/types/gost/GostLaufbahnplanungFachkombinationTyp";
import type { GostFaecherManager } from "@core/core/utils/gost/GostFaecherManager";

export interface GostFaecherProps {
	faecherManager: () => GostFaecherManager;
	patchFach: (data: Partial<GostFach>, fach_id: number) => Promise<void>;
	patchFachkombination: (data: Partial<GostJahrgangFachkombination>, id: number) => Promise<void>;
	addFachkombination: (typ: GostLaufbahnplanungFachkombinationTyp) => Promise<GostJahrgangFachkombination | undefined>;
	removeFachkombination: (id: number) => Promise<GostJahrgangFachkombination | undefined>;
	patchJahrgangsdaten: (data: Partial<GostJahrgangsdaten>, abiturjahr: number) => Promise<boolean>;
	jahrgangsdaten: () => GostJahrgangsdaten;
	mapFachkombinationen: () => Map<number, GostJahrgangFachkombination>;
}