import type { AbiturdatenManager } from "@core/core/abschluss/gost/AbiturdatenManager";
import type { AbiturFachbelegung } from "@core/core/data/gost/AbiturFachbelegung";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";

export interface SchuelerAbiturPruefungsuebersichtTabelleProps {
	schueler: SchuelerListeEintrag;
	manager: () => AbiturdatenManager;
	updateAbiturpruefungsdaten: (manager: () => AbiturdatenManager, belegung: Partial<AbiturFachbelegung>, berechnePflichtpruefungenNeu: boolean) => Promise<void>;
}
