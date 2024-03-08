import type {
	GostHalbjahr,
	GostKlausurenCollectionSkrsKrs,
	GostKlausurtermin,
	GostKursklausur,
	GostKursklausurManager,
	GostSchuelerklausurTermin,
	GostNachschreibterminblockungKonfiguration,
	List,
	GostKlausurenUpdate,
} from "@core";
import type { WritableComputedRef } from "vue";

export interface GostKlausurplanungNachschreiberProps {
	halbjahr: GostHalbjahr;
	kMan: () => GostKursklausurManager;
	patchKlausur: (klausur: GostKursklausur | GostSchuelerklausurTermin, patch: Partial<GostKursklausur | GostSchuelerklausurTermin>) => Promise<GostKlausurenCollectionSkrsKrs>;
	erzeugeKlausurtermin: (quartal: number, istHaupttermin: boolean) => Promise<GostKlausurtermin>;
	loescheKlausurtermine: (termine: List<GostKlausurtermin>) => Promise<void>;
	patchKlausurtermin: (id: number, termin: Partial<GostKlausurtermin>) => Promise<void>;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
	blockenNachschreibklausuren: (config: GostNachschreibterminblockungKonfiguration) => Promise<void>;
	updateKlausurblockung: (update: GostKlausurenUpdate) => Promise<void>;
}
