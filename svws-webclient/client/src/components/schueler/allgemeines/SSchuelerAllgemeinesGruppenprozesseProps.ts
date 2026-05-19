import type { List, BenutzerKompetenz, ApiFile, ReportingParameter, StundenplanListeEintrag, SimpleOperationResponse } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";
import type { SchuelerListeManager } from "@ui";

export interface SSchuelerAllgemeinesGruppenprozesseProps {
	apiStatus: ApiStatus;
	getPDF: (parameter: ReportingParameter) => Promise<ApiFile>;
	sendEMail: (parameter: ReportingParameter) => Promise<SimpleOperationResponse>;
	fetchEMailJobStatus: (jobId: number) => Promise<SimpleOperationResponse>;
	fetchEMailJobLog: (jobId: number) => Promise<SimpleOperationResponse>;
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	schuelerListeManager: () => SchuelerListeManager;
	deleteSchueler: () => Promise<[boolean, List<string | null>]>;
	deleteSchuelerCheck: () => [boolean, List<string>];
}
