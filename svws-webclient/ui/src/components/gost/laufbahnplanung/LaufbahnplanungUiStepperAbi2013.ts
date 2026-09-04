import { Fachgruppe } from "@core/asd/types/fach/Fachgruppe";
import type { AbiturdatenManager } from "@core/core/abschluss/gost/AbiturdatenManager";
import type { GostFach } from "@core/core/data/gost/GostFach";
import type { GostJahrgangsdaten } from "@core/core/data/gost/GostJahrgangsdaten";
import type { GostSchuelerFachwahl } from "@core/core/data/gost/GostSchuelerFachwahl";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { GostAbiturFach } from "@core/core/types/gost/GostAbiturFach";
import { GostFachbereich } from "@core/core/types/gost/GostFachbereich";
import { GostHalbjahr } from "@core/core/types/gost/GostHalbjahr";
import { GostKursart } from "@core/core/types/gost/GostKursart";
import { GostFachUtils } from "@core/core/utils/gost/GostFachUtils";
import { useGostLaufbahnplanungState } from "@ui/states/GostLaufbahnplanungState";
import type { LaufbahnplanungUiManager } from "./LaufbahnplanungUiManager";
import type { LaufbahnplanungUiStepper, LaufbahnplanungUiStepperMode } from "./LaufbahnplanungUiStepper";


/**
 * Eine Klasse für die Ui-Stepper im Falle der Abiturregelung bis Abitur 2029
 */
export class LaufbahnplanungUiStepperAbi2013 implements LaufbahnplanungUiStepper {

	/** Der aktuelle State für die Laufbahnplanung der Gymnasialen Oberstufe */
	private readonly gostLaufbahnplanungState = useGostLaufbahnplanungState();

	/** der aktuelle UiManger, welcher diesen Stepper erzeugt hat */
	private readonly uiManager: LaufbahnplanungUiManager;

	/**
	 * Erzeugt einen neuen Stepper für die Eingabe
	 *
	 * @param uiManager   der Ui-Manager für die Laufbahnplanung
	 */
	public constructor(uiManager: LaufbahnplanungUiManager) {
		this.uiManager = uiManager;
	}


	private get manager(): AbiturdatenManager {
		if (!this.gostLaufbahnplanungState.valid) {
			throw new DeveloperNotificationException("Es wurde auf den State der Laufbahnplanung zugegriffen, obwohl dieser nicht valide ist.");
		}
		return this.gostLaufbahnplanungState.abiturdatenManager;
	}

	private get jahrgang(): GostJahrgangsdaten {
		if (!this.gostLaufbahnplanungState.valid) {
			throw new DeveloperNotificationException("Es wurde auf den State der Laufbahnplanung zugegriffen, obwohl dieser nicht valide ist.");
		}
		return this.gostLaufbahnplanungState.gostJahrgangsdaten;
	}

	private async setWahl(idFach: number, wahl: GostSchuelerFachwahl) {
		if (!this.gostLaufbahnplanungState.valid) {
			throw new DeveloperNotificationException("Es wurde auf den State der Laufbahnplanung zugegriffen, obwohl dieser nicht valide ist.");
		}
		await this.gostLaufbahnplanungState.setWahl(idFach, wahl);
	}

	/**
	 * Stepper für das Durchwandern der Auswahloptionen im Abiturbereich eines Faches
	 * im manuellen Modus.
	 *
	 * @param fach   das Fach
	 *
	 * @returns -
	 */
	private async stepperAbiturManuell(fach: GostFach): Promise<void> {
		if (this.manager.istBewertet(GostHalbjahr.Q22)) {
			return;
		}
		const wahl = this.manager.getSchuelerFachwahl(fach.id);
		if (wahl.halbjahre[GostHalbjahr.Q22.id] === null) {
			return;
		}
		switch (wahl.abiturFach) {
			case null:
				wahl.abiturFach = (wahl.halbjahre[GostHalbjahr.Q22.id] === "LK") ? 1 : 3;
				break;
			case 1:
				wahl.abiturFach = (wahl.halbjahre[GostHalbjahr.Q22.id] === "LK") ? 2 : 3;
				break;
			case 2:
				wahl.abiturFach = (wahl.halbjahre[GostHalbjahr.Q22.id] === "LK") ? null : 3;
				break;
			case 3:
				wahl.abiturFach = (wahl.halbjahre[GostHalbjahr.Q22.id] === "LK") ? null : 4;
				break;
			case 4:
				wahl.abiturFach = null;
				break;
			default:
				wahl.abiturFach = null;
				break;
		}
		await this.setWahl(fach.id, wahl);
	}

	/**
	 * Stepper für das Durchwandern der Auswahloptionen im Abiturbereich eines Faches
	 * im normalen Modus und im Hochschreibemodus.
	 *
	 * @param fach   das Fach
	 *
	 * @returns -
	 */
	private async stepperAbiturNormal(fach: GostFach) {
		// Prüfe, ob die Wahl als Abiturfach überhaupt möglich ist
		if (!this.uiManager.istMoeglichAbi(fach)) {
			return;
		}
		// Bestimme die Fachwahl des Schüler und die mögliche Kursart im Abitur.
		const wahl = this.manager.getSchuelerFachwahl(fach.id);
		const abiMoeglicheKursart = this.uiManager.getMoeglicheAbiKursart(fach);
		// Keine Kursart im Abitur möglich...
		if (abiMoeglicheKursart === null) {
			wahl.abiturFach = null;
			return;
		}
		// Die mögliche Kursart im Abitur ist LK (Leistungskurs)
		if (abiMoeglicheKursart === GostKursart.LK) {
			switch (wahl.abiturFach) {
				case 1:
					wahl.abiturFach = 2;
					break;
				case 2:
					if (GostFachUtils.istWaehlbarLeistungskurs1(fach)) {
						wahl.abiturFach = 1;
					}
					break;
				default:
					if (GostFachUtils.istWaehlbarLeistungskurs1(fach) && !this.manager.hatAbiFach(GostAbiturFach.LK1)) {
						wahl.abiturFach = 1;
					} else {
						wahl.abiturFach = 2;
					}
					break;
			}
		}
		// Die mögliche Kursart im Abitur ist GK (Grundkurs)
		if (abiMoeglicheKursart === GostKursart.GK) {
			switch (wahl.abiturFach) {
				case null:
					wahl.abiturFach = (wahl.halbjahre[GostHalbjahr.Q22.id] === "M") ? 4 : 3;
					break;
				case 4:
					wahl.abiturFach = (wahl.halbjahre[GostHalbjahr.Q22.id] === "S") ? 3 : null;
					break;
				case 3:
					wahl.abiturFach = (wahl.halbjahre[GostHalbjahr.Q22.id] === "M") ? 4 : null;
					break;
				default:
					wahl.abiturFach = null;
					break;
			}
		}
		await this.setWahl(fach.id, wahl);
	}


