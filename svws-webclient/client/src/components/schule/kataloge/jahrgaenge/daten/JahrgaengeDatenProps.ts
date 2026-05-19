import type { JahrgangsDaten, BenutzerKompetenz } from "@core";
import type { JahrgaengeListeManager } from "@ui";

export interface JahrgaengeDatenProps {
	manager: () => JahrgaengeListeManager;
	patch: (data: Partial<JahrgangsDaten>) => Promise<boolean>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
