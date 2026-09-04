import type { List } from "@core/java/util/List";
import type { AnkreuzkompetenzenListeManager } from "@ui/ui/manager/kataloge/AnkreuzkompetenzenListeManager";

export interface AnkreuzkompetenzenGruppenprozesseProps {
	manager: () => AnkreuzkompetenzenListeManager;
	delete: () => Promise<[boolean, List<string | null>]>;
	deleteCheck: () => [boolean, List<string>];
	gotoDefaultView: (id: number | null) => Promise<void>;
}
