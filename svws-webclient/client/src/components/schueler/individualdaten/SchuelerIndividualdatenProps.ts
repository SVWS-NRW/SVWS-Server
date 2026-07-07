import type { SchuelerStammdaten, OrtKatalogEintrag, OrtsteilKatalogEintrag, FoerderschwerpunktEintrag,
	ReligionEintrag, SchulEintrag, BenutzerKompetenz, Telefonart, SchuelerTelefon, List, Haltestelle, Fahrschuelerart } from "@core";
import type { SchuelerListeManager } from "~/states/schueler/SchuelerListeManager";

export interface SchuelerIndividualdatenProps {
	patch: (data: Partial<SchuelerStammdaten>) => Promise<boolean>;
	schuelerListeManager: () => SchuelerListeManager;
	mapSchulen: Map<string, SchulEintrag>;
	orteById: Map<number, OrtKatalogEintrag>;
	ortsteileById: Map<number, OrtsteilKatalogEintrag>;
	fahrschuelerartenById: Map<number, Fahrschuelerart>;
	foerderschwerpunkteById: Map<number, FoerderschwerpunktEintrag>;
	haltestellenById: Map<number, Haltestelle>
	religionenById: Map<number, ReligionEintrag>;
	mapTelefonArten: Map<number, Telefonart>
	getListSchuelerTelefoneintraege: () => List<SchuelerTelefon>;
	addSchuelerTelefoneintrag: (data: Partial<SchuelerTelefon>, idSchueler: number) => Promise<void>;
	patchSchuelerTelefoneintrag: (data: Partial<SchuelerTelefon>, idEintrag: number) => Promise<void>;
	deleteSchuelerTelefoneintrage: (idsEintraege: List<number>) => Promise<void>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	autofocus: boolean;
	zeigeAlles: boolean;
}
