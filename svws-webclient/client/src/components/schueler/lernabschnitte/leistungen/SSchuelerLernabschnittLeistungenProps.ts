import type { BenutzerDaten, BenutzerKompetenz, List, SchuelerLeistungsdaten, SchuelerLernabschnittManager, SchuelerLernabschnittsdaten, SchuelerListeManager, SchuleStammdaten, Schulform, ServerMode } from "@core";

export interface SchuelerLernabschnittLeistungenProps {
	serverMode: ServerMode;
	benutzerdaten: BenutzerDaten;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	benutzerKompetenzenKlassen: Set<number>;
	schule: SchuleStammdaten;
	schulform: Schulform;
	schuelerListeManager: () => SchuelerListeManager;
	manager: () => SchuelerLernabschnittManager;
	patch: (data : Partial<SchuelerLernabschnittsdaten>) => Promise<void>;
	patchLeistung: (data : Partial<SchuelerLeistungsdaten>, id : number) => Promise<void>;
	addLeistung: (fachID : number) => Promise<void>;
	deleteLeistungen: (leistungenIDs: List<number>) => Promise<void>;
}