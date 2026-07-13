import type { BenutzerKompetenz, FachklasseEintrag } from "@core";
import type { FachklassenListeManager } from "@ui";

export interface FachklassenDatenProps {
	patch: (data: Partial<FachklasseEintrag>) => Promise<boolean>;
	manager: () => FachklassenListeManager,
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
