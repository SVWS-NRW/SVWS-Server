import type { GostJahrgangsdaten } from "@core/core/data/gost/GostJahrgangsdaten";

export interface GostBeratungProps {
	patchJahrgangsdaten: (data: Partial<GostJahrgangsdaten>, abiturjahr: number) => Promise<boolean>;
}