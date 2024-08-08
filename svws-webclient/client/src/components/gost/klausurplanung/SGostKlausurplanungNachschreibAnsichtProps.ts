
import type {
	GostHalbjahr,
	GostJahrgangsdaten,
	GostKlausurplanManager,
} from "@core";
import type { WritableComputedRef } from "vue";

export interface GostKlausurplanungNachschreibAnsichtProps {
	jahrgangsdaten: GostJahrgangsdaten;
	halbjahr: GostHalbjahr;
	kMan: () => GostKlausurplanManager;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
}
