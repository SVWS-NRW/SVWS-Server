import type { GostFaecherManager, GostFach, GostJahrgangFachkombination, GostLaufbahnplanungFachkombinationTyp, GostJahrgangsdaten } from "@core";

export interface GostFaecherProps {
	faecherManager: () => GostFaecherManager;
	patchFach: (data: Partial<GostFach>, fach_id: number) => Promise<void>;
	patchFachkombination: (data: Partial<GostJahrgangFachkombination>, id : number) => Promise<void>;
	addFachkombination: (typ: GostLaufbahnplanungFachkombinationTyp) => Promise<GostJahrgangFachkombination | undefined>;
	removeFachkombination: (id: number) => Promise<GostJahrgangFachkombination | undefined>;
	patchJahrgangsdaten: (data: Partial<GostJahrgangsdaten>, abiturjahr : number) => Promise<boolean>;
	jahrgangsdaten: () => GostJahrgangsdaten;
	mapFachkombinationen: () => Map<number, GostJahrgangFachkombination>;
}