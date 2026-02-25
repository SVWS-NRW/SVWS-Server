import type { Ankreuzkompetenz, BenutzerKompetenz } from "@core";
import type { AnkreuzkompetenzenListeManager } from "@ui";

export interface AnkreuzkompetenzenDatenProps {
	patch: (data: Partial<Ankreuzkompetenz>) => Promise<void>;
	manager: () => AnkreuzkompetenzenListeManager,
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
