import type { List } from "@core/java/util/List";
import type { BetriebsartenListeManager } from "@ui/ui/manager/kataloge/BetriebsartenListeManager";

export interface BetriebsartenGruppenprozesseProps {
	manager: () => BetriebsartenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => [boolean, List<string>];
	gotoDefaultView: (id: number | null) => Promise<void>;
}
