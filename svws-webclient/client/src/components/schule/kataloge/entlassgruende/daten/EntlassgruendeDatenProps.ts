import type { KatalogEntlassgrund } from "@core";
import type { EntlassgruendeListeManager } from "@ui";

export interface EntlassgruendeDatenProps {
	manager: () => EntlassgruendeListeManager;
	patch: (data: Partial<KatalogEntlassgrund>) => Promise<boolean>;
}
