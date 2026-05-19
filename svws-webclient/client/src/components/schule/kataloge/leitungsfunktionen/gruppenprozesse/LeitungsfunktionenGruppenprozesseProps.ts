import type { BenutzerKompetenz, List } from "@core";
import type { LeitungsfunktionenListeManager } from "@ui";

export interface LeitungsfunktionenGruppenprozesseProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => LeitungsfunktionenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
}
