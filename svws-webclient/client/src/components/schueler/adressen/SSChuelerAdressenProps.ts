import type { BetriebStammdaten, SchuelerBetriebsdaten, BetriebAnsprechpartner, OrtKatalogEintrag, OrtsteilKatalogEintrag, List, LehrerListeEintrag,
	BetriebListeEintrag, Beschaeftigungsart } from "@core";

export interface SchuelerAdressenProps {
	patchBetrieb: (data : Partial<BetriebStammdaten>, id : number) => Promise<void>;
	patchSchuelerBetriebsdaten: (data : Partial<SchuelerBetriebsdaten>, id : number) => Promise<void>;
	patchAnsprechpartner: (data : Partial<BetriebAnsprechpartner>, id : number) => Promise<void>;
	setSchuelerBetrieb: (betrieb : SchuelerBetriebsdaten | undefined) => Promise<void>;
	createAnsprechpartner: (data: BetriebAnsprechpartner) => Promise<void>;
	createSchuelerBetriebsdaten: (data: SchuelerBetriebsdaten) => Promise<void>;
	mapOrte: Map<number, OrtKatalogEintrag>;
	mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
	idSchueler: number;
	listSchuelerbetriebe:() => List<SchuelerBetriebsdaten>;
	betrieb: SchuelerBetriebsdaten | undefined;
	betriebsStammdaten: BetriebStammdaten | undefined;
	mapBeschaeftigungsarten: Map<number, Beschaeftigungsart>;
	mapLehrer: Map<number, LehrerListeEintrag>;
	mapBetriebe: Map<number, BetriebListeEintrag>;
	mapAnsprechpartner: Map<number, BetriebAnsprechpartner>;
}
