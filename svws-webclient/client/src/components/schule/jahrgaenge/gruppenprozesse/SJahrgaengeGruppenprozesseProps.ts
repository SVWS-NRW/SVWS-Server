import type { Schulform, List, Schulgliederung, ServerMode } from "@core";
import type { JahrgaengeListeManager } from "@ui";

export interface SchuleJahrgangGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
	manager: () => JahrgaengeListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
}
