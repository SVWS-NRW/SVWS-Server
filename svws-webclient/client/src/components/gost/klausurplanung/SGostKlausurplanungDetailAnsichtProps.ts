
import type {
	GostHalbjahr,
	GostJahrgangsdaten,
	GostKlausurraumManager,
	GostKlausurtermin,
	GostKursklausurManager,
	SchuelerListeEintrag,
	StundenplanManager,
} from "@core";
import type { WritableComputedRef } from "vue";

export interface GostKlausurplanungDetailAnsichtProps {
	jahrgangsdaten: GostJahrgangsdaten;
	halbjahr: GostHalbjahr;
	kMan: () => GostKursklausurManager;
	erzeugeKlausurraummanager: (termin: GostKlausurtermin) => Promise<GostKlausurraumManager>;
	stundenplanmanager: StundenplanManager;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
}
