import type { LehrerListeEintrag, KlassenDaten, Schueler, BenutzerKompetenz } from "@core";
import type { KlassenListeManager } from "@ui";

export interface KlassenDatenProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	patch: (data: Partial<KlassenDaten>) => Promise<boolean>;
	manager: () => KlassenListeManager;
	mapKlassenVorigerAbschnitt: () => Map<number, KlassenDaten>;
	mapKlassenFolgenderAbschnitt: () => Map<number, KlassenDaten>;
	setFilter: () => Promise<void>;
	gotoSchueler: (eintrag: Schueler) => Promise<void>;
	gotoLehrer: (eintrag: LehrerListeEintrag) => Promise<void>;
	addKlassenleitung: (idLehrer: number, idKlasse: number) => Promise<void>;
	removeKlassenleitung: (eintrag: LehrerListeEintrag) => Promise<void>;
	updateReihenfolgeKlassenleitung: (idLehrer: number, erhoehe: boolean) => Promise<void>;
}
