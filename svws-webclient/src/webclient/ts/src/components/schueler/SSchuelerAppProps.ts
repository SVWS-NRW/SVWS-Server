import { SchuelerListeEintrag, SchuelerStammdaten, KlassenListeEintrag } from "@svws-nrw/svws-core-ts";

export interface SchuelerAppProps {
	auswahl: SchuelerListeEintrag | undefined;
	stammdaten: SchuelerStammdaten;
	mapKlassen: Map<Number, KlassenListeEintrag>;
}
