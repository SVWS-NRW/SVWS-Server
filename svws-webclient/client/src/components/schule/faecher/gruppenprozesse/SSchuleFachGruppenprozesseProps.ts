import type { Schulform, List, Schulgliederung, FachListeManager, ServerMode } from "@core";

export interface SchuleFachGruppenprozesseProps {
	mode: ServerMode;
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
	fachListeManager: () => FachListeManager;
	deleteFaecher: () => Promise<[boolean, List<string | null>]>;
	deleteFaecherCheck: () => [boolean, List<string>];
}
