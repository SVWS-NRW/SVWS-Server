import type { LehrerStammdaten, OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@core";

export interface LehrerIndividualdatenProps {
	patch: (data : Partial<LehrerStammdaten>) => Promise<void>;
	stammdaten: LehrerStammdaten;
	mapOrte: Map<number, OrtKatalogEintrag>;
	mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
}