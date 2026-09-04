import type { AbiturdatenManager } from "@core/core/abschluss/gost/AbiturdatenManager";
import type { GostBelegpruefungErgebnis } from "@core/core/abschluss/gost/GostBelegpruefungErgebnis";
import type { AbiturFachbelegung } from "@core/core/data/gost/AbiturFachbelegung";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";

export interface SchuelerAbiturZulassungProps {
	schueler: SchuelerListeEintrag;
	managerLaufbahnplanung: () => AbiturdatenManager;
	ergebnisBelegpruefung: () => GostBelegpruefungErgebnis;
	managerAbitur: () => AbiturdatenManager | null;
	copyAbiturdatenAusLeistungsdaten: (idSchueler: number) => Promise<void>;
	updateAbiturpruefungsdaten: (manager: () => AbiturdatenManager, belegung: Partial<AbiturFachbelegung>, berechnePflichtpruefungenNeu: boolean) => Promise<void>;
}
