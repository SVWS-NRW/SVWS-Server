import type { BetriebStammdaten, SchuelerBetriebe, BetriebAnsprechpartner, OrtKatalogEintrag, OrtsteilKatalogEintrag, List, LehrerListeEintrag,
	BetriebListeEintrag, Beschaeftigungsart, BenutzerKompetenz } from "@core";

export interface SchuelerAdressenProps {
	patchBetrieb: (data: Partial<BetriebStammdaten>, id: number) => Promise<void>;
	patchSchuelerBetriebsdaten: (data: Partial<SchuelerBetriebe>, id: number) => Promise<void>;
	patchAnsprechpartner: (data: Partial<BetriebAnsprechpartner>, id: number) => Promise<void>;
	setSchuelerBetrieb: (betrieb: SchuelerBetriebe | undefined) => Promise<void>;
	createAnsprechpartner: (data: BetriebAnsprechpartner) => Promise<void>;
	createSchuelerBetriebsdaten: (data: SchuelerBetriebe) => Promise<void>;
	orteById: Map<number, OrtKatalogEintrag>;
	ortsteileById: Map<number, OrtsteilKatalogEintrag>;
	idSchueler: number;
	listSchuelerbetriebe: () => List<SchuelerBetriebe>;
	betrieb: SchuelerBetriebe | undefined;
	betriebsStammdaten: BetriebStammdaten | undefined;
	beschaeftigungsartenById: Map<number, Beschaeftigungsart>;
	mapLehrer: Map<number, LehrerListeEintrag>;
	betriebeById: Map<number, BetriebListeEintrag>;
	mapAnsprechpartner: Map<number, BetriebAnsprechpartner>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
