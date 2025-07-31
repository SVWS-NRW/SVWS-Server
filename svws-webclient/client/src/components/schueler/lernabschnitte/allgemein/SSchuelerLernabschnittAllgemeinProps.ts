import type { SchuleStammdaten, SchuelerLernabschnittsdaten, SchuelerLernabschnittManager, ServerMode, BenutzerKompetenz } from "@core";
import type { SchuelerListeManager } from "@ui";

export interface SchuelerLernabschnittAllgemeinProps {
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	benutzerKompetenzenKlassen: Set<number>;
	schule: SchuleStammdaten;
	schuelerListeManager: () => SchuelerListeManager;
	manager: () => SchuelerLernabschnittManager;
	patch: (data : Partial<SchuelerLernabschnittsdaten>) => Promise<void>;
}
