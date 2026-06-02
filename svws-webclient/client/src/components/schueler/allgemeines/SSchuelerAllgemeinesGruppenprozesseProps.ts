import type { List, BenutzerKompetenz, StundenplanListeEintrag } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";
import type { SchuelerListeManager } from "@ui";

export interface SSchuelerAllgemeinesGruppenprozesseProps {
	apiStatus: ApiStatus;
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	schuelerListeManager: () => SchuelerListeManager;
	deleteSchueler: () => Promise<[boolean, List<string | null>]>;
	deleteSchuelerCheck: () => [boolean, List<string>];
}
