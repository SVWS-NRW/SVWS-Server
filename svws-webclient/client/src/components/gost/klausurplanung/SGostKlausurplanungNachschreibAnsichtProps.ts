
import type {
	GostHalbjahr,
	GostJahrgangsdaten,
	GostKlausurraumManager,
	GostKursklausurManager,
} from "@core";
import type { WritableComputedRef } from "vue";

export interface GostKlausurplanungNachschreibAnsichtProps {
	jahrgangsdaten: GostJahrgangsdaten;
	halbjahr: GostHalbjahr;
	kMan: () => GostKursklausurManager;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
	raummanager: GostKlausurraumManager;
}
