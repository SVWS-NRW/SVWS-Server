
import type {
	GostHalbjahr,
	GostJahrgangsdaten,
	GostKlausurtermin,
	GostKursklausurManager,
	StundenplanKalenderwochenzuordnung,
	StundenplanManager,
} from "@core";
import type { WritableComputedRef } from "vue";

export interface GostKlausurplanungKalenderProps {
	jahrgangsdaten: GostJahrgangsdaten;
	halbjahr: GostHalbjahr;
	kMan: () => GostKursklausurManager;
	patchKlausurtermin: (id: number, termin: Partial<GostKlausurtermin>) => Promise<void>;
	stundenplanmanager: () => StundenplanManager;
	hatStundenplanManager: boolean,
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
	zeigeAlleJahrgaenge: () => boolean;
	setZeigeAlleJahrgaenge: (value: boolean) => void;
	kalenderwoche: WritableComputedRef<StundenplanKalenderwochenzuordnung>;
	terminSelected: WritableComputedRef<GostKlausurtermin | undefined>;
	gotoKalenderwoche: (value: StundenplanKalenderwochenzuordnung) => Promise<void>;
}
