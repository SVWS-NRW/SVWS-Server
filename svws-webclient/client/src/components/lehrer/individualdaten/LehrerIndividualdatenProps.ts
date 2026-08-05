import type { LehrerStammdaten, Leitungsfunktion, List, Schulleitung } from "@core";
import type { LehrerListeManager } from "@ui";

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
