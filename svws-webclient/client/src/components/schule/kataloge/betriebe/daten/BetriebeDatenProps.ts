import type { BenutzerKompetenz, Betrieb, BetriebeAnsprechpartner, List } from "@core";
import type { BetriebeListeManager } from "../../../../../../../ui/src/ui/manager/kataloge/BetriebeListeManager";

export interface BetriebeDatenProps {
	manager: () => BetriebeListeManager,
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	patch: (data: Partial<Betrieb>) => Promise<void>;
	addAnsprechpartner: (ansprechpartner: Partial<BetriebeAnsprechpartner>) => Promise<void>;
	deleteAnsprechpartner: (ids: List<number>) => Promise<void>;
	patchAnsprechpartner: (id: number, data: Partial<BetriebeAnsprechpartner>) => Promise<void>;
}
