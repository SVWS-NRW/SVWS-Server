import type { BenutzerKompetenz, List, ServerMode } from "@core";
import type { LeitungsfunktionenListeManager } from "@ui";

export interface LeitungsfunktionenGruppenprozesseProps {
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => LeitungsfunktionenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
}
