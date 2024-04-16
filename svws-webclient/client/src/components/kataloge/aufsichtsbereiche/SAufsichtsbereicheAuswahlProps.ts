import type { Aufsichtsbereich, Schuljahresabschnitt, StundenplanManager } from "@core";

export interface AufsichtsbereicheAuswahlProps {
	auswahl: Aufsichtsbereich | undefined;
	addEintrag: (religion: Aufsichtsbereich) => Promise<void>;
	deleteEintraege: (eintraege: Iterable<Aufsichtsbereich>) => Promise<void>;
	gotoEintrag: (eintrag: Aufsichtsbereich) => Promise<void>;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	returnToKataloge: () => Promise<void>;
	stundenplanManager: () => StundenplanManager;
}