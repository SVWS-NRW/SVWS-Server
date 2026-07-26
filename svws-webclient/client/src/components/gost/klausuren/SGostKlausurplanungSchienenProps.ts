import type {
	GostHalbjahr,
	GostJahrgangsdaten,
	GostKlausurtermin,
	GostKlausurterminblockungDaten,
	GostKursklausur,
	GostKlausurplanManager,
	GostSchuelerklausurtermin,
	List,
	GostKlausurenKlausurdaten,
	Schuljahresabschnitt,
} from "@core";
import type { WritableComputedRef } from "vue";

export interface GostKlausurplanungSchienenProps {
	jahrgangsdaten: GostJahrgangsdaten;
	halbjahr: GostHalbjahr;
	abschnitt: Schuljahresabschnitt | undefined;
	kMan: () => GostKlausurplanManager;
	terminSelected: WritableComputedRef<GostKlausurtermin | undefined>;
	patchKlausur: (klausur: GostKursklausur | GostSchuelerklausurtermin, patch: Partial<GostKursklausur | GostSchuelerklausurtermin>) => Promise<void>;
	createSchuelerklausurtermin: (skt: Partial<GostSchuelerklausurtermin>) => Promise<void>;
	erzeugeKlausurtermin: (quartal: number, istHaupttermin: boolean) => Promise<GostKlausurtermin>;
	loescheKlausurtermine: (termine: List<GostKlausurtermin>) => Promise<void>;
	erzeugeKursklausurenAusVorgaben: (quartal: number) => Promise<GostKlausurenKlausurdaten>;
	loescheKursklausuren: (klausuren: List<GostKursklausur> | GostKursklausur[]) => Promise<void>;
	patchKlausurtermin: (id: number, termin: Partial<GostKlausurtermin>) => Promise<void>;
	blockenKursklausuren: (blockungDaten: GostKlausurterminblockungDaten) => Promise<void>;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
	gotoSchienen: (termin: GostKlausurtermin | undefined) => Promise<void>;
	gotoVorgaben: () => Promise<void>;
	gotoKalenderdatum: (datum: string | undefined, termin: GostKlausurtermin | undefined) => Promise<void>;
	gotoRaumzeitTermin: (abiturjahr: number, halbjahr: GostHalbjahr, value: number) => Promise<void>;
}
