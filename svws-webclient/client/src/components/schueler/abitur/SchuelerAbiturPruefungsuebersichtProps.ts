import type { AbiturdatenManager, AbiturFachbelegung, SchuleStammdaten, ServerMode } from "@core";

export interface SchuelerAbiturPruefungsuebersichtProps {
	serverMode: ServerMode;
	schule: SchuleStammdaten;
	manager: () => AbiturdatenManager | null;
	updateAbiturpruefungsdaten: (manager: () => AbiturdatenManager, belegung: Partial<AbiturFachbelegung>) => Promise<void>;
}
