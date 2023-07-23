import type {
	GostFaecherManager,
	GostKlausurtermin,
	GostKursklausur,
	GostKursklausurManager,
	KursManager,
	LehrerListeEintrag,
	SchuelerListeEintrag,
} from "@core";
import type { WritableComputedRef } from "vue";

export interface GostKlausurplanungSchienenProps {
	kursklausurmanager: () => GostKursklausurManager;
	faecherManager: GostFaecherManager;
	mapLehrer: Map<number, LehrerListeEintrag>;
	setTerminToKursklausur: (idTermin: number | null, klausur: GostKursklausur) => Promise<boolean>;
	erzeugeKlausurtermin: (quartal: number) => Promise<GostKlausurtermin>;
	loescheKlausurtermin: (termin: GostKlausurtermin) => Promise<boolean>;
	erzeugeKursklausurenAusVorgaben: (quartal: number) => Promise<boolean>;
	mapSchueler: Map<number, SchuelerListeEintrag>;
	kursmanager: KursManager;
	patchKlausurtermin: (termin: Partial<GostKlausurtermin>, id: number) => Promise<boolean>;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
}
