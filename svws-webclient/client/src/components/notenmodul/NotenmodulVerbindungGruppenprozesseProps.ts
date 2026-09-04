import type { List } from "@core/java/util/List";
import type { WenomAuswahlListeManager } from "@ui/components/enm/WenomAuswahlListeManager";

export interface NotenmodulVerbindungGruppenprozesseProps {
	manager: () => WenomAuswahlListeManager;
	deleteVerbindung: () => Promise<[boolean, List<string | null>]>;
}
