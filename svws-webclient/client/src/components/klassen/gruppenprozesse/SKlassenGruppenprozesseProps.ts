import type { BenutzerKompetenz, List, StundenplanListeEintrag } from "@core";
import type { KlassenListeManager } from "@ui";
import type { ApiStatus } from "~/components/ApiStatus";

export interface KlassenGruppenprozesseProps {
	apiStatus: ApiStatus;
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => KlassenListeManager;
	deleteKlassen: () => Promise<[boolean, List<string | null>]>;
}
