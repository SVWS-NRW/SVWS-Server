import type { Schulform, List, Schulgliederung, ServerMode } from "@core";
import type { KatalogSchuleListeManager } from "@ui";

export interface KatalogSchuleGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
	schuleListeManager: () => KatalogSchuleListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
}
