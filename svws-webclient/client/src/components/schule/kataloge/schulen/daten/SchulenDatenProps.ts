import type { BenutzerKompetenz, SchulEintrag, Schulform } from "@core";
import type { SchulenListeManager } from "@ui";

export interface SchulenDatenProps {
	schuljahr: number;
	patch: (data: Partial<SchulEintrag>) => Promise<void>;
	manager: () => SchulenListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	schulform: Schulform;
}
