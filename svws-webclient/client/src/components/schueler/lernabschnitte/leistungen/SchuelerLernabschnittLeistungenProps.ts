import type { BenutzerDaten, BenutzerKompetenz, List, SchuelerLeistungsdaten, SchuelerLernabschnittsdaten } from "@core";
import type { SchuelerListeManager } from "@ui";
import type { SchuelerLernabschnittManager } from "../SchuelerLernabschnittManager";

export interface SchuelerLernabschnittLeistungenProps {
	benutzerdaten: BenutzerDaten;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	benutzerKompetenzenKlassen: Set<number>;
	schuelerListeManager: () => SchuelerListeManager;
	manager: () => SchuelerLernabschnittManager;
	patch: (data: Partial<SchuelerLernabschnittsdaten>) => Promise<void>;
	patchLeistung: (data: Partial<SchuelerLeistungsdaten>, id: number) => Promise<void>;
	addLeistung: (fachID: number) => Promise<void>;
	deleteLeistungen: (leistungenIDs: List<number>) => Promise<void>;
}
