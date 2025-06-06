import type { BenutzerKompetenz, EntlassgruendeListeManager, KatalogEntlassgrund } from "@core";

export interface EntlassgruendeDatenProps {
	manager: () => EntlassgruendeListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	patch: (data: Partial<KatalogEntlassgrund>) => Promise<void>;
}
