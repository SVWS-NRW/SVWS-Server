import type { List, ServerMode, BenutzerKompetenz, ApiFile, ReportingParameter, StundenplanListeEintrag } from "@core";
import type { FachListeManager } from "@ui";

export interface FaecherGruppenprozesseProps {
	getPDF: (parameter: ReportingParameter, idStundenplan: number) => Promise<ApiFile>;
	stundenplaeneById: Map<number, StundenplanListeEintrag>;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => FachListeManager;
	deleteFaecher: () => Promise<[boolean, List<string | null>]>;
	deleteFaecherCheck: () => [boolean, List<string>];
}
