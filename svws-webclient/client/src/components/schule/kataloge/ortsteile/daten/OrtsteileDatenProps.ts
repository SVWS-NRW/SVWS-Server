import type { OrtsteilKatalogEintrag } from "@core/core/data/kataloge/OrtsteilKatalogEintrag";
import type { OrtsteileListeManager } from "@ui/ui/manager/kataloge/OrtsteileListeManager";

export interface OrtsteileDatenProps {
	manager: () => OrtsteileListeManager,
	patch: (data: Partial<OrtsteilKatalogEintrag>) => Promise<boolean>;
}
