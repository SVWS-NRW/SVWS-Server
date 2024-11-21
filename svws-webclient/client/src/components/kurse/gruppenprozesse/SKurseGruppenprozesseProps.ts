import type { Schulform, List, Schulgliederung, KursListeManager, ServerMode } from "@core";

export interface KurseGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
	manager: () => KursListeManager;
	deleteKurse: () => Promise<[boolean, List<string | null>]>;
}
