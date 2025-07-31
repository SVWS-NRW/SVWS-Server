import type { List, BenutzerKompetenz } from "@core";
import type { StundenplanListeManager } from "@ui";

export interface StundenplanGruppenprozesseProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	stundenplanListeManager: () => StundenplanListeManager;
	deleteStundenplan: () => Promise<[boolean, List<string | null>]>;
}
