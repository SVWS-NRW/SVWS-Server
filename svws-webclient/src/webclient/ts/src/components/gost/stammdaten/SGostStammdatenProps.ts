import { GostJahrgangsdaten } from "@svws-nrw/svws-core-ts";

export interface GostStammdatenProps {
	patchJahrgangsdaten: (data: Partial<GostJahrgangsdaten>, abiturjahr : number) => Promise<boolean>;
	jahrgangsdaten: GostJahrgangsdaten;
}