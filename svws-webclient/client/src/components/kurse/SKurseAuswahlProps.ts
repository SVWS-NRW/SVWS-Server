import type { KursDaten, KursListeManager, Schuljahresabschnitt } from "@core";

export interface KurseAuswahlProps {
	kursListeManager: () => KursListeManager;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	gotoEintrag: (eintrag: KursDaten) => Promise<void>;
	setFilter: () => Promise<void>;
}