import type { AbiturdatenManager, SchuleStammdaten, ServerMode } from "@core";

export interface SchuelerAbiturZulassungProps {
	serverMode: ServerMode;
	schule: SchuleStammdaten;
	manager: () => AbiturdatenManager;
}
