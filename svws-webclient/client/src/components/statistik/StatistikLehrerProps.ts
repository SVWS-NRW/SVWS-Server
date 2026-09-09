import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";

import type { LehrerIndividualdatenProps } from "../lehrer/individualdaten/LehrerIndividualdatenProps";
import type { LehrerPersonaldatenProps } from "../lehrer/personaldaten/LehrerPersonaldatenProps";

export interface StatistikLehrerProps extends Omit<LehrerPersonaldatenProps, "lehrerListeManager">, Omit<LehrerIndividualdatenProps, "lehrerListeManager"> {
	gotoLehrer: (lehrer: LehrerListeEintrag) => Promise<void>;
}
