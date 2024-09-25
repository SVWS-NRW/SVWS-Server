
import type {
	GostHalbjahr,
	GostJahrgangsdaten,
	GostKlausurtermin,
	GostKlausurplanManager,
	StundenplanKalenderwochenzuordnung,
	StundenplanManager,
	BenutzerKompetenz,
} from "@core";
import type { WritableComputedRef } from "vue";

export interface GostKlausurplanungKalenderProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	jahrgangsdaten: GostJahrgangsdaten;
	halbjahr: GostHalbjahr;
	kMan: () => GostKlausurplanManager;
	patchKlausurtermin: (id: number, termin: Partial<GostKlausurtermin>) => Promise<void>;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
	zeigeAlleJahrgaenge: () => boolean;
	setZeigeAlleJahrgaenge: (value: boolean) => void;
	kalenderwoche: WritableComputedRef<StundenplanKalenderwochenzuordnung>;
	terminSelected: WritableComputedRef<GostKlausurtermin | undefined>;
	gotoKalenderwoche: (kw: StundenplanKalenderwochenzuordnung | number | GostKlausurtermin) => Promise<void>;
	gotoRaumzeitTermin: (abiturjahr: number, halbjahr: GostHalbjahr, value: number) => Promise<void>;

}
