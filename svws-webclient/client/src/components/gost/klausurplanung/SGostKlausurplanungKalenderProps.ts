
import type {
	GostHalbjahr,
	GostJahrgangsdaten,
	GostKlausurtermin,
	GostKlausurplanManager,
	BenutzerKompetenz,
	Schuljahresabschnitt,
} from "@core";
import type { WritableComputedRef } from "vue";

export interface GostKlausurplanungKalenderProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	jahrgangsdaten: GostJahrgangsdaten;
	halbjahr: GostHalbjahr;
	abschnitt: Schuljahresabschnitt | undefined;
	kMan: () => GostKlausurplanManager;
	patchKlausurtermin: (id: number, termin: Partial<GostKlausurtermin>) => Promise<void>;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
	zeigeAlleJahrgaenge: () => boolean;
	setZeigeAlleJahrgaenge: (value: boolean) => void;
	kalenderdatum: WritableComputedRef<string | undefined>;
	terminSelected: WritableComputedRef<GostKlausurtermin | undefined>;
	gotoKalenderdatum: (datum: string | undefined, termin: GostKlausurtermin | undefined) => Promise<void>;
	gotoRaumzeitTermin: (abiturjahr: number, halbjahr: GostHalbjahr, value: number) => Promise<void>;

}
