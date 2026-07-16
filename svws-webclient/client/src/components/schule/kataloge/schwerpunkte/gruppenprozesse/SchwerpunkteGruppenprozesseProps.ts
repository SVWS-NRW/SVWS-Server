import type { List } from "@core";
import type { SchwerpunkteListeManager } from "@ui";


export interface SchwerpunkteGruppenprozesseProps {
	manager: () => SchwerpunkteListeManager;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
	delete: () => Promise<[boolean, List<string | null>]>;
	gotoDefaultView: (id: number | null) => Promise<void>;
}
