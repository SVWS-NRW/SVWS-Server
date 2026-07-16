import type { List, SchuelerFoerderempfehlung } from "@core";

export interface SchuelerLernabschnittFoerderempfehlungenProps {
	foerderempfehlungen: () => List<SchuelerFoerderempfehlung>;
	add: (data: Partial<SchuelerFoerderempfehlung>) => Promise<void>;
	patch: (data: Partial<SchuelerFoerderempfehlung>, guid: string) => Promise<boolean>;
	delete: (guIDs: List<string>) => Promise<void>;
}
