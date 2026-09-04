import type { List } from "@core/java/util/List";
import type { FoerderschwerpunkteListeManager } from "@ui/ui/manager/kataloge/FoerderschwerpunkteListeManager";

export interface FoerderschwerpunkteGruppenprozesseProps {
	manager: () => FoerderschwerpunkteListeManager;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	delete: () => Promise<[boolean, List<string | null>]>;
}
