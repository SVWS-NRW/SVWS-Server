import type { Aufsichtsbereich, BenutzerKompetenz, StundenplanManager } from "@core";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface AufsichtsbereicheAuswahlProps {
	auswahl: Aufsichtsbereich | undefined;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	addEintrag: (eintrag: Aufsichtsbereich) => Promise<void>;
	deleteEintraege: (eintraege: Iterable<Aufsichtsbereich>) => Promise<void>;
	gotoEintrag: (eintrag: Aufsichtsbereich) => Promise<RoutingStatus>;
	stundenplanManager: () => StundenplanManager;
	setKatalogAufsichtsbereicheImportJSON: (formData: FormData) => Promise<void>;
}