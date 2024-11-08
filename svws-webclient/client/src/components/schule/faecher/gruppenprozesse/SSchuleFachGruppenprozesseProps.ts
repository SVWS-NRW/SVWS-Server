import type { Schulform, List, Schulgliederung, FachListeManager, ServerMode } from "@core";

export interface SchuleFachGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
	fachListeManager: () => FachListeManager;
	deleteFaecher: () => Promise<[boolean, List<string | null>]>;
	deleteFaecherCheck: () => [boolean, List<string>];
}
