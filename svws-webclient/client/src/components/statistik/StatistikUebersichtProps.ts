import type { SchuleStammdaten } from "@core/asd/data/schule/SchuleStammdaten";
import type { StatistikGesamt } from "@core/asd/data/statistik/StatistikGesamt";
import type { ValidatorKontext } from "@core/asd/validate/ValidatorKontext";

export interface StatistikUebersichtProps {
	validatorKontext: () => ValidatorKontext,
	schuleStammdaten: SchuleStammdaten;
	statistikGesamt: StatistikGesamt;
}
