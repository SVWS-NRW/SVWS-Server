import type { BenutzerDaten, BenutzerEMailDaten } from "@core";

export interface BenutzerprofilAppProps {
		benutzer: BenutzerDaten;
		benutzerEMailDaten: BenutzerEMailDaten;
		patchBenutzerEMailDaten: (data: Partial<BenutzerDaten>) => Promise<void>;
		patch: (data: Partial<BenutzerDaten>) => Promise<void>;
		patchPasswort: (eins: string, zwei: string) => Promise<boolean>;
		// Config
		backticks: () => boolean;
		setBackticks: (value: boolean) => void;
	}