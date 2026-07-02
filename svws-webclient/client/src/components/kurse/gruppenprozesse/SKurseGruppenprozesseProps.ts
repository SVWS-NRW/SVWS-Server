import type { List, BenutzerKompetenz } from "@core";
import type { KursListeManager } from "~/states/kurse/KursListeManager";
import type { ApiStatus } from "~/components/ApiStatus";

export interface KurseGruppenprozesseProps {
	apiStatus: ApiStatus;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => KursListeManager;
	deleteKurse: () => Promise<[boolean, List<string | null>]>;
}
