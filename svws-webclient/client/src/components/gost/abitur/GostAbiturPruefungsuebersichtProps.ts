import type { AbiturdatenManager } from "@core/core/abschluss/gost/AbiturdatenManager";
import type { AbiturFachbelegung } from "@core/core/data/gost/AbiturFachbelegung";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import type { JavaMap } from "@core/java/util/JavaMap";
import type { List } from "@core/java/util/List";

export interface GostAbiturPruefungsuebersichtProps {
	schuelerListe: List<SchuelerListeEintrag>;
	managerMap: () => JavaMap<number, AbiturdatenManager>;
	updateAbiturpruefungsdaten: (manager: () => AbiturdatenManager, belegung: Partial<AbiturFachbelegung>, berechnePflichtpruefungenNeu: boolean) => Promise<void>;
}
