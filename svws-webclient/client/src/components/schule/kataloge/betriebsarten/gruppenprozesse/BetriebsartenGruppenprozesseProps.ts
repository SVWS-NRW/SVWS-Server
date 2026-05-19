import type { BenutzerKompetenz, List } from "@core";
import type { BetriebsartenListeManager } from "@ui";

export interface BetriebsartenGruppenprozesseProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => BetriebsartenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => [boolean, List<string>];
	gotoDefaultView: (id: number | null) => Promise<void>;
}
