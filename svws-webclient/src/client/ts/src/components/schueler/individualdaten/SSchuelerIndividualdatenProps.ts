import { SchuelerStammdaten, OrtKatalogEintrag, OrtsteilKatalogEintrag, KatalogEintrag, FoerderschwerpunktEintrag, ReligionEintrag } from "@svws-nrw/svws-core";

export interface SchuelerIndividualdatenProps {
	patch: (data : Partial<SchuelerStammdaten>) => Promise<void>;
	data: SchuelerStammdaten;
	mapOrte: Map<number, OrtKatalogEintrag>;
	mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
	mapFahrschuelerarten: Map<number, KatalogEintrag>;
	mapFoerderschwerpunkte: Map<number, FoerderschwerpunktEintrag>;
	mapHaltestellen: Map<number, KatalogEintrag>
	mapReligionen: Map<number, ReligionEintrag>;
}