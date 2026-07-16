import type { List } from "@core";
import type { KindergaertenListeManager } from "@ui";

export interface KindergaertenGruppenprozesseProps {
	manager: () => KindergaertenListeManager;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	delete: () => Promise<[boolean, List<string | null>]>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
}
