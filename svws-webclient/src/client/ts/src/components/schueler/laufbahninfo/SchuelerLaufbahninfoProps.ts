import type { List, Sprachbelegung, Sprachpruefung } from "@core";

export interface SchuelerLaufbahninfoProps {
	sprachbelegungen: () => List<Sprachbelegung>;
	sprachpruefungen: () => List<Sprachpruefung>;
	patchSprachbelegung: (data: Partial<Sprachbelegung>) => Promise<void>;
	addSprachbelegung: (data: Partial<Sprachbelegung>) => Promise<Sprachbelegung | null>;
	removeSprachbelegung: (data: Sprachbelegung) => Promise<Sprachbelegung>;
	patchSprachpruefung: (data: Partial<Sprachpruefung>) => Promise<void>;
	addSprachpruefung: (data: Partial<Sprachpruefung>) => Promise<Sprachpruefung | null>;
	removeSprachpruefung: (data: Sprachpruefung) => Promise<Sprachpruefung>;
}