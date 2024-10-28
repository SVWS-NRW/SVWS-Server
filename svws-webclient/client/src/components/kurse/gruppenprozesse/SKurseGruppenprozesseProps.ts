import type { Schulform, List, Schulgliederung, KursListeManager } from "@core";

export interface KurseGruppenprozesseProps {
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
	kurseListeManager: () => KursListeManager;
	deleteKurse: () => Promise<[boolean, List<string | null>]>;
	deleteKurseCheck: () => [boolean, List<string>];
}
