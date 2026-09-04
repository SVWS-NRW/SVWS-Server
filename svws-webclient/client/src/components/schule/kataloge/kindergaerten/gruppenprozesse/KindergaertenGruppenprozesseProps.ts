import type { List } from "@core/java/util/List";
import type { KindergaertenListeManager } from "@ui/ui/manager/kataloge/KindergaertenListeManager";

export interface KindergaertenGruppenprozesseProps {
	manager: () => KindergaertenListeManager;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	delete: () => Promise<[boolean, List<string | null>]>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
}
