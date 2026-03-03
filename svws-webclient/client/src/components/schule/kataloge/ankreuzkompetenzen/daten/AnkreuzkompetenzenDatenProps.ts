import type { Ankreuzkompetenz, AnkreuzkompetenzJahrgangszuordnung, BenutzerKompetenz, List, Schulform } from "@core";
import type { AnkreuzkompetenzenListeManager } from "@ui";

export interface AnkreuzkompetenzenDatenProps {
	patch: (data: Partial<Ankreuzkompetenz>) => Promise<boolean>;
	manager: () => AnkreuzkompetenzenListeManager;
	addJahrgaengezuordnungen: (data: List<AnkreuzkompetenzJahrgangszuordnung>, idAnkreuzkompetenz: number) => Promise<void>;
	deleteJahrgaengezuordnungen: (ids: List<number>) => Promise<void>;
	schuljahr: number;
	schulform: Schulform;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
