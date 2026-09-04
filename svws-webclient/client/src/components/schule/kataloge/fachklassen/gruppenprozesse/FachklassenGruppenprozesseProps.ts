import type { List } from "@core/java/util/List";
import type { FachklassenListeManager } from "@ui/ui/manager/kataloge/FachklassenListeManager";

export interface FachklassenGruppenprozesseProps {
	manager: () => FachklassenListeManager;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	delete: () => Promise<[boolean, List<string | null>]>;
	gotoDefaultView: (id: number | null) => Promise<void>;
}
