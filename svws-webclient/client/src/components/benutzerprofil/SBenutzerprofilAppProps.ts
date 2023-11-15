import type { BenutzerDaten } from "@core";

export interface BenutzerprofilAppProps {
		benutzer: BenutzerDaten;
		patch: (data: Partial<BenutzerDaten>) => Promise<void>;
		patchPasswort: (eins: string, zwei: string) => Promise<boolean>;
	}