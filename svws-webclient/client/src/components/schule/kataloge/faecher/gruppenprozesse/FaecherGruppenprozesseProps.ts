import type { List } from "@core";
import type { FaecherListeManager } from "@ui";

export interface FaecherGruppenprozesseProps {
	manager: () => FaecherListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => [boolean, List<string>];
	sortFaecher: () => Promise<void>;
}
