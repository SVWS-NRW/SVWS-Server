import type { SchuelerStammdaten } from "@core/asd/data/schueler/SchuelerStammdaten";
import type { SchulEintrag } from "@core/core/data/kataloge/SchulEintrag";
import type { SchuelerTelefon } from "@core/core/data/schueler/SchuelerTelefon";
import type { Fahrschuelerart } from "@core/core/data/schule/Fahrschuelerart";
import type { FoerderschwerpunktEintrag } from "@core/core/data/schule/FoerderschwerpunktEintrag";
import type { Haltestelle } from "@core/core/data/schule/Haltestelle";
import type { ReligionEintrag } from "@core/core/data/schule/ReligionEintrag";
import type { Telefonart } from "@core/core/data/schule/Telefonart";
import type { List } from "@core/java/util/List";
import type { SchuelerListeManager } from "~/states/schueler/SchuelerListeManager";

export interface SchuelerIndividualdatenProps {
	patch: (data: Partial<SchuelerStammdaten>) => Promise<boolean>;
	schuelerListeManager: () => SchuelerListeManager;
	mapSchulen: Map<string, SchulEintrag>;
	fahrschuelerartenById: Map<number, Fahrschuelerart>;
	foerderschwerpunkteById: Map<number, FoerderschwerpunktEintrag>;
	haltestellenById: Map<number, Haltestelle>
	religionenById: Map<number, ReligionEintrag>;
	mapTelefonArten: Map<number, Telefonart>
	getListSchuelerTelefoneintraege: () => List<SchuelerTelefon>;
	addSchuelerTelefoneintrag: (data: Partial<SchuelerTelefon>, idSchueler: number) => Promise<void>;
	patchSchuelerTelefoneintrag: (data: Partial<SchuelerTelefon>, idEintrag: number) => Promise<void>;
	deleteSchuelerTelefoneintrage: (idsEintraege: List<number>) => Promise<void>;
	autofocus: boolean;
	zeigeAlles: boolean;
}
