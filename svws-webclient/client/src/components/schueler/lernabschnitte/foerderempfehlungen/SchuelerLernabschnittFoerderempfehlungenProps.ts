import type { BenutzerKompetenz, List, SchuelerFoerderempfehlung } from "@core";

export interface SchuelerLernabschnittFoerderempfehlungenProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>;
	foerderempfehlungen: () => List<SchuelerFoerderempfehlung>;
	add: (data: Partial<SchuelerFoerderempfehlung>) => Promise<void>;
	patch: (data: Partial<SchuelerFoerderempfehlung>, guid: string) => Promise<void>;
	delete: (guIDs: List<string>) => Promise<void>;
}
