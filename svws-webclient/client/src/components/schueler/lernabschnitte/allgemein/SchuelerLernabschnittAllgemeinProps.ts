import type { SchuelerListeManager } from "~/states/schueler/SchuelerListeManager";
import type { SchuelerLernabschnittManager } from "../SchuelerLernabschnittManager";
import type { SchuelerLernabschnittsdaten } from "@core/asd/data/schueler/SchuelerLernabschnittsdaten";

export interface SchuelerLernabschnittAllgemeinProps {
	schuelerListeManager: () => SchuelerListeManager;
	manager: () => SchuelerLernabschnittManager;
	patch: (data: Partial<SchuelerLernabschnittsdaten>) => Promise<void>;
}
