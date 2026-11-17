import { computed } from "vue";

import type { InternatArtKatalogEintrag } from "@core/asd/data/schule/InternatArtKatalogEintrag";
import type { ZusatzerhebungenStatistikGesamt } from "@core/asd/data/statistik/ZusatzerhebungenStatistikGesamt";
import { InternatArt } from "@core/asd/types/schule/InternatArt";
import { JavaInteger } from "@core/java/lang/JavaInteger";
import { ModelProxy } from "@ui/model/ModelProxy";
import { ValidatorNumberRange } from "@ui/validation/common/ValidatorNumberRange";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { StringPattern, ValidatorStringMatchesPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

/**
 * ModelProxy für die Daten der Zusatzerhebungen zur amtlichen Schulstatistik.
 */
export class ZusatzerhebungenModelProxy extends ModelProxy<ZusatzerhebungenStatistikGesamt> {

	/**
	 * ModelProxy für die Daten der Zusatzerhebungen zur amtlichen Schulstatistik.
	 *
	 * @param data    Lambda für den Zugriff auf die Original-Daten
	 * @param patch   Methode zum Patchen einzelner Attribute
	 */
	public constructor(
		data: () => ZusatzerhebungenStatistikGesamt,
		patch?: (data: Partial<ZusatzerhebungenStatistikGesamt>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof ZusatzerhebungenStatistikGesamt> = ["hatInternationaleKontakte"];
		super({ data, patch, listOfAutopatchProps });
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {
		this.addValidatorenAnsprechpartner();
		this.addValidatorenSchulanmeldungen();
		this.addValidatorenInternatsplaetze();
		this.addValidatorenKlinikschule();
	}

	private addValidatorenAnsprechpartner() {
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.statistikAnsprechpartnerName, null, 255), "statistikAnsprechpartnerName");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.statistikAnsprechpartnerName, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES),
			"statistikAnsprechpartnerName");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.statistikAnsprechpartnerTelefon, StringPattern.IS_PHONE_NUMBER),
			"statistikAnsprechpartnerTelefon");
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.statistikAnsprechpartnerTelefon, null, 20), "statistikAnsprechpartnerTelefon");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.statistikAnsprechpartnerEmail, StringPattern.IS_EMAIL), "statistikAnsprechpartnerEmail");
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.statistikAnsprechpartnerEmail, null, 100), "statistikAnsprechpartnerEmail");
	}

	private addValidatorenSchulanmeldungen() {
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.anzahlSchulanmeldungen, 0, JavaInteger.MAX_VALUE), "anzahlSchulanmeldungen");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.anzahlZurueckstellungsverfahren, 0, JavaInteger.MAX_VALUE), "anzahlZurueckstellungsverfahren");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.anzahlZurueckstellungenInsgesamt, 0, JavaInteger.MAX_VALUE), "anzahlZurueckstellungenInsgesamt");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.anzahlZurueckstellungenWeiblich, 0, JavaInteger.MAX_VALUE), "anzahlZurueckstellungenWeiblich");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.anzahlZurueckstellungenAuslaenderZusammen, 0, JavaInteger.MAX_VALUE), "anzahlZurueckstellungenAuslaenderZusammen");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.anzahlZurueckstellungenAuslaenderWeiblich, 0, JavaInteger.MAX_VALUE), "anzahlZurueckstellungenAuslaenderWeiblich");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.anzahlZurueckstellungenInsgesamtQuartal4, 0, JavaInteger.MAX_VALUE), "anzahlZurueckstellungenInsgesamtQuartal4");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.anzahlZurueckstellungenInsgesamtQuartal1, 0, JavaInteger.MAX_VALUE), "anzahlZurueckstellungenInsgesamtQuartal1");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.anzahlZurueckstellungenInsgesamtQuartal2, 0, JavaInteger.MAX_VALUE), "anzahlZurueckstellungenInsgesamtQuartal2");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.anzahlZurueckstellungenInsgesamtQuartal3, 0, JavaInteger.MAX_VALUE), "anzahlZurueckstellungenInsgesamtQuartal3");
	}

	private addValidatorenInternatsplaetze() {
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.anzahlInternatplaetzeJungenMoeglich, 0, JavaInteger.MAX_VALUE), "anzahlInternatplaetzeJungenMoeglich");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.anzahlInternatplaetzeJungenBelegt, 0, JavaInteger.MAX_VALUE), "anzahlInternatplaetzeJungenBelegt");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.anzahlInternatplaetzeMaedchenMoeglich, 0, JavaInteger.MAX_VALUE), "anzahlInternatplaetzeMaedchenMoeglich");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.anzahlInternatplaetzeMaedchenBelegt, 0, JavaInteger.MAX_VALUE), "anzahlInternatplaetzeMaedchenBelegt");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.anzahlInternatplaetzeNeutralMoeglich, 0, JavaInteger.MAX_VALUE), "anzahlInternatplaetzeNeutralMoeglich");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.anzahlInternatplaetzeNeutralBelegt, 0, JavaInteger.MAX_VALUE), "anzahlInternatplaetzeNeutralBelegt");
	}

	private addValidatorenKlinikschule() {
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.durchschnittKlinikschuleAllgemeinbildendInsgesamt, 0, null), "durchschnittKlinikschuleAllgemeinbildendInsgesamt");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte, 0, null), "durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt, 0, null), "durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte, 0, null), "durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt, 0, null), "durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte, 0, null), "durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte");
	}

	/** Die aktuell gewählte Internatsart */
	private readonly _internatArtWert = computed<InternatArt | null>(() =>
		(this.proxy.idInternatArt === null) ? null : InternatArt.data().getWertByID(this.proxy.idInternatArt));

	/**
	 * Die Internatsart als Katalog-Eintrag. Beim Ändern werden die Internatsplätze, welche für die
	 * neue Internatsart nicht zulässig sind, auf null zurückgesetzt und zusammen mit der Internatsart gepatcht.
	 */
	public readonly internatArt = computed<InternatArtKatalogEintrag | null>({
		get: () => (this.proxy.idInternatArt === null) ? null : InternatArt.data().getEintragByID(this.proxy.idInternatArt),
		set: (eintrag: InternatArtKatalogEintrag | null) => {
			const art = (eintrag === null) ? null : InternatArt.data().getWertByID(eintrag.id);
			const update: Partial<ZusatzerhebungenStatistikGesamt> = { idInternatArt: eintrag?.id ?? null };
			if (!ZusatzerhebungenModelProxy.erlaubtJungenplaetze(art)) {
				if (this.proxy.anzahlInternatplaetzeJungenMoeglich !== null) {
					update.anzahlInternatplaetzeJungenMoeglich = null;
				}
				if (this.proxy.anzahlInternatplaetzeJungenBelegt !== null) {
					update.anzahlInternatplaetzeJungenBelegt = null;
				}
			}
			if (!ZusatzerhebungenModelProxy.erlaubtMaedchenplaetze(art)) {
				if (this.proxy.anzahlInternatplaetzeMaedchenMoeglich !== null) {
					update.anzahlInternatplaetzeMaedchenMoeglich = null;
				}
				if (this.proxy.anzahlInternatplaetzeMaedchenBelegt !== null) {
					update.anzahlInternatplaetzeMaedchenBelegt = null;
				}
			}
			if (!ZusatzerhebungenModelProxy.erlaubtNeutralplaetze(art)) {
				if (this.proxy.anzahlInternatplaetzeNeutralMoeglich !== null) {
					update.anzahlInternatplaetzeNeutralMoeglich = null;
				}
				if (this.proxy.anzahlInternatplaetzeNeutralBelegt !== null) {
					update.anzahlInternatplaetzeNeutralBelegt = null;
				}
			}
			void this.applyToPending(update);
		},
	});

	/** Gibt an, ob für die gewählte Internatsart Internatsplätze erfasst werden */
	public readonly hatInternatsplaetze = computed<boolean>(() => (this._internatArtWert.value !== null) && (this._internatArtWert.value !== InternatArt.KEIN_INTERNAT));

	/** Gibt an, ob für die gewählte Internatsart Plätze für Jungen erfasst werden können */
	public readonly erlaubtJungenplaetze = computed<boolean>(() => ZusatzerhebungenModelProxy.erlaubtJungenplaetze(this._internatArtWert.value));

	/** Gibt an, ob für die gewählte Internatsart Plätze für Mädchen erfasst werden können */
	public readonly erlaubtMaedchenplaetze = computed<boolean>(() => ZusatzerhebungenModelProxy.erlaubtMaedchenplaetze(this._internatArtWert.value));

	/** Gibt an, ob für die gewählte Internatsart geschlechtsneutrale Plätze erfasst werden können */
	public readonly erlaubtNeutralplaetze = computed<boolean>(() => ZusatzerhebungenModelProxy.erlaubtNeutralplaetze(this._internatArtWert.value));

	private static erlaubtJungenplaetze(art: InternatArt | null): boolean {
		return (art === InternatArt.INTERNAT_JUNGEN) || (art === InternatArt.INTERNAT_NEUTRAL);
	}

	private static erlaubtMaedchenplaetze(art: InternatArt | null): boolean {
		return (art === InternatArt.INTERNAT_MAEDCHEN) || (art === InternatArt.INTERNAT_NEUTRAL);
	}

	private static erlaubtNeutralplaetze(art: InternatArt | null): boolean {
		return (art === InternatArt.INTERNAT_NEUTRAL);
	}

}
