import type { BenutzerKompetenz, Haltestelle, HaltestellenListeManager } from "@core";

export interface HaltestellenDatenProps {
	manager: () => HaltestellenListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	patch: (data: Partial<Haltestelle>) => Promise<void>;
}
