import type { BenutzerDaten, BenutzerEMailDaten, BenutzerTyp, ServerMode } from "@core";
import type { AES } from "~/utils/crypto/aes";

export interface NutzereinstellungenAppProps {
	mode: ServerMode;
	benutzer: () => BenutzerDaten;
	benutzertyp: BenutzerTyp;
	benutzerEMailDaten: () => BenutzerEMailDaten;
	patchBenutzerEMailDaten: (data: Partial<BenutzerEMailDaten>) => Promise<void>;
	patch: (data: Partial<BenutzerDaten>) => Promise<void>;
	patchPasswort: (eins: string, zwei: string) => Promise<boolean>;
	patchPasswortWenom: (eins: string, zwei: string) => Promise<boolean>;
	resetPasswordWenom: () => Promise<boolean>;
	aes: AES;
}
