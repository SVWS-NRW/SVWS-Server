import type { Aufsichtsbereich } from "@core/core/data/schule/Aufsichtsbereich";
import type { StundenplanManager } from "@core/core/utils/stundenplan/StundenplanManager";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface AufsichtsbereicheAuswahlProps {
	auswahl: Aufsichtsbereich | undefined;
	addEintrag: (eintrag: Aufsichtsbereich) => Promise<void>;
	deleteEintraege: (eintraege: Iterable<Aufsichtsbereich>) => Promise<void>;
	gotoEintrag: (eintrag: Aufsichtsbereich) => Promise<RoutingStatus>;
	stundenplanManager: () => StundenplanManager;
	setKatalogAufsichtsbereicheImportJSON: (formData: FormData) => Promise<void>;
}