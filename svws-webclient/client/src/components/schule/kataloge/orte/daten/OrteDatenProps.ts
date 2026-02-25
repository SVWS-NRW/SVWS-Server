import type { BenutzerKompetenz, OrtKatalogEintrag } from "@core";
import type { OrteListeManager } from "@ui";

export interface OrteDatenProps {
	manager: () => OrteListeManager;
	patch: (data: Partial<OrtKatalogEintrag>) => Promise<boolean>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
