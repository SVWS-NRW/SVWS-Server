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
 * Eine Klasse für die Ui-Stepper im Falle der Abiturregelung ab Abitur 2030
 */
export class LaufbahnplanungUiStepperAbi2030 implements LaufbahnplanungUiStepper {

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

	private checkStateValid(): void {
		if (!this.gostLaufbahnplanungState.valid) {
			throw new DeveloperNotificationException("Es wurde auf den State der Laufbahnplanung zugegriffen, obwohl dieser nicht valide ist.");
		}
	}

	private get manager(): AbiturdatenManager {
		this.checkStateValid();
		return this.gostLaufbahnplanungState.abiturdatenManager;
	}

	private get jahrgang(): GostJahrgangsdaten {
		this.checkStateValid();
		return this.gostLaufbahnplanungState.gostJahrgangsdaten;
	}

	private async setWahl(idFach: number, wahl: GostSchuelerFachwahl) {
		this.checkStateValid();
		await this.gostLaufbahnplanungState.setWahl(idFach, wahl);
	}

	/**
	 * Stepper für das Durchwandern der Auswahloptionen im Abiturbereich eines Faches
	 * im manuellen Modus.
	 *
	 * @param fach   das Fach
	 *
	 * @returns true, wenn eine Änderung vorgenommen wurde
	 */
	private stepperAbiturManuell(fach: GostFach, wahl: GostSchuelerFachwahl): boolean {
		if (this.manager.istBewertet(GostHalbjahr.Q22)) {
			return false;
		}
		if (wahl.halbjahre[GostHalbjahr.Q22.id] === null) {
			return false;
		}
		const aktuell = wahl.abiturFach;
		const istLK = (wahl.halbjahre[GostHalbjahr.Q22.id] === "LK");

		if (istLK && (aktuell === null)) {
			wahl.abiturFach = 1;
		} else if (istLK && (aktuell === 1)) {
			wahl.abiturFach = 2;
		} else if (istLK) {
			wahl.abiturFach = null;
		} else if ((aktuell === null) || (aktuell === 1) || (aktuell === 2)) {
			wahl.abiturFach = 3;
		} else if (aktuell === 3) {
			wahl.abiturFach = 4;
		} else if (aktuell === 4) {
			wahl.abiturFach = 5;
		} else {
			wahl.abiturFach = null;
		}
		return true;
	}


	private stepAbiturLK(fach: GostFach, wahl: GostSchuelerFachwahl): boolean {
		const aktuell = wahl.abiturFach;
		if ((aktuell !== null) && (aktuell !== 1) && (aktuell !== 2)) {
			wahl.abiturFach = (GostFachUtils.istWaehlbarLeistungskurs1(fach) && !this.manager.hatAbiFach(GostAbiturFach.LK1)) ? 1 : 2;
			return true;
		} else if (aktuell === 1) {
			wahl.abiturFach = 2;
			return true;
		} else if ((aktuell === 2) && GostFachUtils.istWaehlbarLeistungskurs1(fach)) {
			wahl.abiturFach = 1;
			return true;
		}
		return false;
	}


	private stepAbiturGK(wahl: GostSchuelerFachwahl): boolean {
		const aktuell = wahl.abiturFach;
		if ((wahl.halbjahre[GostHalbjahr.Q22.id] === "M") && ((aktuell === null) || (aktuell === 3))) {
			wahl.abiturFach = 4;
		} else if ((wahl.halbjahre[GostHalbjahr.Q22.id] === "M") && (aktuell === 4)) {
			wahl.abiturFach = 5;
		} else if ((wahl.halbjahre[GostHalbjahr.Q22.id] === "S") && ((aktuell === null) || (aktuell === 4) || (aktuell === 5))) {
			wahl.abiturFach = 3;
		} else { // z.B. aktuell === 1 oder aktuell === 2
			wahl.abiturFach = null;
		}
		return true;
	}


	/**
	 * Stepper für das Durchwandern der Auswahloptionen im Abiturbereich eines Faches
	 * im normalen Modus und im Hochschreibemodus.
	 *
	 * @param fach   das Fach
	 *
	 * @returns true, wenn eine Änderung vorgenommen wurde
	 */
	private stepperAbiturNormal(fach: GostFach, wahl: GostSchuelerFachwahl): boolean {
		// Prüfe, ob die Wahl als Abiturfach überhaupt möglich ist
		if (!this.uiManager.istMoeglichAbi(fach)) {
			return false;
		}
		// Bestimme die Fachwahl des Schüler und die mögliche Kursart im Abitur.
		const abiMoeglicheKursart = this.uiManager.getMoeglicheAbiKursart(fach);
		// Keine Kursart im Abitur möglich...
		if (abiMoeglicheKursart === null) {
			wahl.abiturFach = null;
			return true;
		}

		const aktuell = wahl.abiturFach;

		// Die mögliche Kursart im Abitur ist PJK (Projektkurs)
		if (abiMoeglicheKursart === GostKursart.PJK) {
			wahl.abiturFach = (aktuell === null) ? 5 : null;
			return true;
		}

		// Die mögliche Kursart im Abitur ist LK (Leistungskurs)
		if (abiMoeglicheKursart === GostKursart.LK) {
			return this.stepAbiturLK(fach, wahl);
		}

		// Die mögliche Kursart im Abitur ist GK (Grundkurs)
		if (abiMoeglicheKursart === GostKursart.GK) {
			return this.stepAbiturGK(wahl);
		}
		return false;
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
		let changed = false;
		const wahl = this.manager.getSchuelerFachwahl(fach.id);
		switch (this.uiManager.modus) {
			case 'manuell':
				changed = this.stepperAbiturManuell(fach, wahl);
				break;
			case 'normal':
			case 'hochschreiben':
				changed = this.stepperAbiturNormal(fach, wahl);
				break;
		}
		if (changed) {
			await this.setWahl(fach.id, wahl);
		}
	}


