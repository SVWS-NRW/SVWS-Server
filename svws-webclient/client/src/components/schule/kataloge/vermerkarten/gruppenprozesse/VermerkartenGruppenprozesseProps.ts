import type { List } from "@core/java/util/List";
import type { VermerkartenListeManager } from "@ui/ui/manager/kataloge/VermerkartenListeManager";

export interface VermerkartenGruppenprozesseProps {
	manager: () => VermerkartenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
}
