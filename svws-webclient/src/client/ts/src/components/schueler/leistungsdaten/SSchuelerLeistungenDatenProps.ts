import type { FaecherListeEintrag, LehrerListeEintrag, SchuelerLeistungsdaten, SchuelerLernabschnittsdaten } from "@core";

export interface SchuelerLeistungenDatenProps {
	data: SchuelerLernabschnittsdaten | undefined;
	mapFaecher: Map<number, FaecherListeEintrag>;
	mapLehrer: Map<number, LehrerListeEintrag>;
	patchLeistung: (data : Partial<SchuelerLeistungsdaten>, id : number) => Promise<void>;
}