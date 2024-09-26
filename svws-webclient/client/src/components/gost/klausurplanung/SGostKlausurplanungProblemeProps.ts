
import type {
	GostHalbjahr,
	GostJahrgangsdaten,
	GostKlausurplanManager,
	GostKlausurtermin,
	GostSchuelerklausur,
	List,
} from "@core";
import type { WritableComputedRef } from "vue";

export interface GostKlausurplanungProblemeProps {
	jahrgangsdaten: GostJahrgangsdaten | undefined;
	halbjahr: GostHalbjahr;
	kMan: () => GostKlausurplanManager;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
	erzeugeSchuelerklausuren: (termine: List<Partial<GostSchuelerklausur>>) => Promise<void>;
	loescheSchuelerklausuren: (termine: List<GostSchuelerklausur>) => Promise<void>;
	erzeugeKursklausurenAusVorgaben: (quartal: number) => Promise<void>;
	gotoVorgaben: () => Promise<void>;
	gotoSchienen: (termin: GostKlausurtermin | undefined) => Promise<void>;
	gotoKalenderwoche: (kw: number | GostKlausurtermin) => Promise<void>;
	gotoRaumzeitTermin: (abiturjahr: number, halbjahr: GostHalbjahr, value: number) => Promise<void>;
	gotoNachschreiber: (abiturjahr: number, halbjahr: GostHalbjahr) => Promise<void>;
}