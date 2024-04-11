import type { StundenplanPausenzeit, Schuljahresabschnitt, StundenplanManager } from "@core";

export interface PausenzeitenAuswahlProps {
	auswahl: StundenplanPausenzeit | undefined;
	addEintrag: (eintrag: Partial<StundenplanPausenzeit>) => Promise<void>;
	deleteEintraege: (eintraege: Iterable<StundenplanPausenzeit>) => Promise<void>;
	gotoEintrag: (eintrag: StundenplanPausenzeit) => Promise<void>;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	returnToKataloge: () => Promise<void>;
	setKatalogPausenzeitenImportJSON: (formData: FormData) => Promise<void>;
	stundenplanManager: () => StundenplanManager;
}