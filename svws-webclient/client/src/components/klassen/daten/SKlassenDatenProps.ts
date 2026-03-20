import type { LehrerListeEintrag, Schulform, KlasseDetails, Schueler, List, Schulgliederung, ServerMode, BenutzerKompetenz } from "@core";
import type { KlassenListeManager } from "@ui";

export interface KlassenDatenProps {
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	patch: (data: Partial<KlasseDetails>) => Promise<boolean>;
	manager: () => KlassenListeManager;
	mapKlassenVorigerAbschnitt: () => Map<number, KlasseDetails>;
	mapKlassenFolgenderAbschnitt: () => Map<number, KlasseDetails>;
	setFilter: () => Promise<void>;
	gotoSchueler: (eintrag: Schueler) => Promise<void>;
	gotoLehrer: (eintrag: LehrerListeEintrag) => Promise<void>;
	addKlassenleitung: (idLehrer: number, idKlasse: number) => Promise<void>;
	removeKlassenleitung: (eintrag: LehrerListeEintrag) => Promise<void>;
	updateReihenfolgeKlassenleitung: (idLehrer: number, erhoehe: boolean) => Promise<void>;
}
