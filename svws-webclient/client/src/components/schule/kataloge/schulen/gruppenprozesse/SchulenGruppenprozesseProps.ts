import type { Schulform, List, Schulgliederung, ServerMode, BenutzerKompetenz } from "@core";

export interface SchulenGruppenprozesseProps {
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
	delete: () => Promise<[boolean, List<string | null>]>;
}
