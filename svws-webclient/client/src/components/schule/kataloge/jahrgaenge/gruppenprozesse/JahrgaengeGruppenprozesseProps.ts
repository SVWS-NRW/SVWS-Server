import type { List } from "@core/java/util/List";
import type { JahrgaengeListeManager } from "@ui/ui/manager/kataloge/JahrgaengeListeManager";

export interface JahrgaengeGruppenprozesseProps {
	manager: () => JahrgaengeListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
}
