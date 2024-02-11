
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
	SchuelerListeEintrag,
	List,
	StundenplanManager,
} from "@core";
import type { WritableComputedRef } from "vue";

export interface GostKlausurplanungRaumzeitProps {
	jahrgangsdaten: GostJahrgangsdaten;
	halbjahr: GostHalbjahr;
	kMan: () => GostKursklausurManager;
	stundenplanmanager: () => StundenplanManager;
	hatStundenplanManager: boolean,
	createKlausurraum: (raum: Partial<GostKlausurraum>, manager: GostKlausurraumManager) => Promise<void>;
	loescheKlausurraum: (id: number, manager: GostKlausurraumManager) => Promise<boolean>;
	patchKlausurraum: (id: number, raum: Partial<GostKlausurraum>, manager: GostKlausurraumManager) => Promise<boolean>;
	erzeugeKlausurraummanager: (termin: GostKlausurtermin) => Promise<GostKlausurraumManager>;
	setzeRaumZuSchuelerklausuren: (raum: GostKlausurraum | null, sks: List<GostSchuelerklausurTermin>, manager: GostKlausurraumManager) => Promise<GostKlausurenCollectionSkrsKrs>;
	patchKlausur: (klausur: GostKursklausur, patch: Partial<GostKursklausur>) => Promise<GostKlausurenCollectionSkrsKrs>;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
}