	public async stepperReferenzfach(fach: GostFach) {
		const istPJK = (this.uiManager.getFachgruppe(fach) === Fachgruppe.FG_PX);
		if (!istPJK) {
			throw new DeveloperNotificationException("Der Stepper für ein Referenzfach darf nur bei Projektkurs-Fächern aufgerufen werden.");
		}
		const wahl = this.manager.getSchuelerFachwahl(fach.id);
		if ((fach.projektKursLeitfach1ID !== null) && (wahl.idReferenzfach === null)) {
			wahl.idReferenzfach = fach.projektKursLeitfach1ID;
		} else if ((fach.projektKursLeitfach2ID !== null) && (wahl.idReferenzfach !== fach.projektKursLeitfach2ID)) {
			wahl.idReferenzfach = fach.projektKursLeitfach2ID;
		} else {
			wahl.idReferenzfach = null;
		}
		await this.setWahl(fach.id, wahl);
	}

	/**
	 * Prüft, ob das übergebene Fach in dem übergebenen Halbjahr wählbar ist oder nicht.
	 *
	 * @param fach       das Fach
	 * @param halbjahr   das Halbjahr
	 *
	 * @returns true, wenn das Fach in dem Halbjahr wählbar ist, und ansonsten false
	 */
	private istFachWaehlbar(fach: GostFach, halbjahr: GostHalbjahr | null): boolean {
		if (halbjahr === null) {
			return false;
		}
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


	private stepProjektkurs(fach: GostFach, wahl: GostSchuelerFachwahl, halbjahr: GostHalbjahr): void {
		// Diese Methode darf nur für Projektkurse aufgerufen werden
		if (this.uiManager.getFachgruppe(fach) !== Fachgruppe.FG_PX) {
			throw new DeveloperNotificationException("Die Methode stepProjektkurs darf nur bei Projektkurs-Fächern verwendet werden.");
		}

		// Projektkurse sind nur in der Q2 erlaubt
		if (halbjahr.istIn(GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12)) {
			wahl.halbjahre[GostHalbjahr.EF1.id] = null;
			wahl.halbjahre[GostHalbjahr.EF2.id] = null;
			wahl.halbjahre[GostHalbjahr.Q11.id] = null;
			wahl.halbjahre[GostHalbjahr.Q12.id] = null;
			return;
		}

		// Wenn ein Projektkurs gewählt ist, dann wähle ihn ab, ...
		const aktuell = wahl.halbjahre[halbjahr.id];
		if (aktuell !== null) {
			wahl.halbjahre[GostHalbjahr.Q21.id] = null;
			wahl.halbjahre[GostHalbjahr.Q22.id] = null;
			wahl.idReferenzfach = null;
			return;
		}

		// ..., ansonsten wähle ihn und ...
		wahl.halbjahre[GostHalbjahr.Q21.id] = "S";
		wahl.halbjahre[GostHalbjahr.Q22.id] = "S";

		// ... setze ein mögliches Referenzfach automatisch
		if (this.uiManager.pruefeAbi30ProjektkursBelegungReferenzfachMoeglich(fach, 1) !== null) {
			wahl.idReferenzfach = fach.projektKursLeitfach1ID;
			return;
		}
		if (this.uiManager.pruefeAbi30ProjektkursBelegungReferenzfachMoeglich(fach, 2) !== null) {
			wahl.idReferenzfach = fach.projektKursLeitfach2ID;
			return;
		}
		wahl.idReferenzfach = null;
	}


	private stepVertiefungskurs(fach: GostFach, wahl: GostSchuelerFachwahl, halbjahr: GostHalbjahr): void {
		// Diese Methode darf nur für Vertiefungskurse aufgerufen werden
		if (this.uiManager.getFachgruppe(fach) !== Fachgruppe.FG_VX) {
			throw new DeveloperNotificationException("Die Methode stepVertiefungskurs darf nur bei Vertiefungskurs-Fächern verwendet werden.");
		}

		// Ein Vertiefungskurs kann immer nur mündlich gewählt werden (also toggle)
		if (wahl.halbjahre[halbjahr.id] === null) {
			wahl.halbjahre[halbjahr.id] = "M";
		} else {
			wahl.halbjahre[halbjahr.id] = null;
		}
	}


	private stepLiteratur(fach: GostFach, wahl: GostSchuelerFachwahl, halbjahr: GostHalbjahr): void {
		// Diese Methode darf nur bei Ersatz-Fächern des Literarisch-Künstlerischen Bereichs aufgerufen werden
		if (!GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(fach)) {
			throw new DeveloperNotificationException("Die Methode stepLiteratur darf nur bei Ersatz-Fächern des Literarisch-Künstlerischen Bereichs verwendet werden.");
		}

		// Ersatz-Fächer sind nur in der Qualifikationsphase möglich
		if (halbjahr.istEinfuehrungsphase()) {
			return;
		}

		// Das Ersatzfach kann immer nur mündlich gewählt werden
		if (wahl.halbjahre[halbjahr.id] === null) {
			wahl.halbjahre[halbjahr.id] = "M";
			if (wahl.halbjahre[halbjahr.previousOrException().id] === null) {
				if (this.istFachWaehlbar(fach, halbjahr.next())) {
					wahl.halbjahre[halbjahr.nextOrException().id] = "M";
				} else if (this.istFachWaehlbar(fach, halbjahr.previous())) {
					wahl.halbjahre[halbjahr.previousOrException().id] = "M";
				}
			}
		} else {
			wahl.halbjahre[halbjahr.id] = null;
		}
	}

	private getWahlSchriftlich(fach: GostFach): Array<string | null> {
		return [
			fach.istMoeglichEF1 ? 'S' : null,
			fach.istMoeglichEF2 ? 'S' : null,
			fach.istMoeglichQ11 ? 'S' : null,
			fach.istMoeglichQ12 ? 'S' : null,
			fach.istMoeglichQ21 ? 'S' : null,
			fach.istMoeglichQ22 ? 'M' : null,
		];
	}

	private getWahlMuendlich(fach: GostFach): Array<string | null> {
		return [
			fach.istMoeglichEF1 ? 'M' : null,
			fach.istMoeglichEF2 ? 'M' : null,
			fach.istMoeglichQ11 ? 'M' : null,
			fach.istMoeglichQ12 ? 'M' : null,
			fach.istMoeglichQ21 ? 'M' : null,
			fach.istMoeglichQ22 ? 'M' : null,
		];
	}

	private schreibeHochLeerZuSchriftlich(fach: GostFach, wahl: GostSchuelerFachwahl, halbjahr: GostHalbjahr): void {
		if (wahl.abiturFach !== null) {
			wahl.halbjahre[halbjahr.id] = 'S';
		} else if ((halbjahr === GostHalbjahr.EF1) && (this.istWahl(fach, wahl, [null, null, null, null, null, null]))) {
			wahl.halbjahre = this.getWahlSchriftlich(fach);
		} else if ((halbjahr !== GostHalbjahr.EF1) && this.istWahlInQPhase(fach, wahl, [null, null, null, null])) {
			const wahlEF1 = wahl.halbjahre[GostHalbjahr.EF1.id];
			wahl.halbjahre = this.getWahlSchriftlich(fach);
			wahl.halbjahre[GostHalbjahr.EF1.id] = wahlEF1;
		} else {
			wahl.halbjahre[halbjahr.id] = "S";
		}
	}

	private schreibeHochLeerSport(fach: GostFach, wahl: GostSchuelerFachwahl, halbjahr: GostHalbjahr): void {
		if (wahl.abiturFach !== null) {
			wahl.halbjahre[halbjahr.id] = 'S';
		} else if ((halbjahr === GostHalbjahr.EF1) && (this.istWahl(fach, wahl, [null, null, null, null, null, null]))) {
			if (!fach.istMoeglichAbiGK && !fach.istMoeglichAbiLK) {
				wahl.halbjahre = ['M', 'M', 'M', 'M', 'M', 'M'];
			} else if (!fach.istMoeglichAbiGK && fach.istMoeglichAbiLK) {
				wahl.halbjahre = ['S', 'S', 'LK', 'LK', 'LK', 'LK'];
			} else {
				wahl.halbjahre = this.getWahlSchriftlich(fach);
			}
		} else if ((halbjahr === GostHalbjahr.EF2) && this.istWahlInQPhase(fach, wahl, [null, null, null, null])) {
			if (!fach.istMoeglichAbiGK && !fach.istMoeglichAbiLK) {
				wahl.halbjahre = [wahl.halbjahre[0], 'M', 'M', 'M', 'M', 'M'];
			} else if (!fach.istMoeglichAbiGK && fach.istMoeglichAbiLK) {
				wahl.halbjahre = [wahl.halbjahre[0], 'S', 'LK', 'LK', 'LK', 'LK'];
			} else {
				const wahlEF1 = wahl.halbjahre[GostHalbjahr.EF1.id];
				wahl.halbjahre = this.getWahlSchriftlich(fach);
				wahl.halbjahre[GostHalbjahr.EF1.id] = wahlEF1;
			}
		} else if (fach.istMoeglichAbiGK || fach.istMoeglichAbiLK) {
			wahl.halbjahre[halbjahr.id] = "S";
		} else {
			wahl.halbjahre[halbjahr.id] = "M";
		}
	}


	private schreibeHochSchriftlichZuMuendlich(fach: GostFach, wahl: GostSchuelerFachwahl, halbjahr: GostHalbjahr): void {
		if (wahl.abiturFach !== null) {
			wahl.halbjahre[halbjahr.id] = 'M';
		} else if ((halbjahr === GostHalbjahr.EF1) && (this.istWahl(fach, wahl, ['S', 'S', 'S', 'S', 'S', 'M']))) {
			wahl.halbjahre = this.getWahlMuendlich(fach);
		} else if ((halbjahr !== GostHalbjahr.EF1) && ((this.istWahlInQPhase(fach, wahl, [null, null, null, null]) || this.istWahlInQPhase(fach, wahl, ['S', 'S', 'S', 'M'])))) {
			const wahlEF1 = wahl.halbjahre[GostHalbjahr.EF1.id];
			wahl.halbjahre = this.getWahlMuendlich(fach);
			wahl.halbjahre[GostHalbjahr.EF1.id] = wahlEF1;
		} else {
			wahl.halbjahre[halbjahr.id] = "M";
		}
	}

	private schreibeHochSchriftlichZuMuendlichKunstMusikReligion(fach: GostFach, wahl: GostSchuelerFachwahl, halbjahr: GostHalbjahr): void {
		if (wahl.abiturFach !== null) {
			wahl.halbjahre[halbjahr.id] = 'M';
		} else if ((halbjahr === GostHalbjahr.EF1) && (this.istWahl(fach, wahl, ['S', 'S', 'S', 'S', 'S', 'M']))) {
			wahl.halbjahre = ['M', 'M', 'M', 'M', null, null];
		} else if ((halbjahr !== GostHalbjahr.EF1) && ((this.istWahlInQPhase(fach, wahl, [null, null, null, null]) || this.istWahlInQPhase(fach, wahl, ['S', 'S', 'S', 'M'])))) {
			wahl.halbjahre = [wahl.halbjahre[0], 'M', 'M', 'M', null, null];
		} else {
			wahl.halbjahre[halbjahr.id] = "M";
		}
	}

	private schreibeHochSchriftlichZuLeer(fach: GostFach, wahl: GostSchuelerFachwahl, halbjahr: GostHalbjahr): void {
		if (wahl.abiturFach !== null) {
			wahl.halbjahre[halbjahr.id] = 'S';
		} else if ((halbjahr === GostHalbjahr.EF1) && (this.istWahl(fach, wahl, ['S', 'S', 'S', 'S', 'S', 'M']))) {
			wahl.halbjahre = [null, null, null, null, null, null];
		} else if ((halbjahr !== GostHalbjahr.EF1) && ((this.istWahlInQPhase(fach, wahl, [null, null, null, null]) || this.istWahlInQPhase(fach, wahl, ['S', 'S', 'S', 'M'])))) {
			wahl.halbjahre = [wahl.halbjahre[0], null, null, null, null, null];
		} else {
			wahl.halbjahre[halbjahr.id] = null;
		}
	}

	private schreibeHochSchriftlichSport(fach: GostFach, wahl: GostSchuelerFachwahl, halbjahr: GostHalbjahr): void {
		if (wahl.abiturFach !== null) {
			wahl.halbjahre[halbjahr.id] = 'M';
		} else if ((halbjahr === GostHalbjahr.EF1) && (this.istWahl(fach, wahl, ['S', 'S', 'S', 'S', 'S', 'M'])
			|| (this.istWahl(fach, wahl, ['S', 'S', 'LK', 'LK', 'LK', 'LK']) && !fach.istMoeglichAbiGK && fach.istMoeglichAbiLK))) {
			wahl.halbjahre = this.getWahlMuendlich(fach);
		} else if ((halbjahr !== GostHalbjahr.EF1) && (this.istWahlInQPhase(fach, wahl, [null, null, null, null]) || this.istWahlInQPhase(fach, wahl, ['S', 'S', 'S', 'M'])
			|| (this.istWahlInQPhase(fach, wahl, ['LK', 'LK', 'LK', 'LK']) && !fach.istMoeglichAbiGK && fach.istMoeglichAbiLK))) {
			const wahlEF1 = wahl.halbjahre[GostHalbjahr.EF1.id];
			wahl.halbjahre = this.getWahlMuendlich(fach);
			wahl.halbjahre[GostHalbjahr.EF1.id] = wahlEF1;
		} else {
			wahl.halbjahre[halbjahr.id] = "M";
		}
	}

	private schreibeHochMuendlicheZuLeer(fach: GostFach, wahl: GostSchuelerFachwahl, halbjahr: GostHalbjahr): void {
		if (wahl.abiturFach !== null) {
			wahl.halbjahre[halbjahr.id] = 'S';
		} else if ((halbjahr === GostHalbjahr.EF1) && (this.istWahl(fach, wahl, ['M', 'M', 'M', 'M', 'M', 'M']) || this.istWahl(fach, wahl, ['M', 'M', 'M', 'M', 'M', null])
				|| this.istWahl(fach, wahl, ['M', 'M', 'M', 'M', null, null]))) {
			wahl.halbjahre = [null, null, null, null, null, null];
		} else if ((halbjahr !== GostHalbjahr.EF1) && (this.istWahlInQPhase(fach, wahl, [null, null, null, null]) || this.istWahlInQPhase(fach, wahl, ['M', 'M', 'M', 'M'])
				|| this.istWahlInQPhase(fach, wahl, ['M', 'M', 'M', null]) || this.istWahlInQPhase(fach, wahl, ['M', 'M', null, null]))) {
			wahl.halbjahre = [wahl.halbjahre[0], null, null, null, null, null];
		} else {
			wahl.halbjahre[halbjahr.id] = null;
		}
	}


	private leereWahlAb(wahl: GostSchuelerFachwahl, halbjahr: GostHalbjahr) {
		for (let hj: GostHalbjahr | null = halbjahr; hj !== null; hj = hj.next()) {
			wahl.halbjahre[hj.id] = null;
		}
		wahl.abiturFach = null;
	}


	private setzeWahlQMuendliche(fach: GostFach, wahl: GostSchuelerFachwahl, nurQ1: boolean = false): void {
		wahl.halbjahre[GostHalbjahr.Q11.id] = fach.istMoeglichQ11 ? "M" : null;
		wahl.halbjahre[GostHalbjahr.Q12.id] = fach.istMoeglichQ12 ? "M" : null;
		if (!nurQ1) {
			wahl.halbjahre[GostHalbjahr.Q21.id] = fach.istMoeglichQ21 ? "M" : null;
			wahl.halbjahre[GostHalbjahr.Q22.id] = fach.istMoeglichQ22 ? "M" : null;
		}
		wahl.abiturFach = null;
	}

	private setzeWahlQSchriftlich(fach: GostFach, wahl: GostSchuelerFachwahl): void {
		wahl.halbjahre[GostHalbjahr.Q11.id] = fach.istMoeglichQ11 ? "S" : null;
		wahl.halbjahre[GostHalbjahr.Q12.id] = fach.istMoeglichQ12 ? "S" : null;
		wahl.halbjahre[GostHalbjahr.Q21.id] = fach.istMoeglichQ21 ? "S" : null;
		wahl.halbjahre[GostHalbjahr.Q22.id] = fach.istMoeglichQ22 ? "M" : null;
	}

	private setzeWahlQLK(fach: GostFach, wahl: GostSchuelerFachwahl): void {
		wahl.halbjahre[GostHalbjahr.Q11.id] = fach.istMoeglichQ11 ? "LK" : null;
		wahl.halbjahre[GostHalbjahr.Q12.id] = fach.istMoeglichQ12 ? "LK" : null;
		wahl.halbjahre[GostHalbjahr.Q21.id] = fach.istMoeglichQ21 ? "LK" : null;
		wahl.halbjahre[GostHalbjahr.Q22.id] = fach.istMoeglichQ22 ? "LK" : null;

		// Setze das Fach als 2. Abiturfach, wenn nicht bereits ein zweites Abiturfach gesetzt ist
		const alle_fachbelegungen = this.manager.getFachbelegungen();
		const lk1_belegt = this.manager.pruefeExistiertAbiFach(alle_fachbelegungen, GostAbiturFach.LK1);
		const lk2_belegt = this.manager.pruefeExistiertAbiFach(alle_fachbelegungen, GostAbiturFach.LK2);
		const istLK1 = !lk1_belegt && (GostFachbereich.DEUTSCH.hat(fach) || GostFachbereich.MATHEMATIK.hat(fach) || GostFachbereich.NATURWISSENSCHAFTLICH_KLASSISCH.hat(fach)
			|| (GostFachbereich.FREMDSPRACHE.hat(fach) && !fach.istFremdSpracheNeuEinsetzend));
		if (istLK1) {
			wahl.abiturFach = 1;
			return;
		}
		wahl.abiturFach = lk2_belegt ? null : 2;
	}

	private stepKunstMusikReligion(fach: GostFach, wahl: GostSchuelerFachwahl, halbjahr: GostHalbjahr, mode: LaufbahnplanungUiStepperMode): void {
		if (!GostFachbereich.KUNST_MUSIK.hat(fach) && !GostFachbereich.RELIGION.hat(fach)) {
			throw new DeveloperNotificationException("Die Methode stepKunstMusikReligion darf nur bei Kunst, Musik oder einem Religionsfach verwendet werden.");
		}

		// Hochschreibe-Modus
		const aktuell = wahl.halbjahre[halbjahr.id];
		if ((mode === 'hochschreiben') && (halbjahr.istEinfuehrungsphase())) {
			if (aktuell === null) {
				this.schreibeHochLeerZuSchriftlich(fach, wahl, halbjahr);
			} else if (aktuell === "S") {
				this.schreibeHochSchriftlichZuMuendlichKunstMusikReligion(fach, wahl, halbjahr);
			} else if (aktuell === "M") {
				this.schreibeHochMuendlicheZuLeer(fach, wahl, halbjahr);
			}
			return;
		}

		// Einführungsphase
		if (halbjahr.istEinfuehrungsphase()) {
			this.stepEFWahl(wahl, halbjahr);
			return;
		}

		// Qualifikationsphase - Halbjahre außer Q1.1
		if (halbjahr !== GostHalbjahr.Q11) {
			this.stepQWahlSonstige(fach, wahl, halbjahr, false, null);
			return;
		}

		// Qualifikationsphase - 1. Halbjahr mit hochschreiben
		if (aktuell === null) {
			this.setzeWahlQMuendliche(fach, wahl, true);
		} else if (aktuell === "M") {
			this.setzeWahlQSchriftlich(fach, wahl);
		} else if ((aktuell === "S") && fach.istMoeglichAbiLK) {
			this.setzeWahlQLK(fach, wahl);
		} else if (((aktuell === "S") || (aktuell === "LK"))) {
			this.leereWahlAb(wahl, halbjahr);
		}
	}


	private stepMatheDeutsch(fach: GostFach, wahl: GostSchuelerFachwahl, halbjahr: GostHalbjahr, mode: LaufbahnplanungUiStepperMode): void {
		if (!GostFachbereich.MATHEMATIK.hat(fach) && !GostFachbereich.DEUTSCH.hat(fach)) {
			throw new DeveloperNotificationException("Die Methode stepMatheDeutsch darf nur bei Mathematik und Deutsch verwendet werden.");
		}

		// Hochschreibe-Modus
		const aktuell = wahl.halbjahre[halbjahr.id];
		if ((mode === 'hochschreiben') && (halbjahr.istEinfuehrungsphase())) {
			if (aktuell === null) {
				this.schreibeHochLeerZuSchriftlich(fach, wahl, halbjahr);
			} else if (aktuell === "S") {
				this.schreibeHochSchriftlichZuLeer(fach, wahl, halbjahr);
			} else if (aktuell === "M") {
				this.schreibeHochMuendlicheZuLeer(fach, wahl, halbjahr);
			}
			return;
		}

		// Einführungsphase
		if (halbjahr.istEinfuehrungsphase()) {
			this.stepEFWahl(wahl, halbjahr);
			return;
		}

		// Qualifikationsphase - berücksichtige zuerst die Spezialfälle bei der Q2.2
		if ((aktuell === "M") && (halbjahr === GostHalbjahr.Q22)) {
			wahl.halbjahre[halbjahr.id] = "S";
			wahl.abiturFach = ((wahl.abiturFach === 4) || (wahl.abiturFach === 5)) && !this.manager.pruefeExistiertAbiFach(this.manager.getFachbelegungen(), GostAbiturFach.AB3)
				? 3 : null;
		} else if ((aktuell === "S") && (halbjahr === GostHalbjahr.Q22)) {
			wahl.halbjahre[halbjahr.id] = "M";
			wahl.abiturFach = (wahl.abiturFach === 3) && !this.manager.pruefeExistiertAbiFach(this.manager.getFachbelegungen(), GostAbiturFach.AB4) ? 4 : null;
		} else if ((aktuell === null) || (aktuell === "M") || ((aktuell === "S") && !fach.istMoeglichAbiLK)) {
			this.setzeWahlQSchriftlich(fach, wahl);
		} else if ((aktuell === "S") && fach.istMoeglichAbiLK) {
			this.setzeWahlQLK(fach, wahl);
		} else if ((aktuell === "LK")) {
			this.setzeWahlQSchriftlich(fach, wahl);
			wahl.abiturFach = null;
		}
	}


	private stepSport(fach: GostFach, wahl: GostSchuelerFachwahl, halbjahr: GostHalbjahr, mode: LaufbahnplanungUiStepperMode): void {
		if (!GostFachbereich.SPORT.hat(fach)) {
			throw new DeveloperNotificationException("Die Methode stepSport darf nur bei Sport verwendet werden.");
		}

		// Hochschreibe-Modus
		const aktuell = wahl.halbjahre[halbjahr.id];
		if ((mode === 'hochschreiben') && (halbjahr.istEinfuehrungsphase())) {
			if (aktuell === null) {
				this.schreibeHochLeerSport(fach, wahl, halbjahr);
			} else if (aktuell === "S") {
				this.schreibeHochSchriftlichSport(fach, wahl, halbjahr);
			} else if (aktuell === "M") {
				this.schreibeHochMuendlicheZuLeer(fach, wahl, halbjahr);
			}
			return;
		}

		// Einführungsphase
		if (halbjahr.istEinfuehrungsphase()) {
			if (aktuell === null) {
				wahl.halbjahre[halbjahr.id] = (fach.istMoeglichAbiGK || fach.istMoeglichAbiLK) ? "S" : "M";
			} else if (aktuell === "S") {
				wahl.halbjahre[halbjahr.id] = "M";
			} else if (aktuell === "M") {
				wahl.halbjahre[halbjahr.id] = null;
			}
			return;
		}

		// Qualifikationsphase - 1. Halbjahr mit hochschreiben
		if (halbjahr === GostHalbjahr.Q11) {
			if (aktuell === null) {
				this.setzeWahlQMuendliche(fach, wahl);
			} else if (fach.istMoeglichAbiLK && ((aktuell === "S") || ((aktuell === "M") && !fach.istMoeglichAbiGK))) {
				this.setzeWahlQLK(fach, wahl);
			} else if ((aktuell === "M") && (fach.istMoeglichAbiGK)) {
				this.setzeWahlQSchriftlich(fach, wahl);
			} else if ((aktuell === "S") || (aktuell === "LK") || ((aktuell === "M") && (!fach.istMoeglichAbiGK && !fach.istMoeglichAbiLK))) {
				this.leereWahlAb(wahl, halbjahr);
			}
		}

		// Qualifikationsphase - Sonstige Halbjahre
		if (aktuell === null) {
			wahl.halbjahre[halbjahr.id] = "M";
			wahl.abiturFach = (halbjahr === GostHalbjahr.Q22) && (wahl.abiturFach === 3) && !this.manager.pruefeExistiertAbiFach(this.manager.getFachbelegungen(), GostAbiturFach.AB4)
				? 4 : null;
		} else if ((aktuell === "M") && (fach.istMoeglichAbiGK)) {
			wahl.halbjahre[halbjahr.id] = "S";
			wahl.abiturFach = (halbjahr === GostHalbjahr.Q22) && (wahl.abiturFach === 4) && !this.manager.pruefeExistiertAbiFach(this.manager.getFachbelegungen(), GostAbiturFach.AB3)
				? 3 : null;
		} else if (fach.istMoeglichAbiLK && (wahl.halbjahre[halbjahr.previousOrException().id] === "LK") && ((aktuell === "S") || ((aktuell === "M") && !fach.istMoeglichAbiGK))) {
			wahl.halbjahre[halbjahr.id] = "LK";
		} else if ((aktuell === "S") || (aktuell === "LK") || (aktuell === "M")) {
			for (let hj: GostHalbjahr | null = halbjahr; hj !== null; hj = hj.next()) {
				wahl.halbjahre[hj.id] = null;
			}
			wahl.abiturFach = null;
		}
	}


	/**
	 * Die Stepper-Methode für die Fachwahlen in der EF im Hochschreibe-Modus
	 *
	 * @param fach       das Fach
	 * @param wahl       die Fachwahl
	 * @param halbjahr   das Halbjahr
	 */
	private stepEFWahlHochschreiben(fach: GostFach, wahl: GostSchuelerFachwahl, halbjahr: GostHalbjahr): void {
		const aktuell = wahl.halbjahre[halbjahr.id];
		if (aktuell === null) {
			this.schreibeHochLeerZuSchriftlich(fach, wahl, halbjahr);
		} else if (aktuell === "S") {
			this.schreibeHochSchriftlichZuMuendlich(fach, wahl, halbjahr);
		} else if (aktuell === "M") {
			this.schreibeHochMuendlicheZuLeer(fach, wahl, halbjahr);
		}
	}


	/**
	 * Die Stepper-Methode für die Fachwahlen in der EF.1 bzw. EF.2
	 *
	 * @param wahl       die Fachwahl
	 * @param halbjahr   das Halbjahr in der Einführungsphase
	 */
	private stepEFWahl(wahl: GostSchuelerFachwahl, halbjahr: GostHalbjahr): void {
		const aktuell = wahl.halbjahre[halbjahr.id];
		if (aktuell === null) {
			wahl.halbjahre[halbjahr.id] = "S";
		} else if (aktuell === "S") {
			wahl.halbjahre[halbjahr.id] = "M";
		} else if (aktuell === "M") {
			wahl.halbjahre[halbjahr.id] = null;
		}
	}


	private entferneZKWahl(wahl: GostSchuelerFachwahl, halbjahr: GostHalbjahr, beginnZK: GostHalbjahr | null): void {
		if ((beginnZK !== null) && (halbjahr !== GostHalbjahr.Q11) && (beginnZK === halbjahr.previousOrException())) {
			wahl.halbjahre[halbjahr.previousOrException().id] = null;
		}
		wahl.halbjahre[halbjahr.id] = null;
		if ((beginnZK !== null) && (halbjahr !== GostHalbjahr.Q22) && (beginnZK === halbjahr)) {
			wahl.halbjahre[halbjahr.nextOrException().id] = null;
		}
		wahl.abiturFach = null;
	}


	private stepZKWahl(fach: GostFach, wahl: GostSchuelerFachwahl, halbjahr: GostHalbjahr, beginnZK: GostHalbjahr | null, checkFachwahl: boolean): boolean {
		if (((halbjahr !== GostHalbjahr.Q11) && (beginnZK === halbjahr.previousOrException())) || ((halbjahr !== GostHalbjahr.Q22) && (beginnZK === halbjahr))) {
			if (checkFachwahl && (this.hatSchuelerFachwahl(wahl, beginnZK.previousOrException()) || this.uiManager.hatDoppelbelegung(fach, beginnZK.previousOrException()))) {
				return false;
			}
			wahl.halbjahre[beginnZK.id] = 'ZK';
			wahl.halbjahre[beginnZK.nextOrException().id] = 'ZK';
			return true;
		}
		return false;
	}


	/**
	 * Die Stepper-Methode für die Fachwahlen in der Q1.1
	 *
	 * @param fach       das Fach
	 * @param wahl       die Fachwahl
	 * @param hatZK      gibt an, on ein Zusatzkurs vorliegt oder nicht
	 * @param beginnZK   gibt den Beginn des Zusatzkurses an
	 */
	private stepQWahlQ11Hochschreiben(fach: GostFach, wahl: GostSchuelerFachwahl, hatZK: boolean, beginnZK: GostHalbjahr | null): void {
		const halbjahr = GostHalbjahr.Q11;
		const aktuell = wahl.halbjahre[halbjahr.id];
		if ((aktuell === null) && hatZK && (beginnZK !== null) && this.stepZKWahl(fach, wahl, halbjahr, beginnZK, true)) {
			return;
		}

		if (aktuell === null) {
			this.setzeWahlQMuendliche(fach, wahl);
		} else if (aktuell === "M") {
			this.setzeWahlQSchriftlich(fach, wahl);
		} else if (fach.istMoeglichAbiLK && (aktuell === "S")) {
			this.setzeWahlQLK(fach, wahl);
		} else if ((aktuell === "S") || (aktuell === "LK")) {
			this.leereWahlAb(wahl, halbjahr);
			if (hatZK && (beginnZK !== null)) {
				this.stepZKWahl(fach, wahl, halbjahr, beginnZK, false);
			}
		} else if (aktuell === "ZK") {
			this.entferneZKWahl(wahl, halbjahr, beginnZK);
		}
	}

	/**
	 * Die Stepper-Methode für die Fachwahlen in der Q1.2
	 *
	 * @param fach       das Fach
	 * @param wahl       die Fachwahl
	 * @param halbjahr   das Halbjahr
	 * @param hatZK      gibt an, on ein Zusatzkurs vorliegt oder nicht
	 * @param beginnZK   gibt den Beginn des Zusatzkurses an
	 */
	private stepQWahlSonstige(fach: GostFach, wahl: GostSchuelerFachwahl, halbjahr: GostHalbjahr, hatZK: boolean, beginnZK: GostHalbjahr | null): void {
		const aktuell = wahl.halbjahre[halbjahr.id];
		if ((aktuell === null) && hatZK && (beginnZK !== null) && this.stepZKWahl(fach, wahl, halbjahr, beginnZK, true)) {
			return;
		}

		if (aktuell === null) {
			wahl.halbjahre[halbjahr.id] = "M";
		} else if (aktuell === "M") {
			wahl.halbjahre[halbjahr.id] = "S";
			wahl.abiturFach = (halbjahr === GostHalbjahr.Q22) && ((wahl.abiturFach === 4) || (wahl.abiturFach === 5)) && !this.manager.pruefeExistiertAbiFach(this.manager.getFachbelegungen(), GostAbiturFach.AB3)
				? 3 : null;
		} else if (fach.istMoeglichAbiLK && (wahl.halbjahre[halbjahr.previousOrException().id] === "LK") && (aktuell === "S")) {
			wahl.halbjahre[halbjahr.id] = "LK";
		} else if ((aktuell === "S") || (aktuell === "LK")) {
			this.leereWahlAb(wahl, halbjahr);
			if (hatZK && (beginnZK !== null)) {
				this.stepZKWahl(fach, wahl, halbjahr, beginnZK, false);
			}
		} else if (aktuell === "ZK") {
			this.entferneZKWahl(wahl, halbjahr, beginnZK);
		}
	}


	/**
	 * Prüft, ob das aktuelle Halbjahr oder ein Folgehalbjahr bereits bewertet und damit für Änderungen gesperrt ist.
	 *
	 * @param halbjahr   das aktuelle Halbjahr
	 *
	 * @returns true, wenn es gesperrt ist, und ansonsten false
	 */
	private istHalbjahrGesperrt(halbjahr: GostHalbjahr): boolean {
		for (const hj of GostHalbjahr.values()) {
			if (hj.id >= halbjahr.id && this.manager.istBewertet(hj)) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Stepper für das Durchwandern der Auswahloptionen im Bereich der Halbjahre eines Faches
	 * im manuellen Modus und im Hochschreibemodus.
	 *
	 * @param fach       das Fach
	 * @param halbjahr   das Halbjahr
	 * @param wahl       die anzupassende Fachwahl
	 */
	private stepManuell(fach: GostFach, halbjahr: GostHalbjahr, wahl: GostSchuelerFachwahl): void {
		const hj = halbjahr.id;
		const istVTF = (this.uiManager.getFachgruppe(fach) === Fachgruppe.FG_VX);
		const istPJK = (this.uiManager.getFachgruppe(fach) === Fachgruppe.FG_PX);
		switch (wahl.halbjahre[hj]) {
			case "AT": // AT ist nicht mehr zulässig, wird dadurch aber bereinigt
			case "ZK":
				wahl.halbjahre[hj] = null;
				break;
			case null:
				wahl.halbjahre[hj] = (istPJK) ? "S" : "M";
				break;
			case "M":
				wahl.halbjahre[hj] = (!fach.istPruefungsordnungsRelevant || istVTF || istPJK || (GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(fach)))
					? null : "S";
				break;
			case "S":
				if (istPJK || (hj <= 1) || (GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(fach))) {
					wahl.halbjahre[hj] = null;
				} else { // in der Q-Phase als LK möglich, allerdings nicht im Fachbereich des literarisch-künstlerischen Bereichs
					wahl.halbjahre[hj] = "LK";
				}
				break;
			case "LK": {
				wahl.halbjahre[hj] = (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(fach) || GostFachbereich.GESCHICHTE.hat(fach)) ? "ZK" : null;
				break;
			}
			default:
				wahl.halbjahre[hj] = null;
				break;
		}
	}


	private stepQWahl(fach: GostFach, wahl: GostSchuelerFachwahl, halbjahr: GostHalbjahr): void {
		let hatZK = false;
		let beginnZK: GostHalbjahr | null = null;
		if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(fach)) {
			hatZK = this.jahrgang.hatZusatzkursSW && !this.uiManager.istBilingual(fach);
			beginnZK = hatZK ? GostHalbjahr.fromKuerzel(this.jahrgang.beginnZusatzkursSW) : null;
		} else if (GostFachbereich.GESCHICHTE.hat(fach)) {
			hatZK = (this.jahrgang.hatZusatzkursGE) && !this.uiManager.istBilingual(fach);
			beginnZK = hatZK ? GostHalbjahr.fromKuerzel(this.jahrgang.beginnZusatzkursGE) : null;
		}
		if (beginnZK === null) { // Fehlervermeidung: Ohne Beginn auch kein Zusatzkurs...
			hatZK = false;
		}

		if (halbjahr === GostHalbjahr.Q11) {
			this.stepQWahlQ11Hochschreiben(fach, wahl, hatZK, beginnZK);
		} else {
			this.stepQWahlSonstige(fach, wahl, halbjahr, hatZK, beginnZK);
		}
	}


	/**
	 * Stepper für das Durchwandern der Auswahloptionen im Bereich der Halbjahre eines Faches
	 * im manuellen Modus und im Hochschreibemodus.
	 *
	 * @param fach       das Fach
	 * @param halbjahr   das Halbjahr
	 * @param wahl       die anzupassende Fachwahl
	 */
	private step(fach: GostFach, halbjahr: GostHalbjahr, wahl: GostSchuelerFachwahl, mode: LaufbahnplanungUiStepperMode): void {
		// Mathematik und Deutsch
		if (GostFachbereich.MATHEMATIK.hat(fach) || GostFachbereich.DEUTSCH.hat(fach)) {
			this.stepMatheDeutsch(fach, wahl, halbjahr, mode);
			return;
		}

		// Projektkurse
		if (this.uiManager.getFachgruppe(fach) === Fachgruppe.FG_PX) {
			this.stepProjektkurs(fach, wahl, halbjahr);
			return;
		}

		// Vertiefungskurse
		if (this.uiManager.getFachgruppe(fach) === Fachgruppe.FG_VX) {
			this.stepVertiefungskurs(fach, wahl, halbjahr);
			return;
		}

		// Ersatzfach im Literarisch-Künstlerischen Bereich
		if (GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(fach)) {
			this.stepLiteratur(fach, wahl, halbjahr);
			return;
		}

		// Kunst, Musik und Religion
		if (GostFachbereich.KUNST_MUSIK.hat(fach) || GostFachbereich.RELIGION.hat(fach)) {
			this.stepKunstMusikReligion(fach, wahl, halbjahr, mode);
			return;
		}

		// Sport
		if (GostFachbereich.SPORT.hat(fach)) {
			this.stepSport(fach, wahl, halbjahr, mode);
			return;
		}

		// Fall: Hochschreiben in der EF
		if ((mode === 'hochschreiben') && (halbjahr.istEinfuehrungsphase())) {
			this.stepEFWahlHochschreiben(fach, wahl, halbjahr);
			return;
		}

		// Einführungsphase
		if (halbjahr.istEinfuehrungsphase()) {
			this.stepEFWahl(wahl, halbjahr);
			return;
		}

		// Qualifikationsphase
		this.stepQWahl(fach, wahl, halbjahr);
	}


	/**
	 * Stepper für das Durchwandern der Auswahloptionen im Bereich der Halbjahre eines Faches. Diese Methode ist ein
	 * Einsprungspunkt für die vue-Komponente und wählt je nach Modus die geeignete Methode aus.
	 *
	 * @param fach       das Fach
	 * @param halbjahr   das Halbjahr
	 * @param modus      der Modus in welchem der Stepper betrieben wird (normal, hochschreiben oder manuell)
	 *
	 * @returns -
	 */
	public async stepper(fach: GostFach, halbjahr: GostHalbjahr, modus?: LaufbahnplanungUiStepperMode) {
		// Bestimme den Modus für den Stepper und prüfe, ob Anpassungen erlaubt sind oder nicht
		const mode = modus ?? this.uiManager.modus;
		if (((mode !== 'manuell') && !this.uiManager.istMoeglich(fach, halbjahr)) || (this.istHalbjahrGesperrt(halbjahr))) {
			return;
		}

		// Bestimme die Schülerfachwahl
		const wahl = this.manager.getSchuelerFachwahl(fach.id);

		// Führe die Schritte des Steppers aus...
		if (wahl.halbjahre[halbjahr.id] === "AT") { // "AT" ist nicht mehr wählbar, wenn es dennoch gesetzt ist, dann wird es entfernt
			wahl.halbjahre[halbjahr.id] = null;
		} else if (mode === 'manuell') { // Manueller Stepper
			this.stepManuell(fach, halbjahr, wahl);
		} else { // Default Stepper
			this.step(fach, halbjahr, wahl, mode);
		}

		// Setze die neue Fachwahl
		await this.setWahl(fach.id, wahl);
	}

}
