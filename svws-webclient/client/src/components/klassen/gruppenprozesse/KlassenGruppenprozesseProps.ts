import type { StundenplanListeEintrag } from "@core/core/data/stundenplan/StundenplanListeEintrag";
import type { List } from "@core/java/util/List";
import type { ApiStatus } from "~/components/ApiStatus";
import type { KlassenListeManager } from "~/states/klassen/KlassenListeManager";

export interface KlassenGruppenprozesseProps {
	apiStatus: ApiStatus;
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	manager: () => KlassenListeManager;
	deleteKlassen: () => Promise<[boolean, List<string | null>]>;
}
