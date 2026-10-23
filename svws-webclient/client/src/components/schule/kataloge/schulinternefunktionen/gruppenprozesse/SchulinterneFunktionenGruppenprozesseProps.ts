import type { List } from "@core/java/util/List";
import type { SchulinterneFunktionenListeManager } from "@ui/ui/manager/kataloge/SchulinterneFunktionenListeManager";

export interface SchulinterneFunktionenGruppenprozesseProps {
	manager: () => SchulinterneFunktionenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
}
