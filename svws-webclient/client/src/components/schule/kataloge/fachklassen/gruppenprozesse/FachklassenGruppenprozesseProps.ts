import type { List } from "@core";
import type { FachklassenListeManager } from "@ui";

export interface FachklassenGruppenprozesseProps {
	manager: () => FachklassenListeManager;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	delete: () => Promise<[boolean, List<string | null>]>;
	gotoDefaultView: (id: number | null) => Promise<void>;
}
