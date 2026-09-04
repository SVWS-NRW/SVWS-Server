import type { StundenplanListeEintrag } from "@core/core/data/stundenplan/StundenplanListeEintrag";
import type { List } from "@core/java/util/List";
import type { ApiStatus } from "~/components/ApiStatus";
import type { SchuelerListeManager } from "~/states/schueler/SchuelerListeManager";

export interface SSchuelerAllgemeinesGruppenprozesseProps {
	apiStatus: ApiStatus;
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	schuelerListeManager: () => SchuelerListeManager;
	deleteSchueler: () => Promise<[boolean, List<string | null>]>;
	deleteSchuelerCheck: () => [boolean, List<string>];
}
