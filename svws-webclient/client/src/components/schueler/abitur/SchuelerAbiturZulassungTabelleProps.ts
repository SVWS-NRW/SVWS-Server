import type { AbiturdatenManager, AbiturFachbelegung, SchuleStammdaten, ServerMode } from "@core";

export interface SchuelerAbiturZulassungTabelleProps {
	serverMode: ServerMode;
	schule: SchuleStammdaten;
	manager: () => AbiturdatenManager;
	updateAbiturpruefungsdaten: ((manager: () => AbiturdatenManager, belegung: Partial<AbiturFachbelegung>) => Promise<void>) | null;
}
