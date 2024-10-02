import type { WritableComputedRef } from "vue";
import type { GostHalbjahr, GostJahrgangsdaten, GostKlausurplanManager } from "@core";
import type { TabData } from "@ui";

export interface GostKlausurplanungAuswahlProps {
	kMan: () => GostKlausurplanManager;
	jahrgangsdaten: GostJahrgangsdaten | undefined;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
	gotoHalbjahr: (value: GostHalbjahr) => Promise<void>;
	halbjahr: GostHalbjahr;
	setChild: (value: TabData) => Promise<void>;
	child: TabData;
	children: TabData[];
	childrenHidden: boolean[];
}