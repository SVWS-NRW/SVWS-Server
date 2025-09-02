import type { Schulform, List, Schulgliederung, ServerMode } from "@core";
import type { ReligionListeManager } from "@ui";

export interface KatalogReligionGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
	religionListeManager: () => ReligionListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	checkBeforeDelete: () => List<string>;
}
