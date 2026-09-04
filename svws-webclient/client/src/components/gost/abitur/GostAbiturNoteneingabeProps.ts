import type { KursDaten } from "@core/asd/data/kurse/KursDaten";
import type { AbiturdatenManager } from "@core/core/abschluss/gost/AbiturdatenManager";
import type { AbiturFachbelegung } from "@core/core/data/gost/AbiturFachbelegung";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import type { JavaMap } from "@core/java/util/JavaMap";
import type { List } from "@core/java/util/List";

export interface GostAbiturNoteneingabeProps {
	schuelerListe: List<SchuelerListeEintrag>;
	mapLehrer: JavaMap<number, LehrerListeEintrag>;
	mapKurse: JavaMap<number, KursDaten>;
	managerMap: () => JavaMap<number, AbiturdatenManager>;
	updateAbiturpruefungsdaten: (manager: () => AbiturdatenManager, belegung: Partial<AbiturFachbelegung>, berechnePflichtpruefungenNeu: boolean) => Promise<void>;
}
