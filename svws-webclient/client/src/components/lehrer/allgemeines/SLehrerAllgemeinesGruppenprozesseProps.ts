import type { List, BenutzerKompetenz, ApiFile, ReportingParameter, StundenplanListeEintrag, LehrerListeManager } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";

export interface SLehrerAllgemeinesGruppenprozesseProps {
	apiStatus: ApiStatus;
	getPDF: (parameter: ReportingParameter, idStundenplan: number) => Promise<ApiFile>;
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	lehrerListeManager: () => LehrerListeManager;
	deleteLehrer: () => Promise<[boolean, List<string | null>]>;
	deleteLehrerCheck: () => { success: boolean, logs: List<string> };
}