	/**
	 * Stepper für das Durchwandern der Auswahloptionen im Abiturbereich. Diese Methode ist ein
	 * Einsprungspunkt für die vue-Komponente und wählt je nach Modus die geeignete Methode aus.
	 *
	 * @param fach   das Fach
	 *
	 * @returns -
	 */
	public async stepperAbitur(fach: GostFach) {
		switch (this.uiManager.modus) {
			case 'manuell':
				await this.stepperAbiturManuell(fach);
				return;
			case 'normal':
			case 'hochschreiben':
				await this.stepperAbiturNormal(fach);
				return;
		}
	}


	public async stepperReferenzfach(fach: GostFach) {
		return;
	}


	/**
	 * Die Stepper-Methode für die Fachwahlen in der EF.1
	 *
	 * @param fach   das Fach
	 * @param wahl   die Fachwahl
	 */
	private stepEF1Wahl(fach: GostFach, wahl: GostSchuelerFachwahl): void {
		const istVTF = (this.uiManager.getFachgruppe(fach) === Fachgruppe.FG_VX);
		const istPJK = (this.uiManager.getFachgruppe(fach) === Fachgruppe.FG_PX);
		switch (wahl.halbjahre[GostHalbjahr.EF1.id]) {
			case null:
				if ((istVTF || istPJK) || (GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && !fach.istMoeglichAbiLK)) {
					wahl.halbjahre[GostHalbjahr.EF1.id] = "M";
				} else {
					wahl.halbjahre[GostHalbjahr.EF1.id] = "S";
				}
				break;
			case "S":
				wahl.halbjahre[GostHalbjahr.EF1.id] = "M";
				break;
			case "M":
				if (GostFachbereich.SPORT.hat(fach)) {
					wahl.halbjahre[GostHalbjahr.EF1.id] = "AT";
				} else {
					wahl.halbjahre[GostHalbjahr.EF1.id] = null;
				}
				break;
			case "AT":
				wahl.halbjahre[GostHalbjahr.EF1.id] = null;
				break;
		}
	}


	/**
	 * Die Stepper-Methode für die Fachwahlen in der EF.1
	 *
	 * @param fach   das Fach
	 * @param wahl   die Fachwahl
	 */
	private stepEF2Wahl(fach: GostFach, wahl: GostSchuelerFachwahl): void {
		const istVTF = (this.uiManager.getFachgruppe(fach) === Fachgruppe.FG_VX);
		const istPJK = (this.uiManager.getFachgruppe(fach) === Fachgruppe.FG_PX);
		switch (wahl.halbjahre[GostHalbjahr.EF2.id]) {
			case null:
				if ((istVTF || istPJK) || (GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && !fach.istMoeglichAbiLK)) {
					wahl.halbjahre[GostHalbjahr.EF2.id] = "M";
				} else {
					wahl.halbjahre[GostHalbjahr.EF2.id] = "S";
				}
				break;
			case "S":
				wahl.halbjahre[GostHalbjahr.EF2.id] = "M";
				break;
			case "M":
				if (GostFachbereich.SPORT.hat(fach)) {
					wahl.halbjahre[GostHalbjahr.EF2.id] = "AT";
				} else {
					wahl.halbjahre[GostHalbjahr.EF2.id] = null;
				}
				break;
			case "AT":
				wahl.halbjahre[GostHalbjahr.EF2.id] = null;
				break;
		}
	}

	/**
	 * Prüft, ob das übergebene Fach in dem übergebenen Halbjahr wählbar ist oder nicht.
	 *
	 * @param fach       das Fach
	 * @param halbjahr   das Halbjahr
	 *
	 * @returns true, wenn das Fach in dem Halbjahr wählbar ist, und ansonsten false
	 */
	private istFachWaehlbar(fach: GostFach, halbjahr: GostHalbjahr): boolean {
		switch (halbjahr) {
			case GostHalbjahr.EF1: return fach.istMoeglichEF1;
			case GostHalbjahr.EF2: return fach.istMoeglichEF2;
			case GostHalbjahr.Q11: return fach.istMoeglichQ11;
			case GostHalbjahr.Q12: return fach.istMoeglichQ12;
			case GostHalbjahr.Q21: return fach.istMoeglichQ21;
			case GostHalbjahr.Q22: return fach.istMoeglichQ22;
		}
		return false;
	}

