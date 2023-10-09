
import type {
	GostFaecherManager,
	GostJahrgangsdaten,
	GostKlausurvorgabe,
	GostKlausurvorgabenManager,
} from "@core";
import type { WritableComputedRef } from "vue";

export interface GostKlausurplanungVorgabenProps {
	jahrgangsdaten: GostJahrgangsdaten | undefined;
	klausurvorgabenmanager: () => GostKlausurvorgabenManager;
	faecherManager: GostFaecherManager;
	erzeugeKlausurvorgabe: (vorgabe: Partial<GostKlausurvorgabe>) => Promise<void>;
	patchKlausurvorgabe: (vorgabe: Partial<GostKlausurvorgabe>, id: number) => Promise<void>;
	loescheKlausurvorgabe: (idVorgabe: number) => Promise<void>;
	erzeugeVorgabenAusVorlage: (quartal: number) => Promise<void>;
	erzeugeDefaultKlausurvorgaben: (quartal: number) => Promise<void>;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
}
