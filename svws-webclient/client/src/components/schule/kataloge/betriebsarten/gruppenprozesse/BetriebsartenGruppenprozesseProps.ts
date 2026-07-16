import type { List } from "@core";
import type { BetriebsartenListeManager } from "@ui";

export interface BetriebsartenGruppenprozesseProps {
	manager: () => BetriebsartenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => [boolean, List<string>];
	gotoDefaultView: (id: number | null) => Promise<void>;
}
