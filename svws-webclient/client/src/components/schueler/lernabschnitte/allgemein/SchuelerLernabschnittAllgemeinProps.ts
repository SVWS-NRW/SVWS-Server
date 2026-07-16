import type { SchuelerLernabschnittsdaten } from "@core";
import type { SchuelerListeManager } from "~/states/schueler/SchuelerListeManager";
import type { SchuelerLernabschnittManager } from "../SchuelerLernabschnittManager";

export interface SchuelerLernabschnittAllgemeinProps {
	schuelerListeManager: () => SchuelerListeManager;
	manager: () => SchuelerLernabschnittManager;
	patch: (data: Partial<SchuelerLernabschnittsdaten>) => Promise<void>;
}
