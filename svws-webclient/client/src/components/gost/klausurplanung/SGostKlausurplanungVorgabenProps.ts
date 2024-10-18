
import type {
	GostFaecherManager,
	GostHalbjahr,
	GostJahrgangsdaten,
	GostKlausurvorgabe,
	GostKlausurplanManager,
	BenutzerKompetenz,
} from "@core";
import type { WritableComputedRef } from "vue";

export interface GostKlausurplanungVorgabenProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	jahrgangsdaten: GostJahrgangsdaten | undefined;
	halbjahr: GostHalbjahr;
	kMan: () => GostKlausurplanManager;
	erzeugeKlausurvorgabe: (vorgabe: Partial<GostKlausurvorgabe>) => Promise<void>;
	patchKlausurvorgabe: (vorgabe: Partial<GostKlausurvorgabe>, id: number) => Promise<void>;
	loescheKlausurvorgabe: (idVorgabe: number) => Promise<void>;
	erzeugeVorgabenAusVorlage: (quartal: number) => Promise<void>;
	erzeugeDefaultKlausurvorgaben: (quartal: number) => Promise<void>;
	quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
}
