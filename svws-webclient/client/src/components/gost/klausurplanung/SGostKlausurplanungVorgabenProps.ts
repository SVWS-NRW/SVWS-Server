
import type {
	GostFaecherManager,
	GostHalbjahr,
	GostJahrgangsdaten,
	GostKlausurvorgabe,
	GostKlausurvorgabenManager,
	GostKursklausurManager,
} from "@core";
import type { WritableComputedRef } from "vue";

export interface GostKlausurplanungVorgabenProps {
	jahrgangsdaten: GostJahrgangsdaten | undefined;
	halbjahr: GostHalbjahr;
	klausurvorgabenmanager: () => GostKlausurvorgabenManager;
	kursklausurmanager?: () => GostKursklausurManager;
	faecherManager: GostFaecherManager;
	erzeugeKlausurvorgabe: (vorgabe: Partial<GostKlausurvorgabe>) => Promise<void>;
	patchKlausurvorgabe: (vorgabe: Partial<GostKlausurvorgabe>, id: number) => Promise<void>;
	loescheKlausurvorgabe: (idVorgabe: number) => Promise<void>;
	erzeugeVorgabenAusVorlage: (quartal: number) => Promise<void>;
	erzeugeDefaultKlausurvorgaben: (quartal: number) => Promise<void>;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
}
