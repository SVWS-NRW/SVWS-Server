import type {
	GostFaecherManager,
	GostKlausurtermin,
	GostKursklausur,
	GostKursklausurManager,
	LehrerListeEintrag,
	SchuelerListeEintrag,
} from "@svws-nrw/svws-core";

export interface GostKlausurplanungSchienenProps {
	kursklausurmanager: () => GostKursklausurManager;
	faecherManager: GostFaecherManager;
	mapLehrer: Map<number, LehrerListeEintrag>;
	setTerminToKursklausur: (idTermin: number | null, klausur: GostKursklausur) => Promise<boolean>;
	erzeugeKlausurtermin: (quartal: number) => Promise<GostKlausurtermin>;
	loescheKlausurtermin: (termin: GostKlausurtermin) => Promise<boolean>;
	erzeugeKursklausurenAusVorgaben: (quartal: number) => Promise<boolean>;
	mapSchueler: Map<number, SchuelerListeEintrag>;
	patchKlausurtermin: (termin: Partial<GostKlausurtermin>, id: number) => Promise<boolean>;
}
