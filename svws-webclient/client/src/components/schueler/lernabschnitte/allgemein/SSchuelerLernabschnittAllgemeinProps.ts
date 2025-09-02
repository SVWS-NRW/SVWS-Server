import type { SchuleStammdaten, SchuelerLernabschnittsdaten, ServerMode, BenutzerKompetenz } from "@core";
import type { SchuelerListeManager } from "@ui";
import type { SchuelerLernabschnittManager } from "../SchuelerLernabschnittManager";

export interface SchuelerLernabschnittAllgemeinProps {
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	benutzerKompetenzenKlassen: Set<number>;
	schule: SchuleStammdaten;
	schuelerListeManager: () => SchuelerListeManager;
	manager: () => SchuelerLernabschnittManager;
	patch: (data : Partial<SchuelerLernabschnittsdaten>) => Promise<void>;
}
