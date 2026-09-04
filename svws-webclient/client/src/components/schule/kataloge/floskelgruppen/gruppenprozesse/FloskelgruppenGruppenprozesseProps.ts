import type { List } from "@core/java/util/List";
import type { FloskelgruppenListeManager } from "@ui/ui/manager/kataloge/FloskelgruppenListeManager";

export interface FloskelgruppenGruppenprozesseProps {
	manager: () => FloskelgruppenListeManager;
	deleteCheck: () => [boolean, List<string>];
	delete: () => Promise<[boolean, List<string | null>]>;
}
