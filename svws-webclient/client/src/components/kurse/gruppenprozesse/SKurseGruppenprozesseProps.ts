import type { KursListeManager } from "~/states/kurse/KursListeManager";
import type { ApiStatus } from "~/components/ApiStatus";
import type { List } from "@core/java/util/List";

export interface KurseGruppenprozesseProps {
	apiStatus: ApiStatus;
	manager: () => KursListeManager;
	deleteKurse: () => Promise<[boolean, List<string | null>]>;
}
