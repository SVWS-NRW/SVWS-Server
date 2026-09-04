import type { List } from "@core/java/util/List";
import type { BetriebeListeManager } from "@ui/ui/manager/kataloge/BetriebeListeManager";

export interface BetriebeGruppenprozesseProps {
	manager: () => BetriebeListeManager;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	delete: () => Promise<[boolean, List<string | null>]>;
}
