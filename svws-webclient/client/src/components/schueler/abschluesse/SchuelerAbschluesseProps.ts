import type { List, Schulform, Schulgliederung } from "@core";
import type { SchuelerListeManager } from "@ui";

export interface SchuelerAbschluesseProps {
	schuelerListeManager: () => SchuelerListeManager;
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
}
