import type { AbiturdatenManager, AbiturFachbelegung, GostBelegpruefungErgebnis, JavaMap, List, SchuelerListeEintrag, SchuleStammdaten, ServerMode } from "@core";

export interface GostAbiturZulassungProps {
	serverMode: ServerMode;
	schule: SchuleStammdaten;
	schuelerListe: List<SchuelerListeEintrag>;
	managerLaufbahnplanungMap: () => JavaMap<number, AbiturdatenManager>;
	ergebnisBelegpruefungMap: () => JavaMap<number, GostBelegpruefungErgebnis>;
	managerAbiturMap: () => JavaMap<number, AbiturdatenManager>;
	copyAbiturdatenAusLeistungsdaten: (idSchueler: number) => Promise<void>;
	updateAbiturpruefungsdaten: (manager: () => AbiturdatenManager, belegung: Partial<AbiturFachbelegung>) => Promise<void>;
}
