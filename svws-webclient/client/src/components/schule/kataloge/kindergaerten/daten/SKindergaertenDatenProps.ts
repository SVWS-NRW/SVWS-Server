import type { BenutzerKompetenz, Kindergarten } from "@core";
import type { KindergaertenListeManager } from "@ui";

export interface KindergaertenDatenProps {
	manager: () => KindergaertenListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>
	patch: (data: Partial<Kindergarten>) => Promise<void>;
}
