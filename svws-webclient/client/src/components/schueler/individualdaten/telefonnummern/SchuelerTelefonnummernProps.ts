import type { Telefonart } from "@core/core/data/schule/Telefonart";

export interface SchuelerTelefonnummernProps {
	readonly: boolean;
	idSchueler: number;
	mapTelefonArten: Map<number, Telefonart>;
}