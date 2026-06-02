import type { List, BenutzerKompetenz } from "@core";
import type { KursListeManager } from "@ui";
import type { ApiStatus } from "~/components/ApiStatus";

export interface KurseGruppenprozesseProps {
	apiStatus: ApiStatus;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => KursListeManager;
	deleteKurse: () => Promise<[boolean, List<string | null>]>;
}
