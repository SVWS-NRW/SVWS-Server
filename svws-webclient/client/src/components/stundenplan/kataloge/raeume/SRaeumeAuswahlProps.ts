import type { Raum } from "@core/core/data/schule/Raum";
import type { RaumListeManager } from "@ui/ui/manager/raum/RaumListeManager";

export interface RaeumeAuswahlProps {
	raumListeManager: () => RaumListeManager;
	addEintrag: (eintrag: Raum) => Promise<void>;
	deleteEintraege: (eintraege: Iterable<Raum>) => Promise<void>;
	gotoEintrag: (eintrag: Raum) => Promise<void>;
	setKatalogRaeumeImportJSON: (formData: FormData) => Promise<void>;
}
