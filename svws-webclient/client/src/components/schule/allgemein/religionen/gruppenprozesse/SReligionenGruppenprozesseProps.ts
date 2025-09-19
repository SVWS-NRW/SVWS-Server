import type { BenutzerKompetenz, List, ServerMode } from "@core";

export interface ReligionenGruppenprozesseProps {
	serverMode: ServerMode;
	delete: () => Promise<[boolean, List<string | null>]>;
	checkBeforeDelete: () => List<string>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
