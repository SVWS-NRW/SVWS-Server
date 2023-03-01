import { SchuelerListeEintrag, SchuelerStammdaten, KlassenListeEintrag } from "@svws-nrw/svws-core-ts";
import { AuswahlChildData } from "../AuswahlChildData";

export interface SchuelerAppProps {
	auswahl: SchuelerListeEintrag | undefined;
	stammdaten: SchuelerStammdaten | undefined;
	mapKlassen: Map<Number, KlassenListeEintrag>;
	setChild: (value: AuswahlChildData) => Promise<void>;
	child: AuswahlChildData;
	children: AuswahlChildData[];
	childrenHidden: boolean[];
}
