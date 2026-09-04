import type { List } from "@core/java/util/List";
import type { OrteListeManager } from "@ui/ui/manager/kataloge/OrteListeManager";

export interface OrteGruppenprozesseProps {
	manager: () => OrteListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
}
