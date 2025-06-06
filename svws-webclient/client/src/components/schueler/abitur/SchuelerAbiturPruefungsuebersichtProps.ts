import type { AbiturdatenManager, AbiturFachbelegung, SchuelerListeEintrag, SchuleStammdaten, ServerMode } from "@core";

export interface SchuelerAbiturPruefungsuebersichtProps {
	serverMode: ServerMode;
	schule: SchuleStammdaten;
	schueler: SchuelerListeEintrag;
	manager: () => AbiturdatenManager | null;
	updateAbiturpruefungsdaten: (manager: () => AbiturdatenManager, belegung: Partial<AbiturFachbelegung>) => Promise<void>;
}
