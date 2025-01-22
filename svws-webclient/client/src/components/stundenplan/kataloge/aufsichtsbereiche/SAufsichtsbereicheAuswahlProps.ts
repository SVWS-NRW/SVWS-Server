import type { Aufsichtsbereich, StundenplanManager } from "@core";
import type { AbschnittAuswahlDaten } from "@ui";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface AufsichtsbereicheAuswahlProps {
	auswahl: Aufsichtsbereich | undefined;
	addEintrag: (religion: Aufsichtsbereich) => Promise<void>;
	deleteEintraege: (eintraege: Iterable<Aufsichtsbereich>) => Promise<void>;
	gotoEintrag: (eintrag: Aufsichtsbereich) => Promise<RoutingStatus>;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	stundenplanManager: () => StundenplanManager;
}