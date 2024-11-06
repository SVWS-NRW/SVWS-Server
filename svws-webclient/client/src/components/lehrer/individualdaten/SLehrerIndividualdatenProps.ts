import type { BenutzerKompetenz, LehrerListeManager, LehrerStammdaten, OrtKatalogEintrag, OrtsteilKatalogEintrag, Schulform, ServerMode, ValidatorKontext } from "@core";

export interface LehrerIndividualdatenProps {
	validatorKontext: () => ValidatorKontext;
	schulform: Schulform;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	patch: (data : Partial<LehrerStammdaten>) => Promise<void>;
	lehrerListeManager: () => LehrerListeManager;
	mapOrte: Map<number, OrtKatalogEintrag>;
	mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
}