import type { KursDaten, Schueler, Schulform, KursListeManager, ServerMode, BenutzerKompetenz } from "@core";

export interface KursDatenProps {
	schulform: Schulform;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	patch: (data : Partial<KursDaten>) => Promise<void>;
	manager: () => KursListeManager;
	setFilter: () => Promise<void>;
	gotoSchueler: (eintrag: Schueler) => Promise<void>,
}
