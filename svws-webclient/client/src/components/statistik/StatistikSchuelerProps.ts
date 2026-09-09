import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";

import type { SchuelerIndividualdatenProps } from "../schueler/individualdaten/SchuelerIndividualdatenProps";

export interface StatistikSchuelerProps extends Omit<SchuelerIndividualdatenProps, "schuelerListeManager"> {
	gotoSchueler: (lehrer: SchuelerListeEintrag) => Promise<void>;
}
