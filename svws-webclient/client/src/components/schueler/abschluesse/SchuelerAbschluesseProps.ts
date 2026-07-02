import type { List, Schulform, Schulgliederung } from "@core";
import type { SchuelerListeManager } from "~/states/schueler/SchuelerListeManager";

export interface SchuelerAbschluesseProps {
	schuelerListeManager: () => SchuelerListeManager;
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
}
