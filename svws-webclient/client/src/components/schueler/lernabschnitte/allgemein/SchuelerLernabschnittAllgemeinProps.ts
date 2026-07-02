import type { SchuelerLernabschnittsdaten, BenutzerKompetenz } from "@core";
import type { SchuelerListeManager } from "~/states/schueler/SchuelerListeManager";
import type { SchuelerLernabschnittManager } from "../SchuelerLernabschnittManager";

export interface SchuelerLernabschnittAllgemeinProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	benutzerKompetenzenKlassen: Set<number>;
	schuelerListeManager: () => SchuelerListeManager;
	manager: () => SchuelerLernabschnittManager;
	patch: (data: Partial<SchuelerLernabschnittsdaten>) => Promise<void>;
}
