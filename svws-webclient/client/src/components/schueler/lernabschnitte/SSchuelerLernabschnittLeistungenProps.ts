import type { SchuelerLeistungsdaten, SchuelerLernabschnittManager } from "@core";

export interface SchuelerLernabschnittLeistungenProps {
	manager: () => SchuelerLernabschnittManager;
	patchLeistung: (data : Partial<SchuelerLeistungsdaten>, id : number) => Promise<void>;
}