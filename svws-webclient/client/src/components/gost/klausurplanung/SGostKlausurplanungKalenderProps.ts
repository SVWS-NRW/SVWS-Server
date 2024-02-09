
import type {
	GostHalbjahr,
	GostJahrgangsdaten,
	GostKlausurtermin,
	GostKursklausurManager,
	SchuelerListeEintrag,
	StundenplanManager,
} from "@core";
import type { WritableComputedRef } from "vue";

export interface GostKlausurplanungKalenderProps {
	jahrgangsdaten: GostJahrgangsdaten;
	halbjahr: GostHalbjahr;
	kMan: () => GostKursklausurManager;
	patchKlausurtermin: (id: number, termin: Partial<GostKlausurtermin>) => Promise<void>;
	stundenplanmanager: StundenplanManager;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
}
