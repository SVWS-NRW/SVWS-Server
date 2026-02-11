import type { BenutzerKompetenz, OrtsteilKatalogEintrag } from "@core";
import type { OrtsteileListeManager } from "@ui";

export interface OrtsteileDatenProps {
	manager: () => OrtsteileListeManager,
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	patch: (data: Partial<OrtsteilKatalogEintrag>) => Promise<void>;
}
