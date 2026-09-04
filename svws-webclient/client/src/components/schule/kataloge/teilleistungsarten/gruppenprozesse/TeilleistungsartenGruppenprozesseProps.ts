import type { List } from "@core/java/util/List";
import type { TeilleistungsartenListeManager } from "~/states/teilleistungsarten/TeilleistungsartenListeManager";

export interface TeilleistungsartenGruppenprozesseProps {
	manager: () => TeilleistungsartenListeManager;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	delete: () => Promise<[boolean, List<string | null>]>;
	gotoDefaultView: (id: number | null) => Promise<void>;
}