	/**
	 * Prüft, ob bei dem übergebenen Fach die Fachwahlen im wählbaren Bereich mit denen aus dem Array für die
	 * Qualifikationsphase übereinstimmen.
	 *
	 * @param fach   das Fach
	 * @param wahl   die Fachwahlen
	 * @param a      das Array für die QPhase zum Abgleich mit den Fachwahlen
	 *
	 * @returns true, wenn die Wahl übereinstimmt, und ansonsten false
	 */
	private istWahlInQPhase(fach: GostFach, wahl: GostSchuelerFachwahl, a: Array<string | null>): boolean {
		for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
			if ((wahl.halbjahre[halbjahr.id] !== a[halbjahr.id - 2]) && this.istFachWaehlbar(fach, halbjahr)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Prüft, ob bei dem übergebenen Fach die Fachwahlen im wählbaren Bereich mit denen aus dem Array übereinstimmen.
	 *
	 * @param fach   das Fach
	 * @param wahl   die Fachwahlen
	 * @param a      das Array zum Abgleich mit den Fachwahlen
	 *
	 * @returns true, wenn die Wahl übereinstimmt, und ansonsten false
	 */
	private istWahl(fach: GostFach, wahl: GostSchuelerFachwahl, a: Array<string | null>) {
		for (const halbjahr of GostHalbjahr.values()) {
			if ((wahl.halbjahre[halbjahr.id] !== a[halbjahr.id]) && this.istFachWaehlbar(fach, halbjahr)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Die Stepper-Methode für die Fachwahlen in der EF.1 im Hochschreibe-Modus
	 *
	 * @param fach   das Fach
	 * @param wahl   die Fachwahl
	 */
	private stepEF1WahlHochschreiben(fach: GostFach, wahl: GostSchuelerFachwahl): void {
		const istVTF = (this.uiManager.getFachgruppe(fach) === Fachgruppe.FG_VX);
		const istPJK = (this.uiManager.getFachgruppe(fach) === Fachgruppe.FG_PX);
		switch (wahl.halbjahre[GostHalbjahr.EF1.id]) {
			case null: {
				if (wahl.abiturFach !== null) {
					wahl.halbjahre[GostHalbjahr.EF1.id] = 'S';
				// Prüfe, ob die Folgehalbjahre auch leer sind, dann setze auch diese
				} else if (this.istWahl(fach, wahl, [null, null, null, null, null, null]) && !(istVTF || istPJK)) {
					if ((GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && !fach.istMoeglichAbiLK)) {
						wahl.halbjahre = ['M', 'M', 'M', 'M', 'M', 'M'];
					} else if ((GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && fach.istMoeglichAbiLK)) {
						wahl.halbjahre = ['S', 'S', 'LK', 'LK', 'LK', 'LK'];
					} else {
						wahl.halbjahre = [
							fach.istMoeglichEF1 ? 'S' : null,
							fach.istMoeglichEF2 ? 'S' : null,
							fach.istMoeglichQ11 ? 'S' : null,
							fach.istMoeglichQ12 ? 'S' : null,
							fach.istMoeglichQ21 ? 'S' : null,
							fach.istMoeglichQ22 ? 'M' : null,
						];
					}
				} else {
					if (istVTF || istPJK || (GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && !fach.istMoeglichAbiLK)) {
						wahl.halbjahre[GostHalbjahr.EF1.id] = "M";
					} else {
						wahl.halbjahre[GostHalbjahr.EF1.id] = "S";
					}
				}
				break;
			}
			case "S": {
				if (wahl.abiturFach !== null) {
					wahl.halbjahre[GostHalbjahr.EF1.id] = 'M';
				} else if (this.istWahl(fach, wahl, ['S', 'S', 'S', 'S', 'S', 'M']) && !(istVTF || istPJK)) {
					// Prüfe, ob die Folgehalbjahre S,S,S,S,M sind und Abi-Fach nicht gesetzt (Spezialfälle berücksichtigen KU+MU+RE)
					if (GostFachbereich.KUNST_MUSIK.hat(fach) || GostFachbereich.RELIGION.hat(fach)) {
						wahl.halbjahre = ['M', 'M', 'M', 'M', null, null];
					} else {
						wahl.halbjahre = [
							fach.istMoeglichEF1 ? 'M' : null,
							fach.istMoeglichEF2 ? 'M' : null,
							fach.istMoeglichQ11 ? 'M' : null,
							fach.istMoeglichQ12 ? 'M' : null,
							fach.istMoeglichQ21 ? 'M' : null,
							fach.istMoeglichQ22 ? 'M' : null,
						];
					}
				} else if (this.istWahl(fach, wahl, ['S', 'S', 'LK', 'LK', 'LK', 'LK']) && (GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && fach.istMoeglichAbiLK)) {
					wahl.halbjahre = ['M', 'M', 'M', 'M', 'M', 'M'];
				} else {
					wahl.halbjahre[GostHalbjahr.EF1.id] = "M";
				}
				break;
			}
			case "M": {
				if (wahl.abiturFach !== null) {
					wahl.halbjahre[GostHalbjahr.EF1.id] = 'S';
				// Prüfe, ob die Folgehalbjahre M,M,M,M?,M? sind und passe diese an (Spezialfälle berücksichtigen KU+MU+RE)
				} else if ((this.istWahl(fach, wahl, ['M', 'M', 'M', 'M', 'M', 'M']) || this.istWahl(fach, wahl, ['M', 'M', 'M', 'M', null, null])) && !(istVTF || istPJK)) {
					if (GostFachbereich.SPORT.hat(fach)) {
						wahl.halbjahre = ["AT", "AT", "AT", "AT", "AT", "AT"];
					} else {
						wahl.halbjahre = [null, null, null, null, null, null];
					}
				} else {
					if (GostFachbereich.SPORT.hat(fach)) {
						wahl.halbjahre[GostHalbjahr.EF1.id] = "AT";
					} else {
						wahl.halbjahre[GostHalbjahr.EF1.id] = null;
					}
				}
				break;
			}
			case "AT": {
				if (wahl.abiturFach !== null) {
					wahl.halbjahre[GostHalbjahr.EF1.id] = 'S';
				} else if (this.istWahl(fach, wahl, ["AT", "AT", "AT", "AT", "AT", "AT"])) {
					wahl.halbjahre = [null, null, null, null, null, null];
				} else {
					wahl.halbjahre[GostHalbjahr.EF1.id] = null;
				}
				break;
			}
		}
	}


	/**
	 * Die Stepper-Methode für die Fachwahlen in der EF.2 im Hochschreibe-Modus
	 *
	 * @param fach   das Fach
	 * @param wahl   die Fachwahl
	 */
	private stepEF2WahlHochschreiben(fach: GostFach, wahl: GostSchuelerFachwahl): void {
		const istVTF = (this.uiManager.getFachgruppe(fach) === Fachgruppe.FG_VX);
		const istPJK = (this.uiManager.getFachgruppe(fach) === Fachgruppe.FG_PX);
		switch (wahl.halbjahre[GostHalbjahr.EF2.id]) {
			case null: {
				if (wahl.abiturFach !== null) {
					wahl.halbjahre[GostHalbjahr.EF2.id] = 'S';
				} else if (this.istWahlInQPhase(fach, wahl, [null, null, null, null]) && !(istVTF || istPJK)) {
					if ((GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && !fach.istMoeglichAbiLK)) {
						wahl.halbjahre = [wahl.halbjahre[0], 'M', 'M', 'M', 'M', 'M'];
					} else if ((GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && fach.istMoeglichAbiLK)) {
						wahl.halbjahre = [wahl.halbjahre[0], 'S', 'LK', 'LK', 'LK', 'LK'];
					} else {
						wahl.halbjahre = [
							wahl.halbjahre[0],
							fach.istMoeglichEF2 ? 'S' : null,
							fach.istMoeglichQ11 ? 'S' : null,
							fach.istMoeglichQ12 ? 'S' : null,
							fach.istMoeglichQ21 ? 'S' : null,
							fach.istMoeglichQ22 ? 'M' : null,
						];
					}
				} else {
					if (istVTF || istPJK || (GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && !fach.istMoeglichAbiLK)) {
						wahl.halbjahre[GostHalbjahr.EF2.id] = "M";
					} else {
						wahl.halbjahre[GostHalbjahr.EF2.id] = "S";
					}
				}
				break;
			}
			case "S": {
				if (wahl.abiturFach !== null) {
					wahl.halbjahre[GostHalbjahr.EF2.id] = 'M';
				} else if ((this.istWahlInQPhase(fach, wahl, [null, null, null, null])
					|| this.istWahlInQPhase(fach, wahl, ['S', 'S', 'S', 'M'])) && !(istVTF || istPJK)) {
					if (GostFachbereich.KUNST_MUSIK.hat(fach) || GostFachbereich.RELIGION.hat(fach)) {
						wahl.halbjahre = [wahl.halbjahre[0], 'M', 'M', 'M', null, null];
					} else {
						wahl.halbjahre = [
							wahl.halbjahre[0],
							fach.istMoeglichEF2 ? 'M' : null,
							fach.istMoeglichQ11 ? 'M' : null,
							fach.istMoeglichQ12 ? 'M' : null,
							fach.istMoeglichQ21 ? 'M' : null,
							fach.istMoeglichQ22 ? 'M' : null,
						];
					}
				} else if (this.istWahlInQPhase(fach, wahl, ['LK', 'LK', 'LK', 'LK']) && (GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && fach.istMoeglichAbiLK)) {
					wahl.halbjahre = [wahl.halbjahre[0], 'M', 'M', 'M', 'M', 'M'];
				} else {
					wahl.halbjahre[GostHalbjahr.EF2.id] = "M";
				}
				break;
			}
			case "M": {
				if (wahl.abiturFach !== null) {
					wahl.halbjahre[GostHalbjahr.EF2.id] = 'S';
				} else if ((this.istWahlInQPhase(fach, wahl, [null, null, null, null])
					|| this.istWahlInQPhase(fach, wahl, ['M', 'M', 'M', 'M'])
					|| this.istWahlInQPhase(fach, wahl, ['M', 'M', null, null])) && !(istVTF || istPJK)) {
					if (GostFachbereich.SPORT.hat(fach)) {
						wahl.halbjahre = [wahl.halbjahre[0], "AT", "AT", "AT", "AT", "AT"];
					} else {
						wahl.halbjahre = [wahl.halbjahre[0], null, null, null, null, null];
					}
				} else {
					if (GostFachbereich.SPORT.hat(fach)) {
						wahl.halbjahre[GostHalbjahr.EF2.id] = "AT";
					} else {
						wahl.halbjahre[GostHalbjahr.EF2.id] = null;
					}
				}
				break;
			}
			case "AT": {
				if (wahl.abiturFach !== null) {
					wahl.halbjahre[GostHalbjahr.EF2.id] = 'S';
				} else if (this.istWahl(fach, wahl, [wahl.halbjahre[0], "AT", "AT", "AT", "AT", "AT"])) {
					wahl.halbjahre = [wahl.halbjahre[0], null, null, null, null, null];
				} else {
					wahl.halbjahre[GostHalbjahr.EF2.id] = null;
				}
			}
		}
	}


	/**
	 * Hilfsmethode für die Stepper, um zu prüfen, ob bei den Fachwahlen in dem übergebenen Halbjahr eine
	 * Belegung vorliegt oder nicht.
	 *
	 * @param fachwahl   die Fachwahlen
	 * @param halbjahr   das Halbjahr
	 *
	 * @returns true, wenn eine Belegung vorliegt und ansonsten false
	 */
	private hatSchuelerFachwahl(fachwahl: GostSchuelerFachwahl | null, halbjahr: GostHalbjahr): boolean {
		if (fachwahl === null) {
			return false;
		}
		return fachwahl.halbjahre[halbjahr.id] !== null;
	}


	/**
	 * Die Stepper-Methode für die Fachwahlen in der Q1.1
	 *
	 * @param fach   das Fach
	 * @param wahl   die Fachwahl
	 */
	private stepQ11Wahl(fach: GostFach, wahl: GostSchuelerFachwahl): void {
		const istVTF = (this.uiManager.getFachgruppe(fach) === Fachgruppe.FG_VX);
		const istPJK = (this.uiManager.getFachgruppe(fach) === Fachgruppe.FG_PX);
		switch (wahl.halbjahre[GostHalbjahr.Q11.id]) {
			case null:
				wahl.halbjahre[GostHalbjahr.Q11.id] = (GostFachbereich.DEUTSCH.hat(fach) || GostFachbereich.MATHEMATIK.hat(fach)) ? "S" : "M";
				break;
			case "M":
				if (istVTF || istPJK || GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(fach)) {
					wahl.halbjahre[GostHalbjahr.Q11.id] = null;
				} else if (GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && !fach.istMoeglichAbiLK) {
					wahl.halbjahre[GostHalbjahr.Q11.id] = null;
				} else if (GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && fach.istMoeglichAbiLK) {
					wahl.halbjahre[GostHalbjahr.Q11.id] = "LK";
				} else {
					wahl.halbjahre[GostHalbjahr.Q11.id] = "S";
				}
				break;
			case "S":
				// S->S ist richtig, weil DE und MA muss belegt sein, entweder S oder LK, anders geht es nicht.
				wahl.halbjahre[GostHalbjahr.Q11.id] = (fach.istMoeglichAbiLK) ? "LK" : (GostFachbereich.DEUTSCH.hat(fach) || GostFachbereich.MATHEMATIK.hat(fach)) ? "S" : null;
				break;
			case "LK":
				wahl.halbjahre[GostHalbjahr.Q11.id] = (GostFachbereich.DEUTSCH.hat(fach) || GostFachbereich.MATHEMATIK.hat(fach)) ? "S" : null;
				wahl.abiturFach = null;
				break;
		}
		// Sonderfall Sport - darf AT haben
		if ((wahl.halbjahre[GostHalbjahr.Q11.id] === null) && GostFachbereich.SPORT.hat(fach)) {
			wahl.halbjahre[GostHalbjahr.Q11.id] = "AT";
		} else if (wahl.halbjahre[GostHalbjahr.Q11.id] === "AT" && GostFachbereich.SPORT.hat(fach)) {
			wahl.halbjahre[GostHalbjahr.Q11.id] = null;
		}
		// Q11 wählt bis Q22
		switch (wahl.halbjahre[GostHalbjahr.Q11.id]) {
			case null:
				if (!istVTF) {
					wahl.halbjahre[GostHalbjahr.Q12.id] = null;
					wahl.halbjahre[GostHalbjahr.Q21.id] = null;
					wahl.halbjahre[GostHalbjahr.Q22.id] = null;
				}
				if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(fach) && (this.jahrgang.hatZusatzkursSW) && !this.uiManager.istBilingual(fach)) {
					const beginn: GostHalbjahr | null = GostHalbjahr.fromKuerzel(this.jahrgang.beginnZusatzkursSW);
					if (beginn !== null) {
						if (beginn === GostHalbjahr.Q11) {
							wahl.halbjahre[GostHalbjahr.Q11.id] = 'ZK';
							wahl.halbjahre[GostHalbjahr.Q12.id] = 'ZK';
						}
					}
				}
				if (GostFachbereich.GESCHICHTE.hat(fach) && this.jahrgang.hatZusatzkursGE && !this.uiManager.istBilingual(fach)) {
					const beginn: GostHalbjahr | null = GostHalbjahr.fromKuerzel(this.jahrgang.beginnZusatzkursGE);
					if (beginn !== null) {
						if (beginn === GostHalbjahr.Q11) {
							wahl.halbjahre[GostHalbjahr.Q11.id] = 'ZK';
							wahl.halbjahre[GostHalbjahr.Q12.id] = 'ZK';
						}
					}
					if ((beginn !== null) && (beginn === GostHalbjahr.Q11) && (wahl.halbjahre[GostHalbjahr.EF2.id] === null)) {
						wahl.halbjahre[GostHalbjahr.Q11.id] = "ZK";
						wahl.halbjahre[GostHalbjahr.Q12.id] = "ZK";
					}
				}
				break;
			case "M":
				if (fach.istMoeglichQ12 && !istVTF) {
					wahl.halbjahre[GostHalbjahr.Q12.id] = wahl.halbjahre[GostHalbjahr.Q11.id];
				}
				if (!(istVTF || istPJK) && !GostFachbereich.KUNST_MUSIK.hat(fach) && !GostFachbereich.RELIGION.hat(fach)) {
					if (fach.istMoeglichQ21) {
						wahl.halbjahre[GostHalbjahr.Q21.id] = wahl.halbjahre[GostHalbjahr.Q11.id];
					}
					if (fach.istMoeglichQ22) {
						wahl.halbjahre[GostHalbjahr.Q22.id] = wahl.halbjahre[GostHalbjahr.Q11.id];
					}
				}
				break;
			case "S":
				if (fach.istMoeglichQ12) {
					wahl.halbjahre[GostHalbjahr.Q12.id] = wahl.halbjahre[GostHalbjahr.Q11.id];
				}
				if (!(istVTF || istPJK)) {
					if (fach.istMoeglichQ21) {
						wahl.halbjahre[GostHalbjahr.Q21.id] = wahl.halbjahre[GostHalbjahr.Q11.id];
					}
					// "S" kann nur für drittes Abifach gewählt werden, Vorauswahl daher "M"
					if (fach.istMoeglichQ22) {
						wahl.halbjahre[GostHalbjahr.Q22.id] = "M";
					}
				}
				break;
			case "ZK":
				wahl.halbjahre[GostHalbjahr.Q11.id] = null;
				wahl.halbjahre[GostHalbjahr.Q12.id] = null;
				break;
			case "LK": {
				wahl.halbjahre[GostHalbjahr.Q12.id] = fach.istMoeglichQ12 ? wahl.halbjahre[GostHalbjahr.Q11.id] : null;
				wahl.halbjahre[GostHalbjahr.Q21.id] = fach.istMoeglichQ21 ? wahl.halbjahre[GostHalbjahr.Q11.id] : null;
				wahl.halbjahre[GostHalbjahr.Q22.id] = fach.istMoeglichQ22 ? wahl.halbjahre[GostHalbjahr.Q11.id] : null;
				// Bedingungen für LK1
				const alle_fachbelegungen = this.manager.getFachbelegungen();
				const lk1_belegt = this.manager.pruefeExistiertAbiFach(alle_fachbelegungen, GostAbiturFach.LK1);
				const lk2_belegt = this.manager.pruefeExistiertAbiFach(alle_fachbelegungen, GostAbiturFach.LK2);
				if (GostFachbereich.DEUTSCH.hat(fach) || GostFachbereich.MATHEMATIK.hat(fach)
					|| GostFachbereich.NATURWISSENSCHAFTLICH_KLASSISCH.hat(fach)
					|| (GostFachbereich.FREMDSPRACHE.hat(fach) && !fach.istFremdSpracheNeuEinsetzend)) {
					wahl.abiturFach = !lk1_belegt ? 1 : lk2_belegt ? null : 2;
				} else {
					wahl.abiturFach = lk2_belegt ? null : 2;
				}
				break;
			}
		}
		if ((wahl.halbjahre[GostHalbjahr.Q11.id] === null) || (wahl.halbjahre[GostHalbjahr.Q11.id] === "M")) {
			wahl.abiturFach = null;
		}
	}


	/**
	 * Die Stepper-Methode für die Fachwahlen in der Q1.2
	 *
	 * @param fach   das Fach
	 * @param wahl   die Fachwahl
	 */
	private stepQ12Wahl(fach: GostFach, wahl: GostSchuelerFachwahl): void {
		const istVTF = (this.uiManager.getFachgruppe(fach) === Fachgruppe.FG_VX);
		const istPJK = (this.uiManager.getFachgruppe(fach) === Fachgruppe.FG_PX);
		switch (wahl.halbjahre[GostHalbjahr.Q12.id]) {
			case null:
				wahl.halbjahre[GostHalbjahr.Q12.id] = "M";
				if (istPJK && (wahl.halbjahre[GostHalbjahr.Q11.id] === null) && fach.istMoeglichQ21) {
					wahl.halbjahre[GostHalbjahr.Q21.id] = "M";
					wahl.halbjahre[GostHalbjahr.Q22.id] = null;
				}
				if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(fach) && (this.jahrgang.hatZusatzkursSW) && !this.uiManager.istBilingual(fach)) {
					const beginn: GostHalbjahr | null = GostHalbjahr.fromKuerzel(this.jahrgang.beginnZusatzkursSW);
					if (beginn !== null) {
						if (beginn === GostHalbjahr.Q11) {
							wahl.halbjahre[GostHalbjahr.Q11.id] = 'ZK';
							wahl.halbjahre[GostHalbjahr.Q12.id] = 'ZK';
						}
						if (beginn === GostHalbjahr.Q12) {
							wahl.halbjahre[GostHalbjahr.Q12.id] = 'ZK';
							wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK';
						}
					}
				}
				if (GostFachbereich.GESCHICHTE.hat(fach) && this.jahrgang.hatZusatzkursGE && !this.uiManager.istBilingual(fach)) {
					const beginn: GostHalbjahr | null = GostHalbjahr.fromKuerzel(this.jahrgang.beginnZusatzkursGE);
					if (beginn !== null) {
						if (beginn === GostHalbjahr.Q11) {
							wahl.halbjahre[GostHalbjahr.Q11.id] = 'ZK';
							wahl.halbjahre[GostHalbjahr.Q12.id] = 'ZK';
						}
						if (beginn === GostHalbjahr.Q12) {
							wahl.halbjahre[GostHalbjahr.Q12.id] = 'ZK';
							wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK';
						}
					}
				}
				break;
			case "M":
				if (istVTF || istPJK || GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(fach)) {
					wahl.halbjahre[GostHalbjahr.Q12.id] = null;
				} else if (GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && !fach.istMoeglichAbiLK) {
					wahl.halbjahre[GostHalbjahr.Q12.id] = null;
				} else if (GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && fach.istMoeglichAbiLK) {
					wahl.halbjahre[GostHalbjahr.Q12.id] = "LK";
				} else {
					wahl.halbjahre[GostHalbjahr.Q12.id] = "S";
				}
				break;
			case "S":
				wahl.halbjahre[GostHalbjahr.Q12.id] = (wahl.halbjahre[GostHalbjahr.Q11.id] === "LK") ? "LK" : null;
				break;
			case "ZK": {
				const beginn: GostHalbjahr | null = (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(fach))
					? GostHalbjahr.fromKuerzel(this.jahrgang.beginnZusatzkursSW ?? "")
					: GostHalbjahr.fromKuerzel(this.jahrgang.beginnZusatzkursGE ?? "");
				if ((beginn !== null) && (beginn === GostHalbjahr.Q11)) {
					wahl.halbjahre[GostHalbjahr.Q11.id] = null;
				}
				wahl.halbjahre[GostHalbjahr.Q12.id] = null;
				if ((beginn !== null) && (beginn === GostHalbjahr.Q12)) {
					wahl.halbjahre[GostHalbjahr.Q21.id] = null;
				}
				break;
				// TODO: Warum ist das so? Bis Q22. Was ist erlaubt: M, S, null?
			}
			case "LK":
				wahl.halbjahre[GostHalbjahr.Q12.id] = null;
				wahl.abiturFach = null;
		}
		// Sonderfall Sport - darf AT haben
		if ((wahl.halbjahre[GostHalbjahr.Q12.id] === null) && GostFachbereich.SPORT.hat(fach)) {
			wahl.halbjahre[GostHalbjahr.Q12.id] = "AT";
		} else if ((wahl.halbjahre[GostHalbjahr.Q12.id] === "AT") && GostFachbereich.SPORT.hat(fach)) {
			wahl.halbjahre[GostHalbjahr.Q12.id] = null;
		}
		// Nachfolgende HJ ebenfalls setzen
		if ((wahl.halbjahre[GostHalbjahr.Q12.id] === null) && !istVTF) {
			wahl.halbjahre[GostHalbjahr.Q21.id] = null;
			wahl.halbjahre[GostHalbjahr.Q22.id] = null;
		}
		if ((wahl.halbjahre[GostHalbjahr.Q12.id] === null) || (wahl.halbjahre[GostHalbjahr.Q12.id] === "M")) {
			wahl.abiturFach = null;
		}
	}


	/**
	 * Die Stepper-Methode für die Fachwahlen in der Q2.1
	 *
	 * @param fach   das Fach
	 * @param wahl   die Fachwahl
	 */
	private stepQ21Wahl(fach: GostFach, wahl: GostSchuelerFachwahl): void {
		const istVTF = (this.uiManager.getFachgruppe(fach) === Fachgruppe.FG_VX);
		const istPJK = (this.uiManager.getFachgruppe(fach) === Fachgruppe.FG_PX);
		switch (wahl.halbjahre[GostHalbjahr.Q21.id]) {
			case null:
				wahl.halbjahre[GostHalbjahr.Q21.id] = "M";
				if (istPJK && (wahl.halbjahre[GostHalbjahr.Q12.id] === null) && fach.istMoeglichQ22) {
					wahl.halbjahre[GostHalbjahr.Q22.id] = "M";
				}
				if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(fach) && this.jahrgang.hatZusatzkursSW && !this.uiManager.istBilingual(fach)) {
					const beginn: GostHalbjahr | null = GostHalbjahr.fromKuerzel(this.jahrgang.beginnZusatzkursSW);
					if (beginn !== null) {
						if (beginn === GostHalbjahr.Q12) {
							if (!this.hatSchuelerFachwahl(wahl, GostHalbjahr.Q11) && !this.uiManager.hatDoppelbelegung(fach, GostHalbjahr.Q11)) {
								wahl.halbjahre[GostHalbjahr.Q12.id] = 'ZK';
								wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK';
							}
						}
						if (beginn === GostHalbjahr.Q21) {
							if (!this.hatSchuelerFachwahl(wahl, GostHalbjahr.Q12) && !this.uiManager.hatDoppelbelegung(fach, GostHalbjahr.Q12)) {
								wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK';
								wahl.halbjahre[GostHalbjahr.Q22.id] = 'ZK';
							}
						}
					}
				}
				if (GostFachbereich.GESCHICHTE.hat(fach) && this.jahrgang.hatZusatzkursGE && !this.uiManager.istBilingual(fach)) {
					const beginn: GostHalbjahr | null = GostHalbjahr.fromKuerzel(this.jahrgang.beginnZusatzkursGE);
					if (beginn !== null) {
						if (beginn === GostHalbjahr.Q12) {
							if (!this.hatSchuelerFachwahl(wahl, GostHalbjahr.Q11) && !this.uiManager.hatDoppelbelegung(fach, GostHalbjahr.Q11)) {
								wahl.halbjahre[GostHalbjahr.Q12.id] = 'ZK';
								wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK';
							}
						}
						if (beginn === GostHalbjahr.Q21) {
							if (!this.hatSchuelerFachwahl(wahl, GostHalbjahr.Q12) && !this.uiManager.hatDoppelbelegung(fach, GostHalbjahr.Q12)) {
								wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK';
								wahl.halbjahre[GostHalbjahr.Q22.id] = 'ZK';
							}
						}
					}
				}
				break;
			case "M":
				if (istVTF || istPJK || GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(fach)) {
					wahl.halbjahre[GostHalbjahr.Q21.id] = null;
				} else if (GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && !fach.istMoeglichAbiLK) {
					wahl.halbjahre[GostHalbjahr.Q21.id] = null;
				} else if (GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && fach.istMoeglichAbiLK) {
					wahl.halbjahre[GostHalbjahr.Q21.id] = "LK";
				} else {
					wahl.halbjahre[GostHalbjahr.Q21.id] = "S";
				}
				break;
			case "S":
				wahl.halbjahre[GostHalbjahr.Q21.id] = (wahl.halbjahre[GostHalbjahr.Q12.id] === "LK") ? "LK" : null;
				break;
			case "ZK": {
				const beginn: GostHalbjahr | null = (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(fach))
					? GostHalbjahr.fromKuerzel(this.jahrgang.beginnZusatzkursSW ?? "")
					: GostHalbjahr.fromKuerzel(this.jahrgang.beginnZusatzkursGE ?? "");
				if ((beginn !== null) && (beginn === GostHalbjahr.Q12)) {
					wahl.halbjahre[GostHalbjahr.Q12.id] = null;
				}
				wahl.halbjahre[GostHalbjahr.Q21.id] = null;
				if ((beginn !== null) && (beginn === GostHalbjahr.Q21)) {
					wahl.halbjahre[GostHalbjahr.Q22.id] = null;
				}
				break;
			}
			case "LK":
				wahl.halbjahre[GostHalbjahr.Q21.id] = null;
				wahl.abiturFach = null;
				break;
		}
		// Sonderfall Sport - darf AT haben
		if ((wahl.halbjahre[GostHalbjahr.Q21.id] === null) && GostFachbereich.SPORT.hat(fach)) {
			wahl.halbjahre[GostHalbjahr.Q21.id] = "AT";
		} else if ((wahl.halbjahre[GostHalbjahr.Q21.id] === "AT") && GostFachbereich.SPORT.hat(fach)) {
			wahl.halbjahre[GostHalbjahr.Q21.id] = null;
		}
		// Nachfolgende HJ ebenfalls setzen
		if ((wahl.halbjahre[GostHalbjahr.Q21.id] === null) && !istVTF) {
			wahl.halbjahre[GostHalbjahr.Q22.id] = null;
		}
		if ((wahl.halbjahre[GostHalbjahr.Q21.id] === null) || (wahl.halbjahre[GostHalbjahr.Q21.id] === "ZK")) {
			wahl.abiturFach = null;
		}
	}


	/**
	 * Die Stepper-Methode für die Fachwahlen in der Q2.2
	 *
	 * @param fach   das Fach
	 * @param wahl   die Fachwahl
	 */
	private stepQ22Wahl(fach: GostFach, wahl: GostSchuelerFachwahl): void {
		const istVTF = (this.uiManager.getFachgruppe(fach) === Fachgruppe.FG_VX);
		const istPJK = (this.uiManager.getFachgruppe(fach) === Fachgruppe.FG_PX);
		switch (wahl.halbjahre[GostHalbjahr.Q22.id]) {
			case null:
				wahl.halbjahre[GostHalbjahr.Q22.id] = "M";
				if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(fach) && this.jahrgang.hatZusatzkursSW && !this.uiManager.istBilingual(fach)) {
					const beginn: GostHalbjahr | null = GostHalbjahr.fromKuerzel(this.jahrgang.beginnZusatzkursSW);
					if (beginn !== null) {
						if (beginn === GostHalbjahr.Q21) {
							if (!this.hatSchuelerFachwahl(wahl, GostHalbjahr.Q12) && !this.uiManager.hatDoppelbelegung(fach, GostHalbjahr.Q12)) {
								wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK';
								wahl.halbjahre[GostHalbjahr.Q22.id] = 'ZK';
							}
						}
					}
				}
				if (GostFachbereich.GESCHICHTE.hat(fach) && this.jahrgang.hatZusatzkursGE && !this.uiManager.istBilingual(fach)) {
					const beginn: GostHalbjahr | null = GostHalbjahr.fromKuerzel(this.jahrgang.beginnZusatzkursGE);
					if (beginn !== null) {
						if (beginn === GostHalbjahr.Q21) {
							if (!this.hatSchuelerFachwahl(wahl, GostHalbjahr.Q12) && !this.uiManager.hatDoppelbelegung(fach, GostHalbjahr.Q12)) {
								wahl.halbjahre[GostHalbjahr.Q21.id] = 'ZK';
								wahl.halbjahre[GostHalbjahr.Q22.id] = 'ZK';
							}
						}
					}
				}
				break;
			case "M":
				if (istVTF || istPJK || GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(fach)) {
					wahl.halbjahre[GostHalbjahr.Q22.id] = null;
				} else if (GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && !fach.istMoeglichAbiLK) {
					wahl.halbjahre[GostHalbjahr.Q22.id] = null;
				} else if (GostFachbereich.SPORT.hat(fach) && !fach.istMoeglichAbiGK && fach.istMoeglichAbiLK) {
					wahl.halbjahre[GostHalbjahr.Q22.id] = "LK";
				} else {
					wahl.halbjahre[GostHalbjahr.Q22.id] = "S";
				}
				break;
			case "S":
				wahl.halbjahre[GostHalbjahr.Q22.id] = (wahl.halbjahre[GostHalbjahr.Q21.id] === "LK") ? "LK" : null;
				break;
			case "ZK": {
				const beginn: GostHalbjahr | null = (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(fach))
					? GostHalbjahr.fromKuerzel(this.jahrgang.beginnZusatzkursSW ?? "")
					: GostHalbjahr.fromKuerzel(this.jahrgang.beginnZusatzkursGE ?? "");
				if ((beginn !== null) && (beginn === GostHalbjahr.Q21)) {
					wahl.halbjahre[GostHalbjahr.Q21.id] = null;
				}
				wahl.halbjahre[GostHalbjahr.Q22.id] = null;
				break;
			}
			case "LK":
				wahl.halbjahre[GostHalbjahr.Q22.id] = null;
				wahl.abiturFach = null;
		}
		// Sonderfall Sport - darf AT haben
		if ((wahl.halbjahre[GostHalbjahr.Q22.id] === null) && GostFachbereich.SPORT.hat(fach)) {
			wahl.halbjahre[GostHalbjahr.Q22.id] = "AT";
		} else if ((wahl.halbjahre[GostHalbjahr.Q22.id] === "AT") && GostFachbereich.SPORT.hat(fach)) {
			wahl.halbjahre[GostHalbjahr.Q22.id] = null;
		}
		// Nachfolgende HJ ebenfalls setzen
		if ((wahl.halbjahre[GostHalbjahr.Q22.id] === null) || (wahl.halbjahre[GostHalbjahr.Q22.id] === "ZK")) {
			wahl.abiturFach = null;
		}
		if (wahl.abiturFach === 3 && wahl.halbjahre[GostHalbjahr.Q22.id] === "M") {
			wahl.abiturFach = this.manager.pruefeExistiertAbiFach(this.manager.getFachbelegungen(), GostAbiturFach.AB4) ? null : 4;
		}
		if (wahl.abiturFach === 4 && wahl.halbjahre[GostHalbjahr.Q22.id] === "S") {
			wahl.abiturFach = this.manager.pruefeExistiertAbiFach(this.manager.getFachbelegungen(), GostAbiturFach.AB3) ? null : 3;
		}
	}


	/**
	 * Stepper für das Durchwandern der Auswahloptionen im Bereich der Halbjahre eines Faches
	 * im manuellen Modus und im Hochschreibemodus.
	 *
	 * @param fach       das Fach
	 * @param halbjahr   das Halbjahr
	 *
	 * @returns -
	 */
	private async stepperManuell(fach: GostFach, halbjahr: GostHalbjahr) {
		for (const hj of GostHalbjahr.values()) {
			if (hj.id >= halbjahr.id) {
				if (this.manager.istBewertet(hj)) {
					return;
				}
			}
		}
		const wahl = this.manager.getSchuelerFachwahl(fach.id);
		const hj = halbjahr.id;
		const istVTF = (this.uiManager.getFachgruppe(fach) === Fachgruppe.FG_VX);
		const istPJK = (this.uiManager.getFachgruppe(fach) === Fachgruppe.FG_PX);
		switch (wahl.halbjahre[hj]) {
			case "AT":
				wahl.halbjahre[hj] = null;
				break;
			case "ZK":
				wahl.halbjahre[hj] = null;
				break;
			case null:
				wahl.halbjahre[hj] = "M";
				break;
			case "M":
				if (!fach.istPruefungsordnungsRelevant || istVTF || istPJK || (GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(fach))) {
					wahl.halbjahre[hj] = null;
				} else {
					wahl.halbjahre[hj] = "S";
				}
				break;
			case "S":
				if ((hj <= 1) || (GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(fach))) {
					if (GostFachbereich.SPORT.hat(fach)) {
						wahl.halbjahre[hj] = "AT";
					} else {
						wahl.halbjahre[hj] = null;
					}
				} else { // in der Q-Phase als LK möglich, allerdings nicht im Fachbereich des literarisch-künstlerischen Bereichs
					wahl.halbjahre[hj] = "LK";
				}
				break;
			case "LK": {
				wahl.halbjahre[hj] = null;
				if (GostFachbereich.SPORT.hat(fach)) {
					wahl.halbjahre[hj] = "AT";
				}
				if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(fach) || GostFachbereich.GESCHICHTE.hat(fach)) {
					wahl.halbjahre[hj] = "ZK";
				}
				break;
			}
			default:
				wahl.halbjahre[hj] = null;
				break;
		}
		await this.setWahl(fach.id, wahl);
	}

