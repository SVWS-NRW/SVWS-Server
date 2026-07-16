import type { List } from "@core";
import type { BeschaeftigungsartenListeManager } from "@ui";

export interface BeschaeftigungsartenGruppenprozesseProps {
	manager: () => BeschaeftigungsartenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => [boolean, List<string>];
}
