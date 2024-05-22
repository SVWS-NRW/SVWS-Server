import type { List, SchuelerListeManager, Schulform, Schulgliederung, ServerMode } from "@core";

export interface SchuelerAbschluesseProps {
	schuelerListeManager: () => SchuelerListeManager;
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
	serverMode: ServerMode;
}