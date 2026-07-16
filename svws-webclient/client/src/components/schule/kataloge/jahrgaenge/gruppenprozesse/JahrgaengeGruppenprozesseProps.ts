import type { List } from "@core";
import type { JahrgaengeListeManager } from "@ui";

export interface JahrgaengeGruppenprozesseProps {
	manager: () => JahrgaengeListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
}
