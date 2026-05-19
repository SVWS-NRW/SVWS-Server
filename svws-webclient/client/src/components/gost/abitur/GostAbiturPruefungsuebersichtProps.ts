import type { AbiturdatenManager, AbiturFachbelegung, JavaMap, List, SchuelerListeEintrag } from "@core";

export interface GostAbiturPruefungsuebersichtProps {
	schuelerListe: List<SchuelerListeEintrag>;
	managerMap: () => JavaMap<number, AbiturdatenManager>;
	updateAbiturpruefungsdaten: (manager: () => AbiturdatenManager, belegung: Partial<AbiturFachbelegung>, berechnePflichtpruefungenNeu: boolean) => Promise<void>;
}
