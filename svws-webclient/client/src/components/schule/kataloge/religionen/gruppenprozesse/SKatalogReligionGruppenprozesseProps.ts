import type { ReligionListeManager, Schulform, List, Schulgliederung, ServerMode } from "@core";

export interface KatalogReligionGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
	religionListeManager: () => ReligionListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	checkBeforeDelete: () => List<string>;
}
