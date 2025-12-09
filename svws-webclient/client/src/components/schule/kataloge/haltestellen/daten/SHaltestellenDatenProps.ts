import type { BenutzerKompetenz, Haltestelle } from "@core";
import type { HaltestellenListeManager } from "@ui";

export interface HaltestellenDatenProps {
	manager: () => HaltestellenListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	patch: (data: Partial<Haltestelle>) => Promise<void>;
}
