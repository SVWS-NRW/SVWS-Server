import type { WritableComputedRef } from "vue";
import type { GostHalbjahr, GostJahrgangsdaten, GostKlausurplanManager } from "@core";

export interface GostKlausurplanungAuswahlChildData {
	name: string,
	text: string,
}

export interface GostKlausurplanungAuswahlProps {
	kMan: () => GostKlausurplanManager;
	jahrgangsdaten: GostJahrgangsdaten | undefined;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
	gotoHalbjahr: (value: GostHalbjahr) => Promise<void>;
	halbjahr: GostHalbjahr;
	setChild: (value: GostKlausurplanungAuswahlChildData) => Promise<void>;
	child: GostKlausurplanungAuswahlChildData;
	children: GostKlausurplanungAuswahlChildData[];
	childrenHidden: boolean[];
}