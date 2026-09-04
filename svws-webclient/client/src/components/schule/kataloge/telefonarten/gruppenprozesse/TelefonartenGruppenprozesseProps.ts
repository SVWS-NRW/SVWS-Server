import type { List } from "@core/java/util/List";
import type { TelefonartenListeManager } from "@ui/ui/manager/kataloge/TelefonartenListeManager";

export interface TelefonartenGruppenprozesseProps {
	manager: () => TelefonartenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => [boolean, List<string>];
	gotoDefaultView: (eintrag?: number | null) => Promise<void>;
}
