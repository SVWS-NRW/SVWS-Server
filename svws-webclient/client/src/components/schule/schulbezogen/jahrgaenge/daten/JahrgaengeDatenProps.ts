import type { JahrgangsDaten, Schulform, BenutzerKompetenz } from "@core";
import type { JahrgaengeListeManager } from "@ui";

export interface JahrgaengeDatenProps {
	schuljahr: number;
	schulform: Schulform;
	manager: () => JahrgaengeListeManager;
	patch: (data: Partial<JahrgangsDaten>) => Promise<void>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
