import type { KlassenDaten, Schueler, KlassenListeManager} from "@core";

export interface KlassenDatenProps {
	patch: (data : Partial<KlassenDaten>) => Promise<void>;
	klassenListeManager: () => KlassenListeManager;
	gotoSchueler: (eintrag: Schueler) => Promise<void>,
}