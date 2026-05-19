import type { AbiturdatenManager, AbiturFachbelegung, SchuelerListeEintrag } from "@core";

export interface SchuelerAbiturPruefungsuebersichtProps {
	schueler: SchuelerListeEintrag;
	manager: () => AbiturdatenManager | null;
	updateAbiturpruefungsdaten: (manager: () => AbiturdatenManager, belegung: Partial<AbiturFachbelegung>, berechnePflichtpruefungenNeu: boolean) => Promise<void>;
}
