import type { AbiturdatenManager } from "@core/core/abschluss/gost/AbiturdatenManager";
import type { GostBelegpruefungErgebnis } from "@core/core/abschluss/gost/GostBelegpruefungErgebnis";
import type { AbiturFachbelegung } from "@core/core/data/gost/AbiturFachbelegung";

export interface SchuelerAbiturZulassungProps {
	managerLaufbahnplanung: () => AbiturdatenManager;
	ergebnisBelegpruefung: () => GostBelegpruefungErgebnis;
	managerAbitur: () => AbiturdatenManager | null;
	copyAbiturdatenAusLeistungsdaten: (idSchueler: number) => Promise<void>;
	updateAbiturpruefungsdaten: (manager: () => AbiturdatenManager, belegung: Partial<AbiturFachbelegung>, berechnePflichtpruefungenNeu: boolean) => Promise<void>;
}
