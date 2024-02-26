import type { List, SchuelerLeistungsdaten, SchuelerLernabschnittManager, SchuelerLernabschnittsdaten, Schulform } from "@core";

export interface SchuelerLernabschnittLeistungenProps {
	schulform: Schulform;
	manager: () => SchuelerLernabschnittManager;
	patch: (data : Partial<SchuelerLernabschnittsdaten>) => Promise<void>;
	patchLeistung: (data : Partial<SchuelerLeistungsdaten>, id : number) => Promise<void>;
	addLeistung: (fachID : number) => Promise<void>;
	deleteLeistungen: (leistungenIDs: List<number>) => Promise<void>;
}