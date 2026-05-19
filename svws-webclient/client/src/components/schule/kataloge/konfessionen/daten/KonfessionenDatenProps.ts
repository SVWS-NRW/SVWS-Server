import type { BenutzerKompetenz, ReligionEintrag } from "@core";
import type { KonfessionenListeManager } from "@ui";

export interface KonfessionenDatenProps {
	manager: () => KonfessionenListeManager;
	patch: (data: Partial<ReligionEintrag>) => Promise<boolean>;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
}
