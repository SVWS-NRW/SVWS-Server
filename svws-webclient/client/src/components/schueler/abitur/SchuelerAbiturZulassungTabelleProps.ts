import type { AbiturdatenManager, AbiturFachbelegung } from "@core";

export interface SchuelerAbiturZulassungTabelleProps {
	manager: () => AbiturdatenManager;
	updateAbiturpruefungsdaten: ((manager: () => AbiturdatenManager, belegung: Partial<AbiturFachbelegung>, berechnePflichtpruefungenNeu: boolean) => Promise<void>) | null;
}
