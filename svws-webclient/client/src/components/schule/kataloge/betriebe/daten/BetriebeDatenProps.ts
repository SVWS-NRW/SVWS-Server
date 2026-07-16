import type { Betrieb, BetriebeAnsprechpartner, List } from "@core";
import type { BetriebeListeManager } from "@ui";

export interface BetriebeDatenProps {
	manager: () => BetriebeListeManager,
	patch: (data: Partial<Betrieb>) => Promise<boolean>;
	addAnsprechpartner: (ansprechpartner: Partial<BetriebeAnsprechpartner>) => Promise<void>;
	deleteAnsprechpartner: (ids: List<number>) => Promise<void>;
	patchAnsprechpartner: (data: Partial<BetriebeAnsprechpartner>) => Promise<boolean>;
}
