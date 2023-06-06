import type { GostJahrgangsdaten } from "@core";

export interface GostStammdatenProps {
	patchJahrgangsdaten: (data: Partial<GostJahrgangsdaten>, abiturjahr : number) => Promise<boolean>;
	jahrgangsdaten: () => GostJahrgangsdaten;
}