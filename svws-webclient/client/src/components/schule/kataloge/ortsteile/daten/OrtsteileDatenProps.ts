import { BenutzerKompetenz, OrtsteilKatalogEintrag } from "@core";
import { OrtsteileListeManager } from "@ui";

export interface OrtsteileDatenProps {
	manager: () => OrtsteileListeManager,
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	patch: (data: Partial<OrtsteilKatalogEintrag>) => Promise<void>;
}
