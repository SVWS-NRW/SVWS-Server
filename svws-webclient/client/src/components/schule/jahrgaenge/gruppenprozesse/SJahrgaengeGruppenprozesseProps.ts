import type{ Schulform, List, Schulgliederung, ServerMode, JahrgaengeListeManager } from "@core";

export interface SchuleJahrgangGruppenprozesseProps {
	serverMode: ServerMode;
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
	manager: () => JahrgaengeListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
}
