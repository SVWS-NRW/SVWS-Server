import type { Teilstandort } from "@core/core/data/schule/Teilstandort";
import { ModelProxy } from "@ui/model/ModelProxy";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorTeilstandortAdrMerkmal } from "~/components/schule/stammdaten/adressdaten/modelproxy/validation/ValidatorTeilstandortAdrMerkmal";

/**
 * ModelProxy für Teilstandorte
 */
export class TeilstandortModelProxy extends ModelProxy<Teilstandort> {

	private readonly _liste: () => Iterable<Teilstandort>;

	/**
	 * ModelProxy für Teilstandorte
	 *
	 * @param data 			Lambda für den Zugriff auf die Original-Daten
	 * @param liste         Lambda für den Zugriff auf die Liste aller Teilstandorte
	 */
	constructor(
		data: () => Teilstandort,
		liste: () => Iterable<Teilstandort>
	) {
		super({ data });
		this._liste = liste;
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {
		this.addValidator(new ValidatorTeilstandortAdrMerkmal(() => this.proxy, this._liste), 'adrMerkmal');

		this.addValidator(new ValidatorStringLength(() => this.proxy.kuerzel, null, 30), 'kuerzel');
		this.addValidator(new ValidatorStringLength(() => this.proxy.strassenname, null, 55), 'strassenname');
		this.addValidator(new ValidatorStringLength(() => this.proxy.hausNr, null, 10), 'hausNr');
		this.addValidator(new ValidatorStringLength(() => this.proxy.hausNrZusatz, null, 30), 'hausNrZusatz');
		this.addValidator(new ValidatorStringLength(() => this.proxy.plz, null, 10), 'plz');
		this.addValidator(new ValidatorStringLength(() => this.proxy.ort, null, 50), 'ort');
		this.addValidator(new ValidatorStringLength(() => this.proxy.bemerkung, null, 50), 'bemerkung');

	}
}
