import type { Betrieb } from "@core/core/data/schule/Betrieb";
import type { BetriebeAnsprechpartner } from "@core/core/data/schule/BetriebeAnsprechpartner";
import type { List } from "@core/java/util/List";
import type { BetriebeListeManager } from "@ui/ui/manager/kataloge/BetriebeListeManager";

export interface BetriebeDatenProps {
	manager: () => BetriebeListeManager,
	patch: (data: Partial<Betrieb>) => Promise<boolean>;
	addAnsprechpartner: (ansprechpartner: Partial<BetriebeAnsprechpartner>) => Promise<void>;
	deleteAnsprechpartner: (ids: List<number>) => Promise<void>;
	patchAnsprechpartner: (data: Partial<BetriebeAnsprechpartner>) => Promise<boolean>;
}
