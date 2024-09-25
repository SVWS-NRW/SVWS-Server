
import type {
	GostHalbjahr,
	GostJahrgangsdaten,
	GostKlausurenCollectionSkrsKrsData,
	GostKlausurraum,
	GostKlausurraumRich,
	GostKlausurtermin,
	GostKursklausur,
	GostKlausurplanManager,
	List,
	BenutzerKompetenz,
} from "@core";
import type { WritableComputedRef } from "vue";

export interface GostKlausurplanungRaumzeitProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	jahrgangsdaten: GostJahrgangsdaten;
	halbjahr: GostHalbjahr;
	gotoTermin: (abiturjahr: number, halbjahr: GostHalbjahr, value: number) => Promise<void>;
	gotoKalenderwoche: (kw: number | GostKlausurtermin) => Promise<void>;
	kMan: () => GostKlausurplanManager;
	createKlausurraum: (raum: Partial<GostKlausurraum>) => Promise<void>;
	loescheKlausurraum: (id: number) => Promise<boolean>;
	patchKlausurraum: (id: number, raum: Partial<GostKlausurraum>) => Promise<boolean>;
	setzeRaumZuSchuelerklausuren: (raeume: List<GostKlausurraumRich>, deleteFromRaeume: boolean) => Promise<GostKlausurenCollectionSkrsKrsData>;
	patchKlausur: (klausur: GostKursklausur, patch: Partial<GostKursklausur>) => Promise<GostKlausurenCollectionSkrsKrsData>;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
	setRaumTermin : (termin: GostKlausurtermin | null) => Promise<void>;
	terminSelected: WritableComputedRef<GostKlausurtermin | undefined>;
	zeigeAlleJahrgaenge: () => boolean;
	setZeigeAlleJahrgaenge: (value: boolean) => void;
	getConfigValue: (value: string) => string;
	setConfigValue: (key: string, value: string) => Promise<void>;
}