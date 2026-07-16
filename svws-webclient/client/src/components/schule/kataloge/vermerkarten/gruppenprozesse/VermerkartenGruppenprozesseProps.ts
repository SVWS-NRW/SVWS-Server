import type { List } from "@core";
import type { VermerkartenListeManager } from "@ui";

export interface VermerkartenGruppenprozesseProps {
	manager: () => VermerkartenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
}
