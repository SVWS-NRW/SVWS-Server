import type { SchuelerTelefon } from "@core/core/data/schueler/SchuelerTelefon";
import type { Telefonart } from "@core/core/data/schule/Telefonart";
import type { List } from "@core/java/util/List";

export interface SchuelerTelefonnummernProps {
	readonly: boolean;
	idSchueler: number;
	mapTelefonArten: Map<number, Telefonart>;
	getListSchuelerTelefoneintraege: () => List<SchuelerTelefon>;
	addSchuelerTelefoneintrag: (data: Partial<SchuelerTelefon>, idSchueler: number) => Promise<void>;
	patchSchuelerTelefoneintrag: (data: Partial<SchuelerTelefon>, idEintrag: number) => Promise<void>;
	deleteSchuelerTelefoneintrage: (idsEintraege: List<number>) => Promise<void>;
}