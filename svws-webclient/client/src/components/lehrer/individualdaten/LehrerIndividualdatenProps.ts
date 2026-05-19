import type { BenutzerKompetenz, LehrerStammdaten, OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@core";
import type { LehrerListeManager } from "@ui";

export interface LehrerIndividualdatenProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	patch: (data: Partial<LehrerStammdaten>) => Promise<boolean>;
	lehrerListeManager: () => LehrerListeManager;
	orteById: Map<number, OrtKatalogEintrag>;
	ortsteileById: Map<number, OrtsteilKatalogEintrag>;
}
