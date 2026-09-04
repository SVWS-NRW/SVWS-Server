import type { List } from "@core/java/util/List";
import type { LeitungsfunktionenListeManager } from "@ui/ui/manager/kataloge/LeitungsfunktionenListeManager";

export interface LeitungsfunktionenGruppenprozesseProps {
	manager: () => LeitungsfunktionenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
}
