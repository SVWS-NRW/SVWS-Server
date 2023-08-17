import type {
	GostFaecherManager,
	GostJahrgangsdaten,
	GostKlausurtermin,
	GostKlausurterminblockungDaten,
	GostKursklausur,
	GostKursklausurManager,
	KursManager,
	LehrerListeEintrag,
	List,
	SchuelerListeEintrag,
} from "@core";
import type { WritableComputedRef } from "vue";

export interface GostKlausurplanungSchienenProps {
	jahrgangsdaten: GostJahrgangsdaten;
	kursklausurmanager: () => GostKursklausurManager;
	faecherManager: GostFaecherManager;
	mapLehrer: Map<number, LehrerListeEintrag>;
	setTerminToKursklausur: (idTermin: number | null, klausur: GostKursklausur) => Promise<boolean>;
	erzeugeKlausurtermin: (quartal: number) => Promise<GostKlausurtermin>;
	loescheKlausurtermine: (termine: List<GostKlausurtermin>) => Promise<boolean>;
	erzeugeKursklausurenAusVorgaben: (quartal: number) => Promise<boolean>;
	mapSchueler: Map<number, SchuelerListeEintrag>;
	kursmanager: KursManager;
	patchKlausurterminDatum: (id: number, termin: Partial<GostKlausurtermin>) => Promise<boolean>;
	blockenKursklausuren: (blockungDaten: GostKlausurterminblockungDaten) => Promise<boolean>;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
}
