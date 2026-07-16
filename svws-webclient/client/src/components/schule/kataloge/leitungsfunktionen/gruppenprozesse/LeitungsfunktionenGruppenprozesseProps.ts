import type { List } from "@core";
import type { LeitungsfunktionenListeManager } from "@ui";

export interface LeitungsfunktionenGruppenprozesseProps {
	manager: () => LeitungsfunktionenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
}
