import type { List } from "@core";
import type { FoerderschwerpunkteListeManager } from "@ui";

export interface FoerderschwerpunkteGruppenprozesseProps {
	manager: () => FoerderschwerpunkteListeManager;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	delete: () => Promise<[boolean, List<string | null>]>;
}
