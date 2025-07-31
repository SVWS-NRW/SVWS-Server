import type { KursDaten, Schueler, Schulform, ServerMode, BenutzerKompetenz, KursLehrer, List } from "@core";
import type { KursListeManager } from "@ui";

export interface KursDatenProps {
	schulform: Schulform;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	patch: (data : Partial<KursDaten>) => Promise<void>;
	manager: () => KursListeManager;
	setFilter: () => Promise<void>;
	gotoSchueler: (eintrag: Schueler) => Promise<void>,
	addKursLehrer: (data: Partial<KursLehrer>, idKurs: number) => Promise<void>;
	patchKursLehrer: (data: Partial<KursLehrer>, idKurs: number, idLehrer: number) => Promise<void>;
	deleteKursLehrer: (lehrerIds: List<number>, idKurs: number) => Promise<void>;
}
