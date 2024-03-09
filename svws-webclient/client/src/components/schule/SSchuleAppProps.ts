import type { BenutzerKompetenz, SMTPServerKonfiguration, SchuleStammdaten } from "@core";

export interface SchuleAppProps {
	schule: () => SchuleStammdaten;
	patch: (data: Partial<SchuleStammdaten>) => Promise<void>;
	smptServerKonfiguration: () => SMTPServerKonfiguration;
	patchSMTPServerKonfiguration: (data: Partial<SMTPServerKonfiguration>) => Promise<void>;
	benutzerIstAdmin: boolean;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}