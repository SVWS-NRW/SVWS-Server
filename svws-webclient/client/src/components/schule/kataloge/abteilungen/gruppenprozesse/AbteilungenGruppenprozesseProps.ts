import type { List } from "@core/java/util/List";
import type { AbteilungenListeManager } from "@ui/ui/manager/kataloge/AbteilungenListeManager";

export interface AbteilungenGruppenprozesseProps {
	manager: () => AbteilungenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
}
