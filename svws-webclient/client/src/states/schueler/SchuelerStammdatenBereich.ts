import type { SchuelerStammdaten } from "@core/asd/data/schueler/SchuelerStammdaten";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import type { List } from "@core/java/util/List";
import type { MultiModelProxy, MultiModelProxyConfiguration } from "@ui/model/MultiModelProxy";

import { GruppenprozessBereich, GruppenprozessBereichSchluessel } from "../GruppenprozessBereich";
import { api } from "~/router/Api";

import { SchuelerStammdatenMultiModelProxy } from "./SchuelerStammdatenMultiModelProxy";

/**
 * Der Bereich der Gruppenprozesse für die Stammdaten der Schüler der Mehrfachauswahl.
 */
export class SchuelerStammdatenBereich extends GruppenprozessBereich<SchuelerListeEintrag, SchuelerStammdaten> {

	/** Der Schlüssel, unter dem der Bereich bei den Gruppenprozessen angemeldet ist */
	public static readonly SCHLUESSEL = new GruppenprozessBereichSchluessel<SchuelerStammdatenBereich>("Gruppenprozesse der Schüler: Stammdaten");

	/**
	 * Erzeugt einen neuen Bereich für die Stammdaten der Schüler der Mehrfachauswahl.
	 */
	public constructor() {
		super(daten => daten.id);
	}

	public get schluessel(): GruppenprozessBereichSchluessel<SchuelerStammdatenBereich> {
		return SchuelerStammdatenBereich.SCHLUESSEL;
	}

	public readonly bezeichnung = "Individualdaten";

	/**
	 * Gibt den Proxy mit den ausstehenden Änderungen und der Aufbereitung für die UI zurück.
	 *
	 * @returns der Proxy
	 */
	public override get model(): SchuelerStammdatenMultiModelProxy {
		return super.model as SchuelerStammdatenMultiModelProxy;
	}

	protected override createModel(config: MultiModelProxyConfiguration<SchuelerStammdaten>): MultiModelProxy<SchuelerStammdaten> {
		return new SchuelerStammdatenMultiModelProxy(config, () => this.auswahlEintraege());
	}

	/** Der Schlüssel der Sperre des Speicherns für ein ungültiges Zuzugsjahr */
	private static readonly SPERRE_ZUZUGSJAHR = "zuzugsjahr";

	/** Das kleinste gültige Zuzugsjahr */
	public get minZuzugsjahr(): number {
		return new Date().getFullYear() + 1 - 100;
	}

	/** Das größte gültige Zuzugsjahr */
	public get maxZuzugsjahr(): number {
		return new Date().getFullYear() + 1;
	}

	/**
	 * Setzt das Zuzugsjahr als ausstehende Änderung. Ein Jahr außerhalb des gültigen Bereiches wird nicht übernommen, sperrt
	 * das Speichern und liefert den Hinweis dazu. Ohne Wert oder mit einem gültigen Wert wird eine Sperre aufgehoben.
	 *
	 * @param value   das Zuzugsjahr oder null
	 */
	public setZuzugsjahr(value: number | null): void {
		const ungueltig = (value !== null) && ((value < this.minZuzugsjahr) || (value > this.maxZuzugsjahr));
		if (ungueltig) {
			this.denyPatch(SchuelerStammdatenBereich.SPERRE_ZUZUGSJAHR, `Zuzugsjahr: Der Wert muss zwischen ${this.minZuzugsjahr} und ${this.maxZuzugsjahr} liegen.`);
		} else {
			this.allowPatch(SchuelerStammdatenBereich.SPERRE_ZUZUGSJAHR);
		}
		if ((value !== null) && !ungueltig) {
			this.model.zuzugsjahr.value = value;
		} else {
			this.model.removePending("zuzugsjahr");
		}
	}

	/**
	 * Verwirft die ausstehenden Änderungen der Felder zum Migrationshintergrund und hebt die Sperre des Zuzugsjahres auf.
	 */
	public resetMigrationshintergrund(): void {
		for (const attr of ["zuzugsjahr", "idGeburtsland", "idVerkehrspracheFamilie", "idGeburtslandMutter", "idGeburtslandVater"] as const) {
			this.model.removePending(attr);
		}
		this.allowPatch(SchuelerStammdatenBereich.SPERRE_ZUZUGSJAHR);
	}

	/**
	 * Gibt die Einträge der Mehrfachauswahl der Auswahlliste zurück. Ist der Bereich nicht (mehr) angemeldet, so gibt es keine
	 * Einträge. Das ist z.B. beim Verlassen der Gruppenprozesse der Fall: Der Bereich wird dann abgemeldet, während die
	 * Bindungen des Proxys (z.B. alleExtern) noch einmal neu berechnet werden können.
	 */
	private auswahlEintraege(): Iterable<SchuelerListeEintrag> {
		const eintraege: SchuelerListeEintrag[] = [];
		if (!this.istAngemeldet) {
			return eintraege;
		}
		for (const id of this.auswahlOrException.getMultiSelection()) {
			eintraege.push(this.getEintragOrException(id));
		}
		return eintraege;
	}

	protected async ladeDatenMultiple(ids: List<number>): Promise<Iterable<SchuelerStammdaten> | null> {
		if (ids.isEmpty()) {
			return null;
		}
		return await api.server.getSchuelerStammdatenMultiple(ids, api.schema);
	}

	protected override async doPatch(partials: List<Partial<SchuelerStammdaten>>): Promise<void> {
		api.status.start();
		try {
			await api.server.patchSchuelerStammdatenMultiple(partials, api.schema);
		} finally {
			api.status.stop();
		}
	}

}
