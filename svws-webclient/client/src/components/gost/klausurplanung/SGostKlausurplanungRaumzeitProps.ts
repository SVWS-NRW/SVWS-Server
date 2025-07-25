
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
	Schuljahresabschnitt,
} from "@core";
import type { WritableComputedRef } from "vue";

export interface GostKlausurplanungRaumzeitProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	jahrgangsdaten: GostJahrgangsdaten;
	halbjahr: GostHalbjahr;
	abschnitt: Schuljahresabschnitt | undefined;
	gotoTermin: (abiturjahr: number, halbjahr: GostHalbjahr, value: number) => Promise<void>;
	gotoKalenderdatum: (datum: string | undefined, termin: GostKlausurtermin | undefined) => Promise<void>;
	kMan: () => GostKlausurplanManager;
	createKlausurraum: (raum: Partial<GostKlausurraum>) => Promise<void>;
	loescheKlausurraum: (id: number) => Promise<boolean>;
	patchKlausurraum: (id: number, raum: Partial<GostKlausurraum>) => Promise<boolean>;
	setzeRaumZuSchuelerklausuren: (raeume: List<GostKlausurraumRich>, deleteFromRaeume: boolean) => Promise<void>;
	patchKlausur: (klausur: GostKursklausur, patch: Partial<GostKursklausur>) => Promise<void>;
	patchKlausurtermin: (id: number, termin: Partial<GostKlausurtermin>) => Promise<void>;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
	setRaumTermin : (termin: GostKlausurtermin | null) => void;
	terminSelected: WritableComputedRef<GostKlausurtermin | undefined>;
	zeigeAlleJahrgaenge: () => boolean;
	setZeigeAlleJahrgaenge: (value: boolean) => void;
	getConfigValue: (value: string) => string;
	setConfigValue: (key: string, value: string) => Promise<void>;
}