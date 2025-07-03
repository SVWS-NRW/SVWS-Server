import type { AbiturdatenManager, SchuleStammdaten, ServerMode } from "@core";

export interface SchuelerAbiturZulassungTabelleProps {
	serverMode: ServerMode;
	schule: SchuleStammdaten;
	manager: () => AbiturdatenManager;
}
