import type { BenutzerKompetenz, LehrerStammdaten, Leitungsfunktion, List, OrtKatalogEintrag, OrtsteilKatalogEintrag, Schulleitung } from "@core";
import type { LehrerListeManager } from "@ui";

export interface LehrerIndividualdatenProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	patch: (data: Partial<LehrerStammdaten>) => Promise<boolean>;
	lehrerListeManager: () => LehrerListeManager;
	orteById: Map<number, OrtKatalogEintrag>;
	ortsteileById: Map<number, OrtsteilKatalogEintrag>;
	mapLeitungsfunktionen: Map<number, Leitungsfunktion>;
	getListLeitungsfunktionen: () => List<Schulleitung>;
	addLeitungsfunktion: (data: Partial<Schulleitung>, idLehrer: number) => Promise<void>;
	patchLeitungsfunktion: (data: Partial<Schulleitung>, idEintrag: number) => Promise<void>;
	deleteLeitungsfunktionen: (idsEintraege: List<number>) => Promise<void>;
}
