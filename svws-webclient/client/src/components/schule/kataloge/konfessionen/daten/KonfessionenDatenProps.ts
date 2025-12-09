import type { BenutzerKompetenz, ReligionEintrag, Schulform } from "@core";
import type { KonfessionenListeManager } from "@ui";

export interface KonfessionenDatenProps {
	manager: () => KonfessionenListeManager;
	schulform: Schulform;
	patch: (data: Partial<ReligionEintrag>) => Promise<void>;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
}
