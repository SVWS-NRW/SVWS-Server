import type { SchuelerLeistungsdaten, SchuelerLernabschnittManager, SchuelerLernabschnittsdaten } from "@core";

export interface SchuelerLernabschnittLeistungenProps {
	manager: () => SchuelerLernabschnittManager;
	patch: (data : Partial<SchuelerLernabschnittsdaten>) => Promise<void>;
	patchLeistung: (data : Partial<SchuelerLeistungsdaten>, id : number) => Promise<void>;
}