import type { AbiturdatenManager, SchuleStammdaten, ServerMode } from "@core";

export interface SchuelerAbiturPruefungsuebersichtTabelleProps {
	serverMode: ServerMode;
	schule: SchuleStammdaten;
	manager: () => AbiturdatenManager;
}
