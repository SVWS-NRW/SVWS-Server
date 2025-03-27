import type { Schulform, List, Schulgliederung, FachListeManager, ServerMode, BenutzerKompetenz, ApiFile, ReportingParameter, StundenplanListeEintrag } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";

export interface SchuleFachGruppenprozesseProps {
	apiStatus: ApiStatus;
	getPDF: (parameter: ReportingParameter, idStundenplan: number) => Promise<ApiFile>;
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	serverMode: ServerMode;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	schulgliederungen: List<Schulgliederung>;
	manager: () => FachListeManager;
	deleteFaecher: () => Promise<[boolean, List<string | null>]>;
	deleteFaecherCheck: () => [boolean, List<string>];
}
