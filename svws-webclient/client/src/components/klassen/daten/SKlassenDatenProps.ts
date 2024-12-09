import type { Schulform, KlassenDaten, Schueler, KlassenListeManager, List, Schulgliederung, ServerMode, BenutzerKompetenz } from "@core";
import type { LehrerListeEintrag } from "@core";

export interface KlassenDatenProps {
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	patch: (data : Partial<KlassenDaten>) => Promise<void>;
	manager: () => KlassenListeManager;
	mapKlassenVorigerAbschnitt: () => Map<number, KlassenDaten>;
	mapKlassenFolgenderAbschnitt: () => Map<number, KlassenDaten>;
	setFilter: () => Promise<void>;
	gotoSchueler: (eintrag: Schueler) => Promise<void>;
	gotoLehrer: (eintrag: LehrerListeEintrag) => Promise<void>;
	addKlassenleitung: (idLehrer : number, idKlasse : number) => Promise<void>;
	removeKlassenleitung: (eintrag: LehrerListeEintrag) => Promise<void>;
	updateReihenfolgeKlassenleitung: (idLehrer : number, erhoehe : boolean) => Promise<void>;
}
