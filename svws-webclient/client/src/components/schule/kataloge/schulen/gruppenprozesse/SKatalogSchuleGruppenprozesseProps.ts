import type { Schulform, List, Schulgliederung, ServerMode, KatalogSchuleListeManager } from "@core";

export interface KatalogSchuleGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
	schuleListeManager: () => KatalogSchuleListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
}
