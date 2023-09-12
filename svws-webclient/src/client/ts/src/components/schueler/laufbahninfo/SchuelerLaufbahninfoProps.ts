import type { List, Sprachbelegung, Sprachpruefung } from "@core";

export interface SchuelerLaufbahninfoProps {
	sprachbelegungen: () => List<Sprachbelegung>;
	sprachpruefungen: () => List<Sprachpruefung>;
	patchSprachbelegung: (data: Partial<Sprachbelegung>, id : number) => Promise<void>;
	addSprachbelegung: (data: Partial<Sprachbelegung>, id : number) => Promise<Sprachbelegung | null>;
	removeSprachbelegung: (data: Sprachbelegung, id : number) => Promise<Sprachbelegung>;
	patchSprachpruefung: (data: Partial<Sprachpruefung>, id : number) => Promise<void>;
	addSprachpruefung: (data: Partial<Sprachpruefung>, id : number) => Promise<Sprachpruefung | null>;
	removeSprachpruefung: (data: Sprachpruefung, id : number) => Promise<Sprachpruefung>;
}