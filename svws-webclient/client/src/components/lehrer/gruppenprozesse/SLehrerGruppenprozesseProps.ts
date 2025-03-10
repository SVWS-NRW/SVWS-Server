import type { Schulform, List, Schulgliederung, ServerMode, BenutzerKompetenz, ApiFile, LehrerListeManager, StundenplanListeEintrag } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";

export interface LehrerGruppenprozesseProps {
	apiStatus: ApiStatus;
	getPDF: (title: DownloadPDFTypen, idStundenplan: number) => Promise<ApiFile>;
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	serverMode: ServerMode;
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
	lehrerListeManager: () => LehrerListeManager;
	deleteLehrer: () => Promise<[boolean, List<string | null>]>;
	deleteLehrerCheck: () => [boolean, List<string>];
}

export type DownloadPDFTypen = "Stundenplan" | "Stundenplan mit Pausenaufsichten" | "Stundenplan mit Pausenzeiten";
