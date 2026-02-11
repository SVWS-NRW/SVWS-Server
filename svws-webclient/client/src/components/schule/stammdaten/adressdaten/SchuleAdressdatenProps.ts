import type { BenutzerKompetenz, SchuleStammdaten } from "@core";

export interface SchuleAdressdatenProps {
	schule: () => SchuleStammdaten;
	patch: (data: Partial<SchuleStammdaten>) => Promise<void>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	benutzerIstAdmin: boolean;
}
