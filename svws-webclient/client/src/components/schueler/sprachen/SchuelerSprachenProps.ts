import type { BenutzerKompetenz, List, Sprachbelegung, Sprachpruefung } from "@core";
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
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	benutzerKompetenzenKlassen: Set<number>;
}
