import type { Collection, SchuelerLeistungsdaten, SchuelerLernabschnittsdaten } from "@core";
import type { SchuelerListeManager } from "~/states/schueler/SchuelerListeManager";
import type { SchuelerLernabschnittManager } from "../SchuelerLernabschnittManager";

export interface SchuelerLernabschnittLeistungenProps {
	schuelerListeManager: () => SchuelerListeManager;
	manager: () => SchuelerLernabschnittManager;
	patch: (data: Partial<SchuelerLernabschnittsdaten>) => Promise<void>;
	patchLeistung: (data: Partial<SchuelerLeistungsdaten>, id: number) => Promise<void>;
	addLeistung: (idFach: number) => Promise<void>;
	deleteLeistungen: (idsLeistungen: Collection<number>) => Promise<void>;
}
