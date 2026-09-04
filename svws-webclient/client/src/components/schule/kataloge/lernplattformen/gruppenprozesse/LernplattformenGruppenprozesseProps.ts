import type { List } from "@core/java/util/List";
import type { LernplattformListeManager } from "@ui/ui/manager/kataloge/LernplattformListeManager";

export interface LernplattformenGruppenprozesseProps {
	manager: () => LernplattformListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
}
