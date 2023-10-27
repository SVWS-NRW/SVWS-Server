import type { SchuelerListeEintrag, Schuljahresabschnitt, SchuelerListeManager } from "@core";

export interface SchuelerAuswahlProps {
	auswahl: SchuelerListeEintrag | null;
	schuelerListeManager: () => SchuelerListeManager;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	gotoSchueler: (value: SchuelerListeEintrag | null) => Promise<void>;
	setFilter: () => Promise<void>;
}

