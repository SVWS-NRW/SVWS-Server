import type { List } from "@core/java/util/List";
import type { FahrschuelerartenListeManager } from "@ui/ui/manager/kataloge/FahrschuelerartenListeManager";

export interface FahrschuelerartenGruppenprozesseProps {
	manager: () => FahrschuelerartenListeManager;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	delete: () => Promise<[boolean, List<string | null>]>;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
}
