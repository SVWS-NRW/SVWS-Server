import type { List } from "@core/java/util/List";
import type { FaecherListeManager } from "@ui/ui/manager/kataloge/FaecherListeManager";

export interface FaecherGruppenprozesseProps {
	manager: () => FaecherListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => [boolean, List<string>];
	sortFaecher: () => Promise<void>;
}
