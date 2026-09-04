import type { OrtKatalogEintrag } from "@core/core/data/kataloge/OrtKatalogEintrag";
import type { OrteListeManager } from "@ui/ui/manager/kataloge/OrteListeManager";

export interface OrteDatenProps {
	manager: () => OrteListeManager;
	patch: (data: Partial<OrtKatalogEintrag>) => Promise<boolean>;
}
