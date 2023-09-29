import type { FaecherListeEintrag, LehrerListeEintrag, KursListeEintrag, SchuelerLeistungsdaten, SchuelerLernabschnittsdaten } from "@core";

export interface SchuelerLeistungenDatenProps {
	data: SchuelerLernabschnittsdaten | undefined;
	mapFaecher: Map<number, FaecherListeEintrag>;
	mapLehrer: Map<number, LehrerListeEintrag>;
	mapKurse: Map<number, KursListeEintrag>;
	patchLeistung: (data : Partial<SchuelerLeistungsdaten>, id : number) => Promise<void>;
}