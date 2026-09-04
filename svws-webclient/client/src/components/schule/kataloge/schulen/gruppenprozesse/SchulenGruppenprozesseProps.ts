import type { List } from "@core/java/util/List";
import type { SchulenListeManager } from "@ui/ui/manager/kataloge/SchulenListeManager";

export interface SchulenGruppenprozesseProps {
	manager: () => SchulenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => [boolean, List<string>];
}
