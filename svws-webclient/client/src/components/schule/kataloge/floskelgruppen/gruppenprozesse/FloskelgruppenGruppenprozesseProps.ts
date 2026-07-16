import type { List } from "@core";
import type { FloskelgruppenListeManager } from "@ui";

export interface FloskelgruppenGruppenprozesseProps {
	manager: () => FloskelgruppenListeManager;
	deleteCheck: () => [boolean, List<string>];
	delete: () => Promise<[boolean, List<string | null>]>;
}
