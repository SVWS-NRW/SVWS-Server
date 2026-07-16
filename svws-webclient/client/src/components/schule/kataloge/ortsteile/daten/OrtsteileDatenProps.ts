import type { OrtsteilKatalogEintrag } from "@core";
import type { OrtsteileListeManager } from "@ui";

export interface OrtsteileDatenProps {
	manager: () => OrtsteileListeManager,
	patch: (data: Partial<OrtsteilKatalogEintrag>) => Promise<boolean>;
}
