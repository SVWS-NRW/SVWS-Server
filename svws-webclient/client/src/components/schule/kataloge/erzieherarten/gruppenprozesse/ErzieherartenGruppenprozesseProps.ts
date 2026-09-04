import type { List } from "@core/java/util/List";
import type { ErzieherartListeManager } from "@ui/ui/manager/kataloge/ErzieherartListeManager";

export interface ErzieherartenGruppenprozesseProps {
	manager: () => ErzieherartListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
}
