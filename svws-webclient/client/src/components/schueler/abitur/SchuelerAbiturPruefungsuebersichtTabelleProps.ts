import type { AbiturdatenManager, AbiturFachbelegung, SchuleStammdaten, ServerMode } from "@core";

export interface SchuelerAbiturPruefungsuebersichtTabelleProps {
	serverMode: ServerMode;
	schule: SchuleStammdaten;
	manager: () => AbiturdatenManager;
	updateAbiturpruefungsdaten: (manager: () => AbiturdatenManager, belegung: Partial<AbiturFachbelegung>) => Promise<void>;
}
