import type { List, BenutzerKompetenz, StundenplanListeManager } from "@core";

export interface StundenplanGruppenprozesseProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	stundenplanListeManager: () => StundenplanListeManager;
	deleteStundenplan: () => Promise<[boolean, List<string | null>]>;
}
