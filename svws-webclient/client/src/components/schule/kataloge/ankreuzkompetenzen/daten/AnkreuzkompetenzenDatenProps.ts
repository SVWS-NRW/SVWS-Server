import type { Ankreuzkompetenz } from "@core/core/data/schule/Ankreuzkompetenz";
import type { List } from "@core/java/util/List";
import type { AnkreuzkompetenzenListeManager } from "@ui/ui/manager/kataloge/AnkreuzkompetenzenListeManager";

export interface AnkreuzkompetenzenDatenProps {
	patch: (data: Partial<Ankreuzkompetenz>) => Promise<boolean>;
	manager: () => AnkreuzkompetenzenListeManager;
	addJahrgaengezuordnungen: (idAnkreuzkompetenz: number, idsJahrgaenge: List<number>) => Promise<void>;
	deleteJahrgaengezuordnungen: (ids: List<number>) => Promise<void>;
}
