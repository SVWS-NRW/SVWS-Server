import { BenutzerKompetenz, OrtKatalogEintrag } from "@core";
import { OrteListeManager } from "@ui";

export interface OrteDatenProps {
	manager: () => OrteListeManager;
	patch: (data: Partial<OrtKatalogEintrag>) => Promise<void>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
