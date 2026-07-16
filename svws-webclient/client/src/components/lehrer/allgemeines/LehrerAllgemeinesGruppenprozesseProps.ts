import type { List, StundenplanListeEintrag } from "@core";
import type { LehrerListeManager } from "@ui";
import type { ApiStatus } from "~/components/ApiStatus";

export interface LehrerAllgemeinesGruppenprozesseProps {
	apiStatus: ApiStatus;
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	lehrerListeManager: () => LehrerListeManager;
	deleteLehrer: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
}
