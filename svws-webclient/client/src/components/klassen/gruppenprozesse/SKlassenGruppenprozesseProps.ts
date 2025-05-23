import type { Schulform, KlassenListeManager, List, Schulgliederung, BenutzerKompetenz, ApiFile, ReportingParameter, StundenplanListeEintrag } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";

export interface KlassenGruppenprozesseProps {
	apiStatus: ApiStatus;
	getPDF: (parameter: ReportingParameter, idStundenplan: number) => Promise<ApiFile>;
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	schulgliederungen: List<Schulgliederung>;
	manager: () => KlassenListeManager;
	deleteKlassen: () => Promise<[boolean, List<string | null>]>;
}
