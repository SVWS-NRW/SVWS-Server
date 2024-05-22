import type { List, SchuelerListeManager, Schulform, Schulgliederung, ServerMode, Sprachbelegung, Sprachpruefung } from "@core";

export interface SchuelerSprachenProps {
	sprachbelegungen: () => List<Sprachbelegung>;
	sprachpruefungen: () => List<Sprachpruefung>;
	patchSprachbelegung: (data: Partial<Sprachbelegung>, sprache: string) => Promise<void>;
	addSprachbelegung: (data: Partial<Sprachbelegung>) => Promise<Sprachbelegung | null>;
	removeSprachbelegung: (data: Sprachbelegung) => Promise<Sprachbelegung>;
	patchSprachpruefung: (data: Partial<Sprachpruefung>, sprache: string) => Promise<void>;
	addSprachpruefung: (data: Partial<Sprachpruefung>) => Promise<Sprachpruefung | null>;
	removeSprachpruefung: (data: Sprachpruefung) => Promise<Sprachpruefung>;
	schuelerListeManager: () => SchuelerListeManager;
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
	serverMode: ServerMode;
}