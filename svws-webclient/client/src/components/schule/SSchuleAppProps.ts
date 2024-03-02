import type { BenutzerKompetenz, SchuleStammdaten } from "@core";

export interface SchuleAppProps {
	schule: () => SchuleStammdaten;
	patch: (data: Partial<SchuleStammdaten>) => Promise<void>;
	benutzerIstAdmin: boolean;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}