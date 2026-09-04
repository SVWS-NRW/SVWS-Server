import type { KatalogEntlassgrund } from "@core/core/data/kataloge/KatalogEntlassgrund";
import type { EntlassgruendeListeManager } from "@ui/ui/manager/kataloge/EntlassgruendeListeManager";

export interface EntlassgruendeDatenProps {
	manager: () => EntlassgruendeListeManager;
	patch: (data: Partial<KatalogEntlassgrund>) => Promise<boolean>;
}
