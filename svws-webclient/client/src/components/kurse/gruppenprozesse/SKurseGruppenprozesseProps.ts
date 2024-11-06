import type { Schulform, List, Schulgliederung, KursListeManager, ServerMode } from "@core";

export interface KurseGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
	kurseListeManager: () => KursListeManager;
	deleteKurse: () => Promise<[boolean, List<string | null>]>;
	deleteKurseCheck: () => [boolean, List<string>];
}
