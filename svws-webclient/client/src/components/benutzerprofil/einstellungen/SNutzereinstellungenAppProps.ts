import type { BenutzerDaten, BenutzerEMailDaten, BenutzerTyp } from "@core";
import type { AES } from "~/utils/crypto/aes";

export interface NutzereinstellungenAppProps {
	benutzer: () => BenutzerDaten;
	benutzertyp: BenutzerTyp;
	benutzerEMailDaten: () => BenutzerEMailDaten;
	patchBenutzerEMailDaten: (data: Partial<BenutzerEMailDaten>) => Promise<void>;
	patch: (data: Partial<BenutzerDaten>) => Promise<void>;
	patchPasswort: (eins: string, zwei: string) => Promise<boolean>;
	resetPasswordWenom: () => Promise<boolean>;
	wenomInitialkennwort: () => string;
	getWenomInitialkennwort: () => Promise<void>;
	aes: AES;
}
