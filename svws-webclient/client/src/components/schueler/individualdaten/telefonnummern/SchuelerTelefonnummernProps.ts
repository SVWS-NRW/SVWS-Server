import type { List, SchuelerTelefon, Telefonart } from "@core";

export interface SchuelerTelefonnummernProps {
	readonly: boolean;
	idSchueler: number;
	mapTelefonArten: Map<number, Telefonart>;
	getListSchuelerTelefoneintraege: () => List<SchuelerTelefon>;
	addSchuelerTelefoneintrag: (data: Partial<SchuelerTelefon>, idSchueler: number) => Promise<void>;
	patchSchuelerTelefoneintrag: (data: Partial<SchuelerTelefon>, idEintrag: number) => Promise<void>;
	deleteSchuelerTelefoneintrage: (idsEintraege: List<number>) => Promise<void>;
}