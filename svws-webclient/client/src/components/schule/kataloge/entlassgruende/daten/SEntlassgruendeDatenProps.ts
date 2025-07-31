import type { BenutzerKompetenz, KatalogEntlassgrund } from "@core";
import type { EntlassgruendeListeManager } from "@ui";

export interface EntlassgruendeDatenProps {
	manager: () => EntlassgruendeListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	patch: (data: Partial<KatalogEntlassgrund>) => Promise<void>;
}
