import type { List, BenutzerKompetenz, ReportingParameter, ApiFile, SimpleOperationResponse } from "@core";
import type { KursListeManager } from "@ui";
import type { ApiStatus } from "~/components/ApiStatus";

export interface KurseGruppenprozesseProps {
	apiStatus: ApiStatus;
	getPDF: (parameter: ReportingParameter) => Promise<ApiFile>;
	sendEMail: (parameter: ReportingParameter) => Promise<SimpleOperationResponse>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => KursListeManager;
	deleteKurse: () => Promise<[boolean, List<string | null>]>;
}
