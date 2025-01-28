import type {Schulform, List, Schulgliederung, ServerMode, BenutzerKompetenz, EinwilligungsartenListeManager} from "@core";

export interface SchuleEinwilligungsartenGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	schulgliederungen: List<Schulgliederung>;
	manager: () => EinwilligungsartenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
}
