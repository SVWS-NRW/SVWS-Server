import type { BenutzerKompetenz, ReligionEintrag } from "@core";
import type { ReligionenListeManager } from "@ui";

export interface ReligionenDatenProps {
	manager: () => ReligionenListeManager;
	patch: (data : Partial<ReligionEintrag>) => Promise<void>;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
}
