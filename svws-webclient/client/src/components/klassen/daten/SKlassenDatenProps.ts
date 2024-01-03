import type { Schulform, KlassenDaten, Schueler, KlassenListeManager, List, Schulgliederung, KlassenListeEintrag } from "@core";

export interface KlassenDatenProps {
	schulform: Schulform;
	schulgliederungen: List<Schulgliederung>;
	patch: (data : Partial<KlassenDaten>) => Promise<void>;
	klassenListeManager: () => KlassenListeManager;
	mapKlassenVorigerAbschnitt: () => Map<number, KlassenListeEintrag>;
	mapKlassenFolgenderAbschnitt: () => Map<number, KlassenListeEintrag>;
	gotoSchueler: (eintrag: Schueler) => Promise<void>;
}