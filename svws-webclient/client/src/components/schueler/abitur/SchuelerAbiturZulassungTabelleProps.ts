import type { AbiturdatenManager } from "@core/core/abschluss/gost/AbiturdatenManager";
import type { AbiturFachbelegung } from "@core/core/data/gost/AbiturFachbelegung";

export interface SchuelerAbiturZulassungTabelleProps {
	manager: () => AbiturdatenManager;
	updateAbiturpruefungsdaten: ((manager: () => AbiturdatenManager, belegung: Partial<AbiturFachbelegung>, berechnePflichtpruefungenNeu: boolean) => Promise<void>) | null;
}
