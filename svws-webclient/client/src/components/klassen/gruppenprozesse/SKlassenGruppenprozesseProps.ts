import type { List, BenutzerKompetenz, ApiFile, ReportingParameter, StundenplanListeEintrag, SimpleOperationResponse } from "@core";
import type { KlassenListeManager } from "@ui";
import type { ApiStatus } from "~/components/ApiStatus";

export interface KlassenGruppenprozesseProps {
	apiStatus: ApiStatus;
	getPDF: (parameter: ReportingParameter) => Promise<ApiFile>;
	sendEMail: (parameter: ReportingParameter) => Promise<SimpleOperationResponse>;
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => KlassenListeManager;
	deleteKlassen: () => Promise<[boolean, List<string | null>]>;
}
