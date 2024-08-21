import type { Schulform, KlassenDaten, Schueler, KlassenListeManager, List, Schulgliederung } from "@core";
import type { LehrerListeEintrag } from "@core";

export interface KlassenDatenProps {
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
	patch: (data : Partial<KlassenDaten>) => Promise<void>;
	klassenListeManager: () => KlassenListeManager;
	mapKlassenVorigerAbschnitt: () => Map<number, KlassenDaten>;
	mapKlassenFolgenderAbschnitt: () => Map<number, KlassenDaten>;
	setFilter: () => Promise<void>;
	gotoSchueler: (eintrag: Schueler) => Promise<void>;
	gotoLehrer: (eintrag: LehrerListeEintrag) => Promise<void>;
	addKlassenleitung: (idLehrer : number, idKlasse : number) => Promise<void>;
	removeKlassenleitung: (eintrag: LehrerListeEintrag) => Promise<void>;
	updateReihenfolgeKlassenleitung: (idLehrer : number, erhoehe : boolean) => Promise<void>;
}
