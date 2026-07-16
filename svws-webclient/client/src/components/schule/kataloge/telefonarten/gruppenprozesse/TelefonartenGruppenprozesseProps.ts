import type { List } from "@core";
import type { TelefonartenListeManager } from "@ui";

export interface TelefonartenGruppenprozesseProps {
	manager: () => TelefonartenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => [boolean, List<string>];
	gotoDefaultView: (eintrag?: number | null) => Promise<void>;
}
