import type { BenutzerKompetenz, SchuleStammdaten, SMTPServerKonfiguration } from "@core";

export interface SchuleAdressdatenProps {
	schule: () => SchuleStammdaten;
	patch: (data: Partial<SchuleStammdaten>) => Promise<void>;
	smptServerKonfiguration: () => SMTPServerKonfiguration;
	patchSMTPServerKonfiguration: (data: Partial<SMTPServerKonfiguration>) => Promise<void>;
	benutzerIstAdmin: boolean;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
