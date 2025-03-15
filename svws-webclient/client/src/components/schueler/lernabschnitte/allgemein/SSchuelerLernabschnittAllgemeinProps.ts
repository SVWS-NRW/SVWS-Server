import type { SchuleStammdaten, SchuelerLernabschnittsdaten, SchuelerLernabschnittManager, ServerMode, BenutzerKompetenz, SchuelerListeManager, Schulform } from "@core";

export interface SchuelerLernabschnittAllgemeinProps {
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	benutzerKompetenzenKlassen: Set<number>;
	schule: SchuleStammdaten;
	schuelerListeManager: () => SchuelerListeManager;
	manager: () => SchuelerLernabschnittManager;
	patch: (data : Partial<SchuelerLernabschnittsdaten>) => Promise<void>;
}
