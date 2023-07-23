
import type {
	GostFaecherManager,
	GostJahrgangsdaten,
	GostKlausurvorgabe,
	GostKlausurvorgabenManager,
	GostKursklausurManager,
	LehrerListeEintrag,
} from "@core";
import type { WritableComputedRef } from "vue";

export interface GostKlausurplanungDatenProps {
	jahrgangsdaten: GostJahrgangsdaten | undefined;
	kursklausurmanager: () => GostKursklausurManager;
	klausurvorgabenmanager: () => GostKlausurvorgabenManager;
	faecherManager: GostFaecherManager;
	mapLehrer: Map<number, LehrerListeEintrag>;
	erzeugeKlausurvorgabe: (vorgabe: GostKlausurvorgabe) => Promise<GostKlausurvorgabe>;
	patchKlausurvorgabe: (vorgabe: Partial<GostKlausurvorgabe>, id: number) => Promise<boolean>;
	loescheKlausurvorgabe: (vorgabe: GostKlausurvorgabe) => Promise<boolean>;
	erzeugeVorgabenAusVorlage: () => Promise<boolean>;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
}
