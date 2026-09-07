import type { List } from "@core/java/util/List";

import type { ApiStatus } from "~/components/ApiStatus";
import type { KursListeManager } from "~/states/kurse/KursListeManager";

export interface KurseGruppenprozesseProps {
	apiStatus: ApiStatus;
	manager: () => KursListeManager;
	deleteKurse: () => Promise<[boolean, List<string | null>]>;
}
