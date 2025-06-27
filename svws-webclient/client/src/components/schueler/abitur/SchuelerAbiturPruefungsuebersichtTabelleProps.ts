import type { AbiturdatenManager, AbiturFachbelegung, SchuelerListeEintrag, SchuleStammdaten, ServerMode } from "@core";

export interface SchuelerAbiturPruefungsuebersichtTabelleProps {
	serverMode: ServerMode;
	schule: SchuleStammdaten;
	schueler: SchuelerListeEintrag;
	manager: () => AbiturdatenManager;
	updateAbiturpruefungsdaten: (manager: () => AbiturdatenManager, belegung: Partial<AbiturFachbelegung>, berechnePflichtpruefungenNeu: boolean) => Promise<void>;
}
