import type { KlassenDaten } from "@core/asd/data/klassen/KlassenDaten";
import type { Schueler } from "@core/asd/data/schueler/Schueler";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { KlassenListeManager } from "~/states/klassen/KlassenListeManager";

export interface KlassenDatenProps {
	patch: (data: Partial<KlassenDaten>) => Promise<boolean>;
	manager: () => KlassenListeManager;
	setFilter: () => Promise<void>;
	gotoSchueler: (eintrag: Schueler) => Promise<void>;
	gotoLehrer: (eintrag: LehrerListeEintrag) => Promise<void>;
	addKlassenleitung: (idLehrer: number, idKlasse: number) => Promise<void>;
	removeKlassenleitung: (eintrag: LehrerListeEintrag) => Promise<void>;
	updateReihenfolgeKlassenleitung: (idLehrer: number, erhoehe: boolean) => Promise<void>;
}
