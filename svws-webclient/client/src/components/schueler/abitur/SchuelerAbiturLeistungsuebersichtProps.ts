import type { AbiturdatenManager, GostBelegpruefungErgebnis, SchuleStammdaten, ServerMode } from "@core";

export interface SchuelerAbiturLeistungsuebersichtProps {
	serverMode: ServerMode;
	schule: SchuleStammdaten;
	managerLaufbahnplanung: () => AbiturdatenManager;
	ergebnisBelegpruefung: () => GostBelegpruefungErgebnis;
	managerAbitur: () => AbiturdatenManager | null;
}
