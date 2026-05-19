import type { BenutzerKompetenz, List } from "@core";
import type { AbteilungenListeManager } from "@ui";

export interface AbteilungenGruppenprozesseProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => AbteilungenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
}