	/**
	 * Stepper für das Durchwandern der Auswahloptionen im Bereich der Halbjahre eines Faches
	 * im Hochschreibe-Modus und im Hochschreibemodus.
	 *
	 * @param fach       das Fach
	 * @param halbjahr   das Halbjahr
	 *
	 * @returns -
	 */
	private async stepperHochschreiben(fach: GostFach, halbjahr: GostHalbjahr) {
		if (!this.uiManager.istMoeglich(fach, halbjahr)) {
			return;
		}
		for (const hj of GostHalbjahr.values()) {
			if (hj.id >= halbjahr.id) {
				if (this.manager.istBewertet(hj)) {
					return;
				}
			}
		}
		const wahl = this.manager.getSchuelerFachwahl(fach.id);
		if (halbjahr === GostHalbjahr.EF1) {
			this.stepEF1WahlHochschreiben(fach, wahl);
		} else if (halbjahr === GostHalbjahr.EF2) {
			this.stepEF2WahlHochschreiben(fach, wahl);
		} else if (halbjahr === GostHalbjahr.Q11) {
			this.stepQ11Wahl(fach, wahl);
		} else if (halbjahr === GostHalbjahr.Q12) {
			this.stepQ12Wahl(fach, wahl);
		} else if (halbjahr === GostHalbjahr.Q21) {
			this.stepQ21Wahl(fach, wahl);
		} else if (halbjahr === GostHalbjahr.Q22) {
			this.stepQ22Wahl(fach, wahl);
		}
		await this.setWahl(fach.id, wahl);
	}

