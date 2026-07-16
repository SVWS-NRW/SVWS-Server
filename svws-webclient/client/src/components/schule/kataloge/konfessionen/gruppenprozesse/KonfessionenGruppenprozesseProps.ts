import type { List } from "@core";
import type { KonfessionenListeManager } from "@ui";

export interface KonfessionenGruppenprozesseProps {
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	manager: () => KonfessionenListeManager;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
}
