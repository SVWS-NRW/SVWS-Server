import type { AbiturdatenManager, AbiturFachbelegung, GostBelegpruefungErgebnis, SchuelerListeEintrag, SchuleStammdaten, ServerMode } from "@core";

export interface SchuelerAbiturZulassungProps {
	serverMode: ServerMode;
	schule: SchuleStammdaten;
	schueler: SchuelerListeEintrag;
	managerLaufbahnplanung: () => AbiturdatenManager;
	ergebnisBelegpruefung: () => GostBelegpruefungErgebnis;
	managerAbitur: () => AbiturdatenManager | null;
	copyAbiturdatenAusLeistungsdaten: (idSchueler: number) => Promise<void>;
	updateAbiturpruefungsdaten: (manager: () => AbiturdatenManager, belegung: Partial<AbiturFachbelegung>, berechnePflichtpruefungenNeu: boolean) => Promise<void>;
}
