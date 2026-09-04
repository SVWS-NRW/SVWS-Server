import type { StundenplanListeEintrag } from "@core/core/data/stundenplan/StundenplanListeEintrag";
import type { List } from "@core/java/util/List";
import type { LehrerListeManager } from "@ui/ui/manager/lehrer/LehrerListeManager";
import type { ApiStatus } from "~/components/ApiStatus";

export interface LehrerAllgemeinesGruppenprozesseProps {
	apiStatus: ApiStatus;
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	lehrerListeManager: () => LehrerListeManager;
	deleteLehrer: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
}
