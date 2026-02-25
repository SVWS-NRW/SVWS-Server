import type { BenutzerKompetenz, SchulEintrag, Schulform } from "@core";
import type { SchulenListeManager } from "@ui";

export interface SchulenDatenProps {
	schuljahr: number;
	patch: (data: Partial<SchulEintrag>) => Promise<boolean>;
	manager: () => SchulenListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	schulform: Schulform;
}
