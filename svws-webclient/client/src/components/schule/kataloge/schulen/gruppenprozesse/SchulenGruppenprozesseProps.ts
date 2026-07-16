import type { List } from "@core";
import type { SchulenListeManager } from "@ui";

export interface SchulenGruppenprozesseProps {
	manager: () => SchulenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => [boolean, List<string>];
}
