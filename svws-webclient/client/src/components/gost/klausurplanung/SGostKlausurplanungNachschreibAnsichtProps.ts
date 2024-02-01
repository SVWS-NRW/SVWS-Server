
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

export interface GostKlausurplanungNachschreibAnsichtProps {
	jahrgangsdaten: GostJahrgangsdaten;
	halbjahr: GostHalbjahr;
	kMan: () => GostKursklausurManager;
	erzeugeKlausurraummanager: (termin: GostKlausurtermin) => Promise<GostKlausurraumManager>;
	mapSchueler: Map<number, SchuelerListeEintrag>;
	stundenplanmanager: StundenplanManager;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
}
