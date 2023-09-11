import type { List, Sprachbelegung, Sprachpruefung } from "@core";

export interface SchuelerLaufbahninfoProps {
	sprachbelegungen: () => List<Sprachbelegung>;
	sprachpruefungen: () => List<Sprachpruefung>;
}