
import type {
	GostHalbjahr,
	GostJahrgangsdaten,
	GostKlausurplanManager,
	Schuljahresabschnitt,
} from "@core";
import type { WritableComputedRef } from "vue";

export interface GostKlausurplanungDetailAnsichtProps {
	jahrgangsdaten: GostJahrgangsdaten;
	halbjahr: GostHalbjahr;
	abschnitt: Schuljahresabschnitt | undefined;
	kMan: () => GostKlausurplanManager;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
}
