import type {
	GostKlausurenCollectionSkrsKrs,
	GostKlausurtermin,
	GostKlausurterminblockungDaten,
	GostKursklausur,
	GostKursklausurManager,
	KursManager,
	LehrerListeEintrag,
	List,
} from "@core";
import type { WritableComputedRef } from "vue";

export interface GostKlausurplanungSchienenProps {
	kursklausurmanager: () => GostKursklausurManager;
	mapLehrer: Map<number, LehrerListeEintrag>;
	patchKursklausur: (id: number, klausur: Partial<GostKursklausur>) => Promise<GostKlausurenCollectionSkrsKrs>;
	erzeugeKlausurtermin: (quartal: number) => Promise<GostKlausurtermin>;
	loescheKlausurtermine: (termine: List<GostKlausurtermin>) => Promise<void>;
	erzeugeKursklausurenAusVorgaben: (quartal: number) => Promise<void>;
	kursmanager: KursManager;
	patchKlausurtermin: (id: number, termin: Partial<GostKlausurtermin>) => Promise<void>;
	blockenKursklausuren: (blockungDaten: GostKlausurterminblockungDaten) => Promise<boolean>;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
}
