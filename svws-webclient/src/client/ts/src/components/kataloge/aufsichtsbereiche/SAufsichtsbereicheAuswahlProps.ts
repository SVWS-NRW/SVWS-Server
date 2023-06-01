import type { Aufsichtsbereich, Schuljahresabschnitt } from "@svws-nrw/svws-core";

export interface AufsichtsbereicheAuswahlProps {
	auswahl: Aufsichtsbereich | undefined;
	mapKatalogeintraege: () => Map<number, Aufsichtsbereich>;
	addEintrag: (religion: Aufsichtsbereich) => Promise<void>;
	deleteEintraege: (eintraege: Aufsichtsbereich[]) => Promise<void>;
	gotoEintrag: (eintrag: Aufsichtsbereich) => Promise<void>;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	returnToKataloge: () => Promise<void>;
}