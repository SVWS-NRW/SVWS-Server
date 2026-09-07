import type { SchuelerLernabschnittsdaten } from "@core/asd/data/schueler/SchuelerLernabschnittsdaten";

import type { SchuelerLernabschnittManager } from "../SchuelerLernabschnittManager";
import type { SchuelerListeManager } from "~/states/schueler/SchuelerListeManager";

export interface SchuelerLernabschnittAllgemeinProps {
	schuelerListeManager: () => SchuelerListeManager;
	manager: () => SchuelerLernabschnittManager;
	patch: (data: Partial<SchuelerLernabschnittsdaten>) => Promise<void>;
}
