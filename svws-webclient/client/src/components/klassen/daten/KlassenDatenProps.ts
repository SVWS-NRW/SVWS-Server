import type { LehrerListeEintrag, KlassenDaten, Schueler, BenutzerKompetenz } from "@core";
import type { KlassenListeManager } from "~/states/klassen/KlassenListeManager";

export interface KlassenDatenProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	patch: (data: Partial<KlassenDaten>) => Promise<boolean>;
	manager: () => KlassenListeManager;
	setFilter: () => Promise<void>;
	gotoSchueler: (eintrag: Schueler) => Promise<void>;
	gotoLehrer: (eintrag: LehrerListeEintrag) => Promise<void>;
	addKlassenleitung: (idLehrer: number, idKlasse: number) => Promise<void>;
	removeKlassenleitung: (eintrag: LehrerListeEintrag) => Promise<void>;
	updateReihenfolgeKlassenleitung: (idLehrer: number, erhoehe: boolean) => Promise<void>;
}
