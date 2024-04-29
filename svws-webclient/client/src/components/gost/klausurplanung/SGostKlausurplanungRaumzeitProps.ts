
import type {
	GostHalbjahr,
	GostJahrgangsdaten,
	GostKlausurenCollectionSkrsKrs,
	GostKlausurraum,
	GostKlausurraumManager,
	GostKlausurtermin,
	GostKursklausur,
	GostKursklausurManager,
	GostSchuelerklausurTermin,
	List,
	StundenplanManager,
} from "@core";
import type { Ref, WritableComputedRef } from "vue";

export interface GostKlausurplanungRaumzeitProps {
	jahrgangsdaten: GostJahrgangsdaten;
	halbjahr: GostHalbjahr;
	gotoTermin: (value: number) => Promise<void>;
	kMan: () => GostKursklausurManager;
	stundenplanmanager: () => StundenplanManager;
	hatStundenplanManager: boolean,
	raummanager: () => GostKlausurraumManager | undefined;
	createKlausurraum: (raum: Partial<GostKlausurraum>, manager: GostKlausurraumManager) => Promise<void>;
	loescheKlausurraum: (id: number, manager: GostKlausurraumManager) => Promise<boolean>;
	patchKlausurraum: (id: number, raum: Partial<GostKlausurraum>, manager: GostKlausurraumManager) => Promise<boolean>;
	erzeugeKlausurraummanager: (termin: GostKlausurtermin) => Promise<GostKlausurraumManager>;
	setzeRaumZuSchuelerklausuren: (raum: GostKlausurraum | null, sks: List<GostSchuelerklausurTermin>, manager: GostKlausurraumManager) => Promise<GostKlausurenCollectionSkrsKrs>;
	patchKlausur: (klausur: GostKursklausur, patch: Partial<GostKursklausur>) => Promise<GostKlausurenCollectionSkrsKrs>;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
	setRaumTermin : (termin: GostKlausurtermin | null) => Promise<void>;
	zeigeAlleJahrgaenge: () => boolean;
	setZeigeAlleJahrgaenge: (value: boolean) => void;
}