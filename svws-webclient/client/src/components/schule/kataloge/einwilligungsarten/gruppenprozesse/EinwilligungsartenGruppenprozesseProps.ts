import type { List } from "@core";
import type { EinwilligungsartenListeManager } from "@ui";

export interface EinwilligungsartenGruppenprozesseProps {
	manager: () => EinwilligungsartenListeManager;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	delete: () => Promise<[boolean, List<string | null>]>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
}
