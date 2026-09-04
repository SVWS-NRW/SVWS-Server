import type { List } from "@core/java/util/List";
import type { FloskelnListeManager } from "@ui/ui/manager/kataloge/FloskelnListeManager";

export interface FloskelnGruppenprozesseProps {
	manager: () => FloskelnListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
}
