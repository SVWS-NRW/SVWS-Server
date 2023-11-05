import type { LehrerListeEintrag, LehrerListeManager, Schuljahresabschnitt } from "@core";

export interface LehrerAuswahlProps {
	lehrerListeManager: () => LehrerListeManager;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	gotoLehrer: (value: LehrerListeEintrag) => Promise<void>;
	setFilter: () => Promise<void>;
}