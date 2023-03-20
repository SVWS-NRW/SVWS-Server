import { GostJahrgangsdaten } from "@svws-nrw/svws-core";

export interface GostStammdatenProps {
	patchJahrgangsdaten: (data: Partial<GostJahrgangsdaten>, abiturjahr : number) => Promise<boolean>;
	jahrgangsdaten: () => GostJahrgangsdaten;
}