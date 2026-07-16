import type { List } from "@core";
import type { KursListeManager } from "~/states/kurse/KursListeManager";
import type { ApiStatus } from "~/components/ApiStatus";

export interface KurseGruppenprozesseProps {
	apiStatus: ApiStatus;
	manager: () => KursListeManager;
	deleteKurse: () => Promise<[boolean, List<string | null>]>;
}
