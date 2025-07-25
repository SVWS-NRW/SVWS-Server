import type {
	GostHalbjahr,
	GostJahrgangsdaten,
	GostKlausurenCollectionSkrsKrsData,
	GostKlausurtermin,
	GostKlausurterminblockungDaten,
	GostKursklausur,
	GostKlausurplanManager,
	GostSchuelerklausurTermin,
	List,
	BenutzerKompetenz,
	GostKlausurenCollectionData,
} from "@core";
import type { WritableComputedRef } from "vue";

export interface GostKlausurplanungSchienenProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	jahrgangsdaten: GostJahrgangsdaten;
	halbjahr: GostHalbjahr;
	kMan: () => GostKlausurplanManager;
	terminSelected: WritableComputedRef<GostKlausurtermin | undefined>;
	patchKlausur: (klausur: GostKursklausur | GostSchuelerklausurTermin, patch: Partial<GostKursklausur | GostSchuelerklausurTermin>) => Promise<void>;
	createSchuelerklausurTermin: (id: number) => Promise<void>;
	erzeugeKlausurtermin: (quartal: number, istHaupttermin: boolean) => Promise<GostKlausurtermin>;
	loescheKlausurtermine: (termine: List<GostKlausurtermin>) => Promise<void>;
	erzeugeKursklausurenAusVorgaben: (quartal: number) => Promise<GostKlausurenCollectionData>;
	loescheKursklausuren: (klausuren: List<GostKursklausur> | GostKursklausur[]) => Promise<void>;
	patchKlausurtermin: (id: number, termin: Partial<GostKlausurtermin>) => Promise<void>;
	blockenKursklausuren: (blockungDaten: GostKlausurterminblockungDaten) => Promise<void>;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
	gotoSchienen: (termin: GostKlausurtermin | undefined) => Promise<void>;
	gotoVorgaben: () => Promise<void>;
	gotoKalenderdatum: (datum: string | undefined, termin: GostKlausurtermin | undefined) => Promise<void>;
	gotoRaumzeitTermin: (abiturjahr: number, halbjahr: GostHalbjahr, value: number) => Promise<void>;
}
