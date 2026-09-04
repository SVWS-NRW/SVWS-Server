import type { List } from "@core/java/util/List";
import type { StundenplanListeManager } from "@ui/ui/manager/stundenplan/StundenplanListeManager";

export interface StundenplanGruppenprozesseProps {
	stundenplanListeManager: () => StundenplanListeManager;
	deleteStundenplan: () => Promise<[boolean, List<string | null>]>;
}
