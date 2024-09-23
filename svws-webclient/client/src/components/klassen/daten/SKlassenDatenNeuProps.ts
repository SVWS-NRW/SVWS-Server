import type { Schulform, KlassenDaten, KlassenListeManager } from "@core";

export interface KlassenDatenNeuProps {
	klassenListeManager: () => KlassenListeManager;
	schulform: Schulform;
	mapKlassenVorigerAbschnitt: () => Map<number, KlassenDaten>;
	mapKlassenFolgenderAbschnitt: () => Map<number, KlassenDaten>;
	add: (patchObject: Partial<KlassenDaten>) => Promise<void>;
	gotoEintrag: (eintragId?: number | null) => Promise<void>;
}
