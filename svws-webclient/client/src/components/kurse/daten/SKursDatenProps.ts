import type { KursDaten, Schueler, Schulform, KursListeManager } from "@core";

export interface KursDatenProps {
	schulform: Schulform;
	patch: (data : Partial<KursDaten>) => Promise<void>;
	kursListeManager: () => KursListeManager;
	setFilter: () => Promise<void>;
	gotoSchueler: (eintrag: Schueler) => Promise<void>,
}
