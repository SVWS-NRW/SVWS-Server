
import type {
	GostFaecherManager,
	GostJahrgangsdaten,
	GostKlausurenCollectionSkrsKrs,
	GostKlausurraum,
	GostKlausurraumManager,
	GostKlausurtermin,
	GostKursklausurManager,
	GostSchuelerklausur,
	KursManager,
	LehrerListeEintrag,
	List,
	StundenplanManager,
} from "@core";
import type { WritableComputedRef } from "vue";

export interface GostKlausurplanungRaumzeitProps {
	jahrgangsdaten: GostJahrgangsdaten;
	kursklausurmanager: () => GostKursklausurManager;
	faecherManager: GostFaecherManager;
	mapLehrer: Map<number, LehrerListeEintrag>;
	kursmanager: KursManager;
	stundenplanmanager: StundenplanManager;
	erzeugeKlausurraum: (raum: GostKlausurraum) => Promise<GostKlausurraum>;
	patchKlausurraum: (id: number, raum: Partial<GostKlausurraum>, manager: GostKlausurraumManager) => Promise<boolean>;
	erzeugeKlausurraummanager: (termin: GostKlausurtermin) => Promise<GostKlausurraumManager>;
	setzeRaumZuSchuelerklausuren: (raum: GostKlausurraum, sks: List<GostSchuelerklausur>, manager: GostKlausurraumManager) => Promise<GostKlausurenCollectionSkrsKrs>;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
}