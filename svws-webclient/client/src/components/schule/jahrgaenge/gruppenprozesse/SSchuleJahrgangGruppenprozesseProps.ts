import type { JahrgangListeManager, Schulform, List, Schulgliederung, ServerMode } from "@core";

export interface SchuleJahrgangGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
	jahrgangListeManager: () => JahrgangListeManager;
	deleteJahrgaenge: () => Promise<[boolean, List<string | null>]>;
	deleteJahrgaengeCheck: () => [boolean, List<string>];
}
