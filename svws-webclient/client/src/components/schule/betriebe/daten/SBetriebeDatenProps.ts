import type {BenutzerKompetenz, BetriebAnsprechpartner, BetriebStammdaten, KatalogEintrag, OrtKatalogEintrag} from "@core";

export interface BetriebeDatenProps {
	patch: (data : Partial<BetriebStammdaten>) => Promise<void>;
	patchBetriebAnpsrechpartner: (data : Partial<BetriebAnsprechpartner>, id: number) => Promise<void>;
	addBetriebAnsprechpartner: (data: BetriebAnsprechpartner) => Promise<void>;
	removeBetriebAnsprechpartner: (data: BetriebAnsprechpartner[]) => Promise<void>;
	daten: BetriebStammdaten;
	mapBeschaeftigungsarten: Map<number, KatalogEintrag>;
	mapOrte: Map<number, OrtKatalogEintrag>;
	mapAnsprechpartner: Map<number, BetriebAnsprechpartner>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
