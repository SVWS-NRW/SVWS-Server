import type { AbiturdatenManager, GostBelegpruefungErgebnis, SchuleStammdaten, ServerMode } from "@core";

export interface SchuelerAbiturZulassungProps {
	serverMode: ServerMode;
	schule: SchuleStammdaten;
	managerLaufbahnplanung: () => AbiturdatenManager;
	ergebnisBelegpruefung: () => GostBelegpruefungErgebnis;
	managerAbitur: () => AbiturdatenManager | null;
}
