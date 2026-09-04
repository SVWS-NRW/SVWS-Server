import type { Sprachbelegung } from "@core/asd/data/schueler/Sprachbelegung";
import type { Sprachpruefung } from "@core/asd/data/schueler/Sprachpruefung";
import type { List } from "@core/java/util/List";
import type { SchuelerListeManager } from "~/states/schueler/SchuelerListeManager";

export interface SchuelerSprachenProps {
	sprachbelegungen: () => List<Sprachbelegung>;
	sprachpruefungen: () => List<Sprachpruefung>;
	patchSprachbelegung: (data: Partial<Sprachbelegung>, sprache: string) => Promise<void>;
	addSprachbelegung: (data: Partial<Sprachbelegung>) => Promise<Sprachbelegung | null>;
	removeSprachbelegung: (data: Sprachbelegung) => Promise<Sprachbelegung>;
	patchSprachpruefung: (data: Partial<Sprachpruefung>, id: number) => Promise<void>;
	addSprachpruefung: (data: Partial<Sprachpruefung>) => Promise<Sprachpruefung | null>;
	removeSprachpruefung: (data: Sprachpruefung) => Promise<Sprachpruefung>;
	schuelerListeManager: () => SchuelerListeManager;
}
