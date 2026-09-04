import type { List } from "@core/java/util/List";
import type { EntlassgruendeListeManager } from "@ui/ui/manager/kataloge/EntlassgruendeListeManager";

export interface EntlassgruendeGruppenprozesseProps {
	manager: () => EntlassgruendeListeManager;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	delete: () => Promise<[boolean, List<string | null>]>;
}
