import type { List } from "@core";
import type { ErzieherartListeManager } from "@ui";

export interface ErzieherartenGruppenprozesseProps {
	manager: () => ErzieherartListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
}
