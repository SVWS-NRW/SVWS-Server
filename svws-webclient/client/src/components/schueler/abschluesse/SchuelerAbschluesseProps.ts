import type { List, Schulform, Schulgliederung, ServerMode } from "@core";
import type { SchuelerListeManager } from "@ui";

export interface SchuelerAbschluesseProps {
	schuelerListeManager: () => SchuelerListeManager;
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
	serverMode: ServerMode;
}
