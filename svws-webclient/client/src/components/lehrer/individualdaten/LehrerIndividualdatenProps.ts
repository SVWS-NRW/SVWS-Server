import type { BenutzerKompetenz, LehrerStammdaten, OrtKatalogEintrag, OrtsteilKatalogEintrag, Schulform, ServerMode, ValidatorKontext } from "@core";
import type { LehrerListeManager } from "@ui";

export interface LehrerIndividualdatenProps {
	validatorKontext: () => ValidatorKontext;
	schulform: Schulform;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	patch: (data: Partial<LehrerStammdaten>) => Promise<void>;
	lehrerListeManager: () => LehrerListeManager;
	mapOrte: Map<number, OrtKatalogEintrag>;
	mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
}
