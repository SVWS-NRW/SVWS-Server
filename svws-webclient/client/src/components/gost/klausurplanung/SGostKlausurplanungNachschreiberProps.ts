import type {
	GostHalbjahr,
	GostKlausurenCollectionSkrsKrsData,
	GostKlausurtermin,
	GostKursklausur,
	GostKlausurplanManager,
	GostSchuelerklausurTermin,
	GostNachschreibterminblockungKonfiguration,
	List,
	GostKlausurenUpdate,
	GostJahrgangsdaten,
	Schuljahresabschnitt,
} from "@core";
import type { WritableComputedRef } from "vue";

export interface GostKlausurplanungNachschreiberProps {
	jahrgangsdaten: GostJahrgangsdaten;
	halbjahr: GostHalbjahr;
	kMan: () => GostKlausurplanManager;
	patchKlausur: (klausur: GostKursklausur | GostSchuelerklausurTermin, patch: Partial<GostKursklausur | GostSchuelerklausurTermin>) => Promise<GostKlausurenCollectionSkrsKrsData>;
	erzeugeKlausurtermin: (quartal: number, istHaupttermin: boolean) => Promise<GostKlausurtermin>;
	loescheKlausurtermine: (termine: List<GostKlausurtermin>) => Promise<void>;
	patchKlausurtermin: (id: number, termin: Partial<GostKlausurtermin>) => Promise<void>;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
	blockenNachschreibklausuren: (config: GostNachschreibterminblockungKonfiguration) => Promise<void>;
	updateKlausurblockung: (update: GostKlausurenUpdate) => Promise<void>;
	zeigeAlleJahrgaenge: () => boolean;
	setZeigeAlleJahrgaenge: (value: boolean) => void;
}
