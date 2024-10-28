import type { Schulform, List, Schulgliederung } from "@core";
import type { LehrerListeManager } from "@core";

export interface LehrerGruppenprozesseProps {
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
	lehrerListeManager: () => LehrerListeManager;
	deleteLehrer: () => Promise<[boolean, List<string | null>]>;
	deleteLehrerCheck: () => [boolean, List<string>];
}
