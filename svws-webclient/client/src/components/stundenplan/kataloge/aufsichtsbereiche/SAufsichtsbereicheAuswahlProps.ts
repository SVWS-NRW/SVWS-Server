import type { Aufsichtsbereich, StundenplanManager } from "@core";
import type { AbschnittAuswahlDaten } from "@comp";

export interface AufsichtsbereicheAuswahlProps {
	auswahl: Aufsichtsbereich | undefined;
	addEintrag: (religion: Aufsichtsbereich) => Promise<void>;
	deleteEintraege: (eintraege: Iterable<Aufsichtsbereich>) => Promise<void>;
	gotoEintrag: (eintrag: Aufsichtsbereich) => Promise<void>;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	returnToKataloge: () => Promise<void>;
	returnToStundenplan: () => Promise<void>;
	stundenplanManager: () => StundenplanManager;
}