import type { BenutzerKompetenz, SchulEintrag } from "@core";
import type { KatalogSchuleListeManager } from "@ui";

export interface SchuleDatenProps {
	schuljahr: number;
	patch: (data : Partial<SchulEintrag>) => Promise<void>;
	schuleListeManager: () => KatalogSchuleListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
