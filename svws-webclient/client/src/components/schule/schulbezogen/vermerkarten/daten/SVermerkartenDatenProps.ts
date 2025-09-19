import type { BenutzerKompetenz, SchuelerVermerkartZusammenfassung, VermerkartEintrag } from "@core";
import type { VermerkartenListeManager } from "@ui";

export interface VermerkartenDatenProps {
	patch: (data : Partial<VermerkartEintrag>) => Promise<void>;
	vermerkartenManager: () => VermerkartenListeManager,
	gotoSchueler : (schuelerVermerkartZusammenfassung : SchuelerVermerkartZusammenfassung) => Promise<void>;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
}
