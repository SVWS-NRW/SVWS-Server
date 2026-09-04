import type { List } from "@core/java/util/List";
import type { EinwilligungsartenListeManager } from "@ui/ui/manager/kataloge/EinwilligungsartenListeManager";

export interface EinwilligungsartenGruppenprozesseProps {
	manager: () => EinwilligungsartenListeManager;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	delete: () => Promise<[boolean, List<string | null>]>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
}
