import type { BenutzerKompetenz, KindergaertenListeManager, Kindergarten } from "@core";

export interface KindergaertenDatenProps {
	manager: () => KindergaertenListeManager;
	benutzerKompetenzen: Set<BenutzerKompetenz>
	patch: (data: Partial<Kindergarten>) => Promise<void>;
}
