import type { List } from "@core";
import type { StundenplanListeManager } from "@ui";

export interface StundenplanGruppenprozesseProps {
	stundenplanListeManager: () => StundenplanListeManager;
	deleteStundenplan: () => Promise<[boolean, List<string | null>]>;
}
