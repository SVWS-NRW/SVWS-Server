import type { BenutzerDaten, BenutzerKompetenz, List, SchuelerLeistungsdaten, SchuelerLernabschnittManager, SchuelerLernabschnittsdaten, SchuleStammdaten, Schulform, Schuljahresabschnitt, ServerMode } from "@core";
import type { SchuelerListeManager } from "@ui";

export interface SchuelerLernabschnittLeistungenProps {
	serverMode: ServerMode;
	benutzerdaten: BenutzerDaten;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	benutzerKompetenzenKlassen: Set<number>;
	schule: SchuleStammdaten;
	schulform: Schulform;
	schuleSchuljahresabschnitt: () => Schuljahresabschnitt;
	schuelerListeManager: () => SchuelerListeManager;
	manager: () => SchuelerLernabschnittManager;
	patch: (data : Partial<SchuelerLernabschnittsdaten>) => Promise<void>;
	patchLeistung: (data : Partial<SchuelerLeistungsdaten>, id : number) => Promise<void>;
	addLeistung: (fachID : number) => Promise<void>;
	deleteLeistungen: (leistungenIDs: List<number>) => Promise<void>;
}
