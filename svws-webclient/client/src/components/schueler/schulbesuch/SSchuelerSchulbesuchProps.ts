import type { BenutzerKompetenz, SchuelerListeManager, SchuelerSchulbesuchsdaten, Schulform, ServerMode } from "@core";

export interface SchuelerSchulbesuchProps {
	schulform: Schulform;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	schuelerListeManager: () => SchuelerListeManager;
	data: SchuelerSchulbesuchsdaten;
	patch: (data : Partial<SchuelerSchulbesuchsdaten>) => Promise<void>;
	autofocus: boolean;
}
