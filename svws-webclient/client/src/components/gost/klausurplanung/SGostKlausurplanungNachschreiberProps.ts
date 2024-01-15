import type {
	GostHalbjahr,
	GostKlausurenCollectionSkrsKrs,
	GostKlausurtermin,
	GostKlausurterminblockungDaten,
	GostKursklausur,
	GostKursklausurManager,
	GostSchuelerklausurTermin,
	KursManager,
	LehrerListeEintrag,
	List,
	SchuelerListeEintrag,
} from "@core";
import type { WritableComputedRef } from "vue";

export interface GostKlausurplanungNachschreiberProps {
	halbjahr: GostHalbjahr;
	kursklausurmanager: () => GostKursklausurManager;
	mapLehrer: Map<number, LehrerListeEintrag>;
	mapSchueler: Map<number, SchuelerListeEintrag>;
	patchKlausur: (klausur: GostKursklausur | GostSchuelerklausurTermin, patch: Partial<GostKursklausur | GostSchuelerklausurTermin>) => Promise<GostKlausurenCollectionSkrsKrs>;
	erzeugeKlausurtermin: (quartal: number, istHaupttermin: boolean) => Promise<GostKlausurtermin>;
	loescheKlausurtermine: (termine: List<GostKlausurtermin>) => Promise<void>;
	kursmanager: KursManager;
	patchKlausurtermin: (id: number, termin: Partial<GostKlausurtermin>) => Promise<void>;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
}
