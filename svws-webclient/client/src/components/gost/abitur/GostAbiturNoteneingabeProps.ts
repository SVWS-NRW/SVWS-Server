import type { AbiturdatenManager, AbiturFachbelegung, JavaMap, KursDaten, LehrerListeEintrag, List, SchuelerListeEintrag } from "@core";

export interface GostAbiturNoteneingabeProps {
	schuelerListe: List<SchuelerListeEintrag>;
	mapLehrer: JavaMap<number, LehrerListeEintrag>;
	mapKurse: JavaMap<number, KursDaten>;
	managerMap: () => JavaMap<number, AbiturdatenManager>;
	updateAbiturpruefungsdaten: (manager: () => AbiturdatenManager, belegung: Partial<AbiturFachbelegung>, berechnePflichtpruefungenNeu: boolean) => Promise<void>;
}
