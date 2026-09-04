import type { List } from "@core/java/util/List";
import type { BeschaeftigungsartenListeManager } from "@ui/ui/manager/kataloge/BeschaeftigungsartenListeManager";

export interface BeschaeftigungsartenGruppenprozesseProps {
	manager: () => BeschaeftigungsartenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => [boolean, List<string>];
}
