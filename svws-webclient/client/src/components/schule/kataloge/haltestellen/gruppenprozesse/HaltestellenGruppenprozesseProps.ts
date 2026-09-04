import type { List } from "@core/java/util/List";
import type { HaltestellenListeManager } from "@ui/ui/manager/kataloge/HaltestellenListeManager";

export interface HaltestellenGruppenprozesseProps {
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	manager: () => HaltestellenListeManager;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
}
