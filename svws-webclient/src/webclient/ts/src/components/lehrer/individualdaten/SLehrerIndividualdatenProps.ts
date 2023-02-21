import { LehrerStammdaten, List, OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@svws-nrw/svws-core-ts";

export interface LehrerIndividualdatenProps {
	patch: (data : Partial<LehrerStammdaten>) => Promise<void>;
	stammdaten: LehrerStammdaten;
	orte: List<OrtKatalogEintrag>;
	ortsteile: List<OrtsteilKatalogEintrag>;
}