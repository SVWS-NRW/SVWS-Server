import type { AbiturdatenManager, AbiturFachbelegung, SchuelerListeEintrag } from "@core";

export interface SchuelerAbiturPruefungsuebersichtTabelleProps {
	schueler: SchuelerListeEintrag;
	manager: () => AbiturdatenManager;
	updateAbiturpruefungsdaten: (manager: () => AbiturdatenManager, belegung: Partial<AbiturFachbelegung>, berechnePflichtpruefungenNeu: boolean) => Promise<void>;
}
