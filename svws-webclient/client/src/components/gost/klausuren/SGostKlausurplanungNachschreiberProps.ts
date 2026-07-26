import type { GostHalbjahr, GostJahrgangsdaten, GostKlausurplanManager, GostKlausurtermin, GostKursklausur, GostNachschreibterminblockungKonfiguration, GostSchuelerklausurtermin, List } from "@core";
import type { WritableComputedRef } from "vue";

export interface GostKlausurplanungNachschreiberProps {
	jahrgangsdaten: GostJahrgangsdaten;
	halbjahr: GostHalbjahr;
	kMan: () => GostKlausurplanManager;
	patchKlausur: (klausur: GostKursklausur | GostSchuelerklausurtermin, patch: Partial<GostKursklausur | GostSchuelerklausurtermin>) => Promise<void>;
	erzeugeKlausurtermin: (quartal: number, istHaupttermin: boolean) => Promise<GostKlausurtermin>;
	loescheKlausurtermine: (termine: List<GostKlausurtermin>) => Promise<void>;
	patchKlausurtermin: (id: number, termin: Partial<GostKlausurtermin>) => Promise<void>;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
	blockenNachschreibklausuren: (config: GostNachschreibterminblockungKonfiguration) => Promise<void>;
	zeigeAlleJahrgaenge: () => boolean;
	setZeigeAlleJahrgaenge: (value: boolean) => void;
	gotoNachschreiber: (abiturjahr: number, halbjahr: GostHalbjahr) => Promise<void>;
	gotoKalenderdatum: (datum: string | undefined, termin: GostKlausurtermin | undefined) => Promise<void>;
	gotoRaumzeitTermin: (abiturjahr: number, halbjahr: GostHalbjahr, value: number) => Promise<void>;
}
