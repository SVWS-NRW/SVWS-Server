import type { Schulform, KlassenListeManager, List, Schulgliederung, BenutzerKompetenz } from "@core";

export interface KlassenGruppenprozesseProps {
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	schulgliederungen: List<Schulgliederung>;
	manager: () => KlassenListeManager;
	deleteKlassen: () => Promise<[boolean, List<string | null>]>;
}
