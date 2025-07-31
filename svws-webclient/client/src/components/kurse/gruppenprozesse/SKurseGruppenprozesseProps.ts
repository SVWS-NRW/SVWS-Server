import type { Schulform, List, Schulgliederung, ServerMode } from "@core";
import type { KursListeManager } from "@ui";

export interface KurseGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
	manager: () => KursListeManager;
	deleteKurse: () => Promise<[boolean, List<string | null>]>;
}
