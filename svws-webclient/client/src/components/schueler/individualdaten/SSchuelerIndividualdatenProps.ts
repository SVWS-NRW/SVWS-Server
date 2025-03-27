import type { SchuelerStammdaten, OrtKatalogEintrag, OrtsteilKatalogEintrag, KatalogEintrag, FoerderschwerpunktEintrag, ReligionEintrag, SchuelerListeManager, SchulEintrag, Schulform, ServerMode, BenutzerKompetenz } from "@core";

export interface SchuelerIndividualdatenProps {
	patch: (data: Partial<SchuelerStammdaten>) => Promise<void>;
	schuelerListeManager: () => SchuelerListeManager;
	mapSchulen: Map<string, SchulEintrag>;
	mapOrte: Map<number, OrtKatalogEintrag>;
	mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
	mapFahrschuelerarten: Map<number, KatalogEintrag>;
	mapFoerderschwerpunkte: Map<number, FoerderschwerpunktEintrag>;
	mapHaltestellen: Map<number, KatalogEintrag>
	mapReligionen: Map<number, ReligionEintrag>;
	schulform: Schulform;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	autofocus: boolean;
}
