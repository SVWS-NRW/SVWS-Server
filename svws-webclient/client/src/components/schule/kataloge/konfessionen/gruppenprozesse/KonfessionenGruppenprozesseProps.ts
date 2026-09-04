import type { List } from "@core/java/util/List";
import type { KonfessionenListeManager } from "@ui/ui/manager/kataloge/KonfessionenListeManager";

export interface KonfessionenGruppenprozesseProps {
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	manager: () => KonfessionenListeManager;
	gotoDefaultView: (eintragId?: number | null) => Promise<void>;
}
