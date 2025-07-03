import type { AbiturdatenManager, SchuleStammdaten, ServerMode } from "@core";

export interface SchuelerAbiturPruefungsuebersichtProps {
	serverMode: ServerMode;
	schule: SchuleStammdaten;
	manager: () => AbiturdatenManager | null;
}
