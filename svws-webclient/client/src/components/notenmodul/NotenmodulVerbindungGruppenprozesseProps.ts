import type { List } from "@core";
import type { WenomAuswahlListeManager } from "@ui";

export interface NotenmodulVerbindungGruppenprozesseProps {
	manager: () => WenomAuswahlListeManager;
	deleteVerbindung: () => Promise<[boolean, List<string | null>]>;
}
