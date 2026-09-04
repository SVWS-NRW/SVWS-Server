import type { List } from "@core/java/util/List";
import type { SchwerpunkteListeManager } from "@ui/ui/manager/kataloge/SchwerpunkteListeManager";

export interface SchwerpunkteGruppenprozesseProps {
	manager: () => SchwerpunkteListeManager;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	delete: () => Promise<[boolean, List<string | null>]>;
	gotoDefaultView: (id: number | null) => Promise<void>;
}
