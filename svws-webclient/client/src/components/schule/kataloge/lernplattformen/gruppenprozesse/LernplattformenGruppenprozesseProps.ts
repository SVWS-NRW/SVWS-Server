import type { List } from "@core";
import type { LernplattformListeManager } from "@ui";

export interface LernplattformenGruppenprozesseProps {
	manager: () => LernplattformListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
}
