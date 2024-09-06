import type { BenutzerKompetenz, SchuelerSchulbesuchsdaten, Schulform, ServerMode } from "@core";

export interface SchuelerSchulbesuchProps {
	schulform: Schulform;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	data: SchuelerSchulbesuchsdaten;
	patch: (data : Partial<SchuelerSchulbesuchsdaten>) => Promise<void>;
	autofocus: boolean;
}
