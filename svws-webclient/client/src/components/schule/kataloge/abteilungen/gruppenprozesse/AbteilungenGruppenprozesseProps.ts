import type { List } from "@core";
import type { AbteilungenListeManager } from "@ui";

export interface AbteilungenGruppenprozesseProps {
	manager: () => AbteilungenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
}
