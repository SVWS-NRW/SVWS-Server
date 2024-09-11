import type { Schulform, KlassenDaten, KlassenListeManager, List, Schulgliederung } from "@core";

export interface KlassenDatenNeuProps {
	klassenListeManager: () => KlassenListeManager;
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
	mapKlassenVorigerAbschnitt: () => Map<number, KlassenDaten>;
	mapKlassenFolgenderAbschnitt: () => Map<number, KlassenDaten>;
	addKlasse: (patchObject: Partial<KlassenDaten>) => Promise<void>;
	gotoEintrag: (eintragId?: number | null) => Promise<void>;
}
