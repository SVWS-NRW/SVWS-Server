import type { List } from "@core/java/util/List";
import type { OrtsteileListeManager } from "@ui/ui/manager/kataloge/OrtsteileListeManager";

export interface OrtsteileGruppenprozesseProps {
	manager: () => OrtsteileListeManager;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	delete: () => Promise<[boolean, List<string | null>]>;
	goToDefaultView: (eintragId?: number | null) => Promise<void>;
}
