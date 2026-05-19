import type { List, BenutzerKompetenz, ApiFile, ReportingParameter, StundenplanListeEintrag, SimpleOperationResponse } from "@core";
import type { LehrerListeManager } from "@ui";
import type { ApiStatus } from "~/components/ApiStatus";

export interface LehrerAllgemeinesGruppenprozesseProps {
	apiStatus: ApiStatus;
	getPDF: (parameter: ReportingParameter) => Promise<ApiFile>;
	sendEMail: (parameter: ReportingParameter) => Promise<SimpleOperationResponse>;
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	lehrerListeManager: () => LehrerListeManager;
	deleteLehrer: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => { success: boolean, logs: Iterable<string> };
}
