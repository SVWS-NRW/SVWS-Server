import type { BenutzerKompetenz, SchulEintrag, Schulform } from "@core";
import type { KatalogSchuleListeManager } from "@ui";

export interface SchuleDatenProps {
	schuljahr: number;
	patch: (data: Partial<SchulEintrag>) => Promise<void>;
	manager: () => KatalogSchuleListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	schulform: Schulform;
}
