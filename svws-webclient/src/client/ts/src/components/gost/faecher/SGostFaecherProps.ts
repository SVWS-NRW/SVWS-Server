import type { GostFaecherManager, GostFach, GostJahrgangFachkombination, GostLaufbahnplanungFachkombinationTyp, GostJahrgangsdaten } from "@svws-nrw/svws-core";

export interface GostFaecherProps {
	faecherManager: () => GostFaecherManager;
	patchFach: (data: Partial<GostFach>, fach_id: number) => Promise<boolean>;
	patchFachkombination: (data: Partial<GostJahrgangFachkombination>, id : number) => Promise<boolean>;
	addFachkombination: (typ: GostLaufbahnplanungFachkombinationTyp) => Promise<GostJahrgangFachkombination | undefined>;
	removeFachkombination: (id: number) => Promise<GostJahrgangFachkombination | undefined>;
	patchJahrgangsdaten: (data: Partial<GostJahrgangsdaten>, abiturjahr : number) => Promise<boolean>;
	jahrgangsdaten: () => GostJahrgangsdaten;
	mapFachkombinationen: () => Map<number, GostJahrgangFachkombination>;
}