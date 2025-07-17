import type { AbiturdatenManager, AbiturFachbelegung, JavaMap, KursDaten, LehrerListeEintrag, List, SchuelerListeEintrag, SchuleStammdaten, ServerMode } from "@core";

export interface GostAbiturNoteneingabeProps {
	serverMode: ServerMode;
	schule: SchuleStammdaten;
	schuelerListe: List<SchuelerListeEintrag>;
	mapLehrer: JavaMap<number, LehrerListeEintrag>;
	mapKurse: JavaMap<number, KursDaten>;
	managerMap: () => JavaMap<number, AbiturdatenManager>;
	updateAbiturpruefungsdaten: (manager: () => AbiturdatenManager, belegung: Partial<AbiturFachbelegung>, berechnePflichtpruefungenNeu: boolean) => Promise<void>;
}
