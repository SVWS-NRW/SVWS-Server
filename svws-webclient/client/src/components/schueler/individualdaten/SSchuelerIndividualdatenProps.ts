import type { SchuelerStammdaten, OrtKatalogEintrag, OrtsteilKatalogEintrag, FoerderschwerpunktEintrag, ReligionEintrag, SchulEintrag, Schulform, ServerMode, BenutzerKompetenz, TelefonArt, SchuelerTelefon, List, Haltestelle, Fahrschuelerart, ApiFile, ReportingParameter } from "@core";
import type { SchuelerListeManager } from "@ui";

export interface SchuelerIndividualdatenProps {
	patch: (data: Partial<SchuelerStammdaten>) => Promise<void>;
	schuelerListeManager: () => SchuelerListeManager;
	mapSchulen: Map<string, SchulEintrag>;
	mapOrte: Map<number, OrtKatalogEintrag>;
	mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
	mapFahrschuelerarten: Map<number, Fahrschuelerart>;
	mapFoerderschwerpunkte: Map<number, FoerderschwerpunktEintrag>;
	mapHaltestellen: Map<number, Haltestelle>
	mapReligionen: Map<number, ReligionEintrag>;
	mapTelefonArten: Map<number, TelefonArt>
	getListSchuelerTelefoneintraege: () => List<SchuelerTelefon>;
	addSchuelerTelefoneintrag: (data: Partial<SchuelerTelefon>, idSchueler: number) => Promise<void>;
	patchSchuelerTelefoneintrag: (data: Partial<SchuelerTelefon>, idEintrag: number) => Promise<void>;
	deleteSchuelerTelefoneintrage: (idsEintraege: List<number>) => Promise<void>;
	schulform: Schulform;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	autofocus: boolean;
	getPDF: (parameter: ReportingParameter) => Promise<ApiFile>;
}
