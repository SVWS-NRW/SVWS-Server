import type { JahrgangsDaten, Schulform, BenutzerKompetenz } from "@core";
import type { JahrgaengeListeManager } from "@ui";

export interface JahrgangDatenProps {
	schuljahr: number;
	schulform: Schulform;
	manager: () => JahrgaengeListeManager;
	patch: (data : Partial<JahrgangsDaten>) => Promise<void>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
