import type { Ankreuzkompetenz, BenutzerKompetenz, List, Schulform } from "@core";
import type { AnkreuzkompetenzenListeManager } from "@ui";

export interface AnkreuzkompetenzenDatenProps {
	patch: (data: Partial<Ankreuzkompetenz>) => Promise<boolean>;
	manager: () => AnkreuzkompetenzenListeManager;
	addJahrgaengezuordnungen: (idAnkreuzkompetenz: number, idsJahrgaenge: List<number>) => Promise<void>;
	deleteJahrgaengezuordnungen: (ids: List<number>) => Promise<void>;
	schuljahr: number;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
