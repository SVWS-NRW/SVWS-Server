import type { SchuelerLernabschnittsdaten } from "@core/asd/data/schueler/SchuelerLernabschnittsdaten";
import type { SchuelerLernabschnittManager } from "../SchuelerLernabschnittManager";

export interface SchuelerLernabschnittNachpruefungProps {
	manager: () => SchuelerLernabschnittManager;
	patch: (data: Partial<SchuelerLernabschnittsdaten>) => Promise<void>;
}
