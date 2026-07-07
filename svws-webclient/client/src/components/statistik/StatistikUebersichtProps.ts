import type { SchuleStammdaten, StatistikGesamt, ValidatorKontext } from "@core";

export interface StatistikUebersichtProps {
	validatorKontext: () => ValidatorKontext,
	schuleStammdaten: SchuleStammdaten;
	statistikGesamt: StatistikGesamt;
}
