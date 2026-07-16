import type { List } from "@core";
import type { EntlassgruendeListeManager } from "@ui";

export interface EntlassgruendeGruppenprozesseProps {
	manager: () => EntlassgruendeListeManager;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	delete: () => Promise<[boolean, List<string | null>]>;
}
