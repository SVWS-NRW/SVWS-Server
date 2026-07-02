import type { BenutzerKompetenz, List, StundenplanListeEintrag } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";
import type { KlassenListeManager } from "~/states/klassen/KlassenListeManager";

export interface KlassenGruppenprozesseProps {
	apiStatus: ApiStatus;
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => KlassenListeManager;
	deleteKlassen: () => Promise<[boolean, List<string | null>]>;
}
