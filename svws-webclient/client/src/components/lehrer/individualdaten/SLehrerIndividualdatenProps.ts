import type { LehrerListeManager, LehrerStammdaten, OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@core";

export interface LehrerIndividualdatenProps {
	patch: (data : Partial<LehrerStammdaten>) => Promise<void>;
	lehrerListeManager: () => LehrerListeManager;
	mapOrte: Map<number, OrtKatalogEintrag>;
	mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
}