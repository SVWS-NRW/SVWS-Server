import type { List, BenutzerKompetenz, ApiFile, ReportingParameter } from "@core";
import type { FaecherListeManager } from "@ui";

export interface FaecherGruppenprozesseProps {
	getPDF: (parameter: ReportingParameter) => Promise<ApiFile>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	manager: () => FaecherListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => [boolean, List<string>];
	sortFaecher: () => Promise<void>;
}
