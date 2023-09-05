
import type {
	GostFaecherManager,
	GostJahrgangsdaten,
	GostKlausurvorgabe,
	GostKlausurvorgabenManager,
	GostKursklausurManager,
	LehrerListeEintrag,
} from "@core";
import type { WritableComputedRef } from "vue";

export interface GostKlausurplanungVorgabenProps {
	jahrgangsdaten: GostJahrgangsdaten | undefined;
	kursklausurmanager: () => GostKursklausurManager;
	klausurvorgabenmanager: () => GostKlausurvorgabenManager;
	faecherManager: GostFaecherManager;
	mapLehrer: Map<number, LehrerListeEintrag>;
	erzeugeKlausurvorgabe: (vorgabe: Partial<GostKlausurvorgabe>) => Promise<void>;
	patchKlausurvorgabe: (vorgabe: Partial<GostKlausurvorgabe>, id: number) => Promise<void>;
	loescheKlausurvorgabe: (vorgabe: GostKlausurvorgabe) => Promise<void>;
	erzeugeVorgabenAusVorlage: (quartal: number) => Promise<void>;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
}
