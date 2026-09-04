import type { GostFach } from "@core/core/data/gost/GostFach";
import type { GostHalbjahr } from "@core/core/types/gost/GostHalbjahr";


export type LaufbahnplanungUiStepperMode = 'manuell' | 'normal' | 'hochschreiben';

/**
 * Das Interface für die Ui-Stepper der Laufbahnplanung
 */
export interface LaufbahnplanungUiStepper {

	/**
	 * Stepper für das Durchwandern der Auswahloptionen im Abiturbereich. Diese Methode ist ein
	 * Einsprungspunkt für die vue-Komponente und wählt je nach Modus die geeignete Methode aus.
	 *
	 * @param fach   das Fach
	 *
	 * @returns -
	 */
	stepperAbitur(fach: GostFach): Promise<void>;

	/**
	 * Stepper für das Durchwandern der Referenzfächer bei einem Projektkurs-Fach.
	 *
	 * @param fach   das Projektkurs-Fach
	 *
	 * @returns -
	 */
	stepperReferenzfach(fach: GostFach): Promise<void>;

	/**
	 * Stepper für das Durchwandern der Auswahloptionen im Bereich der Halbjahre eines Faches. Diese Methode ist ein
	 * Einsprungspunkt für die vue-Komponente und wählt je nach Modus die geeignete Methode aus.
	 *
	 * @param fach       das Fach
	 * @param halbjahr   das Halbjahr
	 * @param
	 *
	 * @returns -
	 */
	stepper(fach: GostFach, halbjahr: GostHalbjahr, modus?: LaufbahnplanungUiStepperMode): Promise<void>;

}
