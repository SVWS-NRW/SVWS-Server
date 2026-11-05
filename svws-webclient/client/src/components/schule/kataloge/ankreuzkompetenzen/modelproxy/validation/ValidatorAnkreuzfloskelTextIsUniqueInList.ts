import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import type { Ankreuzkompetenz } from "@core/core/data/schule/Ankreuzkompetenz";

/**
 * Prüft, ob der Floskeltext in der Liste pro Fach bzw. für alle ASV-Einträge eindeutig ist.
 * Ist noch kein Fach gewählt und handelt es sich nicht um einen ASV-Eintrag, so gibt es keine Duplikate.
 */
export class ValidatorAnkreuzfloskelTextIsUniqueInList extends BasicValidator {

	/** Eine Funktion, um auf die zu validierenden Daten zugreifen zu können. */
	private readonly data: () => Ankreuzkompetenz | null;
	/** Die Liste aller Ankreuzkompetenzen. */
	private readonly liste: () => Iterable<Ankreuzkompetenz>;

	/**
	 * Prüft, ob der Floskeltext in der Liste pro Fach bzw. für alle ASV-Einträge eindeutig ist.
	 *
	 * @param data	  Lambda für den zu prüfenden Eintrag (Proxy), unabhängig von der Auswahl im Manager
	 * @param liste   Lambda für die Liste aller Ankreuzkompetenzen
	 */
	constructor(data: () => Ankreuzkompetenz | undefined, liste: () => Iterable<Ankreuzkompetenz>) {
		super(ValidatorFehlerart.MUSS);
		this.data = () => data() ?? null;
		this.liste = liste;
	}

	/**
	 * Prüft die Eindeutigkeit des Floskeltextes.
	 *
	 * @returns true, wenn der Floskeltext pro Fach bzw. für ASV eindeutig ist
	 */
	protected pruefe(): boolean {
		const data = this.data();
		if ((data === null) || (!data.istASV && (data.idFach === null))) {
			return true;
		}
		const floskelText = data.floskelText.trim().toLowerCase();
		for (const ankreuzkompetenz of this.liste()) {
			if ((ankreuzkompetenz.id !== data.id)
					&& (ankreuzkompetenz.istASV === data.istASV)
					&& (ankreuzkompetenz.idFach === data.idFach)
					&& (ankreuzkompetenz.floskelText.trim().toLowerCase() === floskelText)) {
				this.addFehler(0, "Der Wert ist für dieses Fach bereits vergeben.");
				return false;
			}
		}
		return true;
	}

}
