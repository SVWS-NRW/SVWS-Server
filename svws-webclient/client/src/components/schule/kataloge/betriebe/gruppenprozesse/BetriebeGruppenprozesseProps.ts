import type { List } from "@core";
import type { BetriebeListeManager } from "@ui";

export interface BetriebeGruppenprozesseProps {
	manager: () => BetriebeListeManager;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	delete: () => Promise<[boolean, List<string | null>]>;
}
