import type { List } from "@core";
import type { FloskelnListeManager } from "@ui";

export interface FloskelnGruppenprozesseProps {
	manager: () => FloskelnListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
}
