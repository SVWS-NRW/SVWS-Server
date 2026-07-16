import type { KursDaten, Schueler, KursLehrer, List } from "@core";
import type { KursListeManager } from "~/states/kurse/KursListeManager";

export interface KursDatenProps {
	patch: (data: Partial<KursDaten>) => Promise<boolean>;
	manager: () => KursListeManager;
	setFilter: () => Promise<void>;
	gotoSchueler: (eintrag: Schueler) => Promise<void>,
	addKursLehrer: (data: Partial<KursLehrer>, idKurs: number) => Promise<void>;
	patchKursLehrer: (data: Partial<KursLehrer>, idKurs: number, idLehrer: number) => Promise<void>;
	deleteKursLehrer: (lehrerIds: List<number>, idKurs: number) => Promise<void>;
}
