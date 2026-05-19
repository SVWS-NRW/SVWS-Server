import type { AbiturdatenManager, AbiturFachbelegung, GostBelegpruefungErgebnis, JavaMap, List, SchuelerListeEintrag } from "@core";

export interface GostAbiturZulassungProps {
	schuelerListe: List<SchuelerListeEintrag>;
	managerLaufbahnplanungMap: () => JavaMap<number, AbiturdatenManager>;
	ergebnisBelegpruefungMap: () => JavaMap<number, GostBelegpruefungErgebnis>;
	managerAbiturMap: () => JavaMap<number, AbiturdatenManager>;
	copyAbiturdatenAusLeistungsdaten: (idSchueler: number) => Promise<void>;
	updateAbiturpruefungsdaten: (manager: () => AbiturdatenManager, belegung: Partial<AbiturFachbelegung>, berechnePflichtpruefungenNeu: boolean) => Promise<void>;
}
