import type { LehrerStammdaten } from "@core/asd/data/lehrer/LehrerStammdaten";
import type { Schulleitung } from "@core/asd/data/schule/Schulleitung";
import type { Leitungsfunktion } from "@core/core/data/schule/Leitungsfunktion";
import type { List } from "@core/java/util/List";
import type { LehrerListeManager } from "@ui/ui/manager/lehrer/LehrerListeManager";

export interface LehrerIndividualdatenProps {
	patch: (data: Partial<LehrerStammdaten>) => Promise<boolean>;
	lehrerListeManager: () => LehrerListeManager;
	mapLeitungsfunktionen: Map<number, Leitungsfunktion>;
	getListLeitungsfunktionen: () => List<Schulleitung>;
	addLeitungsfunktion: (data: Partial<Schulleitung>, idLehrer: number) => Promise<void>;
	patchLeitungsfunktion: (data: Partial<Schulleitung>, idEintrag: number) => Promise<void>;
	deleteLeitungsfunktionen: (idsEintraege: List<number>) => Promise<void>;
	zeigeAlles: boolean;
}
