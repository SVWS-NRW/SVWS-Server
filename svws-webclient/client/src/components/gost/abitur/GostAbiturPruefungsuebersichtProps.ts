import type { AbiturdatenManager, AbiturFachbelegung, JavaMap, List, SchuelerListeEintrag, SchuleStammdaten, ServerMode } from "@core";

export interface GostAbiturPruefungsuebersichtProps {
	serverMode: ServerMode;
	schule: SchuleStammdaten;
	schuelerListe: List<SchuelerListeEintrag>;
	managerMap: () => JavaMap<number, AbiturdatenManager>;
	updateAbiturpruefungsdaten: (manager: () => AbiturdatenManager, belegung: Partial<AbiturFachbelegung>) => Promise<void>;
}
