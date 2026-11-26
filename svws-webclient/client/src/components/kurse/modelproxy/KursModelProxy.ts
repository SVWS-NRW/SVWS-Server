import { computed } from "vue";

import type { KursDaten } from "@core/asd/data/kurse/KursDaten";
import type { FachDaten } from "@core/core/data/fach/FachDaten";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import { KursFortschreibungsart } from "@core/core/types/KursFortschreibungsart";
import { JavaInteger } from "@core/java/lang/JavaInteger";
import { ArrayList } from "@core/java/util/ArrayList";
import { Arrays } from "@core/java/util/Arrays";
import { ModelProxy } from "@ui/model/ModelProxy";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorNumberRange } from "@ui/validation/common/ValidatorNumberRange";
import { ValidatorStringIsUniqueInList } from "@ui/validation/common/ValidatorStringIsUniqueInList";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { StringPattern, ValidatorStringMatchesPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

import { useKurseAuswahlState } from "~/states/kurse/KurseAuswahlState";

/**
 *ModelProxy für Kurse
 */
export class KursModelProxy extends ModelProxy<KursDaten> {

	private readonly _kurseAuswahlState = useKurseAuswahlState();

	/**
	 * ModelProxy für Kurse
	 *
	 * @param data Lambda für den Zugriff auf die Original-Daten
	 * @param patch Methode zum Patchen einzelner Attribute
	 */
	constructor(
		data: () => KursDaten,
		patch?: (data: Partial<KursDaten>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof KursDaten> =
			["kursartAllg", "idFach", "idJahrgaenge", "schienen", "idKursFortschreibungsart", "idLehrer", "istSichtbar"];
		super({ data, patch, listOfAutopatchProps });
		this.addValidatoren();
		this.setInactiveValidators();
		this.validate();
	}

	/**
	 * Setzt das Proxy-Objekt zurück und passt anschließend an, ob die Wochenstunden der Lehrkraft
	 * geprüft werden. Wird auch beim Wechsel auf einen anderen Kurs aufgerufen.
	 */
	public override reset(): void {
		super.reset();
		this.setInactiveValidators();
	}

	private addValidatoren(): void {
		// Kürzel
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.kuerzel), "kuerzel");
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.kuerzel, null, 20), "kuerzel");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.kuerzel, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "kuerzel");
		this.addBlockingValidator(new ValidatorStringIsUniqueInList<KursDaten>(
			() => this.proxy,
			(data: KursDaten) => data.id,
			(data: KursDaten) => data.kuerzel,
			() => this._kurseAuswahlState.liste.list(),
			false
		), "kuerzel");
		// Fach
		this.addBlockingValidator(new ValidatorInputRequired(() => (this.proxy.idFach === 0) ? null : this.proxy.idFach), "idFach");
		// Kursart
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.kursartAllg), "kursartAllg");
		// Wochenstunden
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.wochenstunden), "wochenstunden");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.wochenstunden, 0, 40), "wochenstunden");
		// sortierung
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.sortierung), "sortierung");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, JavaInteger.MAX_VALUE), "sortierung");
		// Zeugnisbezeichnung
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.bezeichnungZeugnis, null, 130), "bezeichnungZeugnis");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.bezeichnungZeugnis, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "bezeichnungZeugnis");
		// Fortschreibungsart
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.idKursFortschreibungsart), "idKursFortschreibungsart");
		// Wochenstunden Lehrkraft (nur prüfen, wenn eine Lehrkraft gesetzt ist)
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.wochenstundenLehrer), "wochenstundenLehrer", "idLehrer");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.wochenstundenLehrer, 0, null), "wochenstundenLehrer");
	}

	// Wochenstunden der Lehrkraft werden nur geprüft, wenn eine Lehrkraft gesetzt ist
	private setInactiveValidators(): void {
		if (this.proxy.idLehrer === null) {
			this.disableValidation("wochenstundenLehrer");
		} else {
			this.enableValidation("wochenstundenLehrer");
		}
	}

	// computeds

	schuelerListe = computed(() => this._kurseAuswahlState.schuelerstatus.filter(this.proxy.schueler));

	lehrer = computed<LehrerListeEintrag | null>({
		get: () => {
			const idLehrer = this.proxy.idLehrer;
			return (idLehrer === null) ? null : this._kurseAuswahlState.lehrer.get(idLehrer);
		},
		set: (value) => {
			const idLehrer = value?.id ?? null;
			if (idLehrer === null) {
				this.proxy.wochenstundenLehrer = 0;
			}
			this.proxy.idLehrer = idLehrer;
			this.setInactiveValidators();
		},
	});

	fach = computed<FachDaten | null>({
		get: () => this._kurseAuswahlState.faecher.get(this.proxy.idFach),
		set: (value) => this.proxy.idFach = value?.id ?? 0,
	});

	jahrgaenge = computed<JahrgangsDaten[]>({
		get: () => Array.from(this.proxy.idJahrgaenge, id => this._kurseAuswahlState.jahrgaenge.get(id)).filter(j => j !== null),
		set: (value) => this.proxy.idJahrgaenge = ArrayList.of(...value.map(j => j.id)),
	});

	kursartAllg = computed({
		get: () => (this.proxy.kursartAllg === "") ? null : this.proxy.kursartAllg,
		set: (v: string | null) => this.proxy.kursartAllg = v ?? "",
	});

	schienenArray = computed<number[]>({
		get: () => this.proxy.schienen.toArray(new Array<number>),
		set: (value) => this.proxy.schienen = Arrays.asList([...value].sort((a, b) => a - b)),
	});

	fortschreibungsart = computed<KursFortschreibungsart | null>({
		get: () => KursFortschreibungsart.fromID(this.proxy.idKursFortschreibungsart),
		set: (value) => {
			if (value === null) {
				return;
			}
			this.proxy.idKursFortschreibungsart = value.id;
		},
	});
}
