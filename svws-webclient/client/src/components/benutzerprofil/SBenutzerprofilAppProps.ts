import type { BenutzerDaten, BenutzerEMailDaten, ServerMode } from "@core";
import type { AES } from "~/utils/crypto/aes";

export interface BenutzerprofilAppProps {
	mode: ServerMode;
	benutzer: () => BenutzerDaten;
	benutzerEMailDaten: () => BenutzerEMailDaten;
	patchBenutzerEMailDaten: (data: Partial<BenutzerEMailDaten>) => Promise<void>;
	patch: (data: Partial<BenutzerDaten>) => Promise<void>;
	patchPasswort: (eins: string, zwei: string) => Promise<boolean>;
	aes: AES;
}
