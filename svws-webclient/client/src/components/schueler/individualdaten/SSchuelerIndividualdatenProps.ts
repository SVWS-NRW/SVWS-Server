import type { SchuelerStammdaten, OrtKatalogEintrag, OrtsteilKatalogEintrag, KatalogEintrag, FoerderschwerpunktEintrag, ReligionEintrag, SchuelerListeManager } from "@core";

export interface SchuelerIndividualdatenProps {
	patch: (data: Partial<SchuelerStammdaten>) => Promise<void>;
	schuelerListeManager: () => SchuelerListeManager;
	mapOrte: Map<number, OrtKatalogEintrag>;
	mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
	mapFahrschuelerarten: Map<number, KatalogEintrag>;
	mapFoerderschwerpunkte: Map<number, FoerderschwerpunktEintrag>;
	mapHaltestellen: Map<number, KatalogEintrag>
	mapReligionen: Map<number, ReligionEintrag>;
}