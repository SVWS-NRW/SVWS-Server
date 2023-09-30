import type { SchuelerLeistungsdaten, SchuelerLernabschnittManager } from "@core";

export interface SchuelerLeistungenDatenProps {
	manager: () => SchuelerLernabschnittManager;
	patchLeistung: (data : Partial<SchuelerLeistungsdaten>, id : number) => Promise<void>;
}