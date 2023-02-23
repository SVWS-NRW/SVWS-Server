import { SchuelerListeEintrag, SchuelerStammdaten, KlassenListeEintrag } from "@svws-nrw/svws-core-ts";

export interface SchuelerAppProps {
	auswahl: SchuelerListeEintrag | undefined;
	stammdaten: SchuelerStammdaten | undefined;
	mapKlassen: Map<Number, KlassenListeEintrag>;
}
