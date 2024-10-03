import type { WritableComputedRef } from "vue";
import type { GostHalbjahr, GostJahrgangsdaten, GostKlausurplanManager } from "@core";
import type { TabManager } from "@ui";

export interface GostKlausurplanungAuswahlProps {
	kMan: () => GostKlausurplanManager;
	jahrgangsdaten: GostJahrgangsdaten | undefined;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
	gotoHalbjahr: (value: GostHalbjahr) => Promise<void>;
	halbjahr: GostHalbjahr;
	tabManager: () => TabManager;
}