	/**
	 * Stepper für das Durchwandern der Auswahloptionen im Bereich der Halbjahre eines Faches
	 * im normalen Modus und im Hochschreibemodus.
	 *
	 * @param fach       das Fach
	 * @param halbjahr   das Halbjahr
	 *
	 * @returns -
	 */
	private async stepperNormal(fach: GostFach, halbjahr: GostHalbjahr) {
		if (!this.uiManager.istMoeglich(fach, halbjahr)) {
			return;
		}
		for (const hj of GostHalbjahr.values()) {
			if (hj.id >= halbjahr.id) {
				if (this.manager.istBewertet(hj)) {
					return;
				}
			}
		}
		const wahl = this.manager.getSchuelerFachwahl(fach.id);
		if (halbjahr === GostHalbjahr.EF1) {
			this.stepEF1Wahl(fach, wahl);
		} else if (halbjahr === GostHalbjahr.EF2) {
			this.stepEF2Wahl(fach, wahl);
		} else if (halbjahr === GostHalbjahr.Q11) {
			this.stepQ11Wahl(fach, wahl);
		} else if (halbjahr === GostHalbjahr.Q12) {
			this.stepQ12Wahl(fach, wahl);
		} else if (halbjahr === GostHalbjahr.Q21) {
			this.stepQ21Wahl(fach, wahl);
		} else if (halbjahr === GostHalbjahr.Q22) {
			this.stepQ22Wahl(fach, wahl);
		}
		await this.setWahl(fach.id, wahl);
	}

	/**
	 * Stepper für das Durchwandern der Auswahloptionen im Bereich der Halbjahre eines Faches. Diese Methode ist ein
	 * Einsprungspunkt für die vue-Komponente und wählt je nach Modus die geeignete Methode aus.
	 *
	 * @param fach       das Fach
	 * @param halbjahr   das Halbjahr
	 *
	 * @returns -
	 */
	public async stepper(fach: GostFach, halbjahr: GostHalbjahr, modus?: LaufbahnplanungUiStepperMode) {
		const mode = modus ?? this.uiManager.modus;
		switch (mode) {
			case 'manuell':
				await this.stepperManuell(fach, halbjahr);
				return;
			case 'hochschreiben':
				await this.stepperHochschreiben(fach, halbjahr);
				return;
			case 'normal':
				await this.stepperNormal(fach, halbjahr);
				return;
		}
	}

}
