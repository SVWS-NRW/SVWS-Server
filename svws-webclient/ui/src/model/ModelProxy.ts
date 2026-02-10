import type { ComputedRef, Ref, ShallowRef } from "vue";
import { computed, ref, shallowRef, watch } from "vue";

import type { List } from "../../../core/src/java/util/List";
import type { ValidatorFehler } from "../../../core/src/asd/validate/ValidatorFehler";

import { ModelProxyHandler } from "./ModelProxyHandler";
import { ModelProxyValidation } from "./ModelProxyValidation";
import type { BasicValidator } from "../../../core/src/asd/validate/BasicValidator";


/**
 * Die Konfiguration für ein Model-Proxy für ein Model des typ T.
 */
interface ModelProxyConfigurationComplete<T extends object> {

	/** Der Lambda-Ausdruck für den Zugriff auf die "Original"-Daten, welche i.A. vom SVWS-Server stammen und dessen Zustand widerspiegeln */
	data: () => T;

	/** Gibt an, ob eine automatische Revalidierung erfolgen soll, wenn ein neuer Validator hinzugefügt wird. */
	autoRevalidate: boolean;

	/** Eine optionale Methode, um bei Änderungen an dem Pending-State automatisch einen Patch abzusetzen. */
	patch?: (data: Partial<T>) => Promise<boolean>;

	/** Gibt an, welche Props automatisch gepatcht werden sollen bei einer Änderung */
	listOfAutopatchProps: Iterable<keyof T>;

	/** Gibt an, ob vor dem ausführen eines Patches geprüft wird, ob der Pending-State valide ist. */
	checkValidBeforePatch: boolean;

	/** Gibt an, ob die Validierung zu Beginn komplett oder in Teilen deaktiviert ist */
	listOfDisabledPropValidations: Iterable<keyof T>;

}


/**
 * Ein Typ mit den verpflichtenden Felder für eine Model-Proxy-Konfiguration
 */
export type ModelProxyConfiguration<T extends object> = Partial<ModelProxyConfigurationComplete<T>> & Pick<ModelProxyConfigurationComplete<T>, 'data'>;



/**
 * Eine Klasse, welche einen Proxy bereitstellt, der den Zugriff auf ein Core-DTO abstrahiert. Dabei werden
 * Funktionalitäten, wie die Validierung und das direkte oder das verzögerte Patchen Attributen des Core-DTOs
 * unterstützt.
 * Der zu verwendende Proxy wird mit der Methode "getProxy" bereitgestellt.
 */
export class ModelProxy<T extends object> {

	/** Die Konfiguration des ModelProxies */
	private readonly _config: ModelProxyConfigurationComplete<T>;

	/** Die Klasse für die Validierung des Models */
	private readonly _validation: ModelProxyValidation<T>;

	/** Bestimmt die Original-Daten über den konfigurierten Lambda-Ausdruck und Aktualisiere diese Daten, sofern aufrgund der Reaktivität ein neues DTO vorliegt  */
	private readonly _dataForProxy: ComputedRef<T>;

	/** Das Proxy-Objekt zu den Daten, welches die "Original"-Daten mit dem Partial des Pending-States kombiniert */
	private readonly _proxy: ShallowRef<T>;

	/** Eine Referenz auf den aktuellen Pending-State, der in diesem ModelProxy verwaltet wird */
	private readonly _pending: Ref<Partial<T>>;

	/** Die Menge an props, die automatisch gepatched werden */
	private readonly setOfAutopatchProps: Set<keyof T>;


	/**
	 * Der Konstruktor zur Initialisierung des Model-Proxies
	 *
	 * @param config   ein Lambda für den Zugriff auf die "Original"-Daten
	 */
	public constructor(config: ModelProxyConfiguration<T>) {
		// Erstelle die eigentliche Konfiguration ggf. durch setzen von Default-Werten
		this._config = {
			data: config.data,
			autoRevalidate: config.autoRevalidate ?? false,
			patch: config.patch,
			listOfAutopatchProps: config.listOfAutopatchProps ?? [],
			checkValidBeforePatch: config.checkValidBeforePatch ?? false,
			listOfDisabledPropValidations: config.listOfDisabledPropValidations ?? [],
		};

		// das Set mit den automatisch zu patchenden Props wird hier initialisiert
		this.setOfAutopatchProps = new Set(this._config.listOfAutopatchProps);

		// Zunächst wird ein Objekt für die Validierung erzeugt. Diesem können mit der Methode addValidator Validatoren hinzugefügt werden
		this._validation = new ModelProxyValidation<T>(this._config.autoRevalidate, this._config.listOfDisabledPropValidations);

		// Die Daten werden zunächst in einem Computed gespeichert. Dieses fängt ab, falls Updates kommen, aber das DTO selbst sich nicht geändert hat.
		// In diesem Fall ist dann keine Erneuerung des Proxies nötig, da sich, wenn überhaupt, nur die Properties geändert haben.
		this._dataForProxy = computed<T>(() => this._config.data());

		// Pending enthält die Änderungen an den Daten, die seit der Erzeugung des Proxies bzw. seit dem letzten Patch angefallen sind.
		this._pending = ref({});

		// Erzeuge zunächst initial einen Proxy und setze diesen in einer ShallowRef - Reaktivität von getProxy()
		this._proxy = shallowRef(this.createNewProxy());
		watch(this._dataForProxy, () => {
			this._proxy.value = this.createNewProxy();
			this._pending.value = <Partial<T>>{};
			this.validate();
		});
	}

	/**
	 * Erstellt für die Daten einen neuen Proxy mit dem zugehörigen ModelProxyHandler
	 *
	 * @param data   die Daten, in diesem Fall identisch zu this.config.data()
	 *
	 * @returns der neue Proxy
	 */
	private createNewProxy(): T {
		return new Proxy(this._config.data(), new ModelProxyHandler<T>(this));
	}

	/**
	 * Fügt einen neuen Validator zur automatischen Validierung bei allen angegebenen Attributen hinzu.
	 *
	 * @param validator   der hinzuzufügende Validator
	 * @param prop        das Attribut des Proxies, bei welchem der Validator ausgeführt werden soll und welchem die
	 *                    Validatorfehler zugeordnet werden.
	 * @param props       die zusätzlichen Attribute des Proxies, bei welchen der Validator ausgeführt werden soll.	 */
	public addValidator(validator: BasicValidator, prop: keyof T, ...props: Array<keyof T>): void {
		this._validation.addValidator(validator, prop, ...props);
	}


	/**
	 * Gibt die "Original"-Daten zum aktuellen Zeitpunkt mithilfe des Zugriffs über die Referenz zurück.
	 *
	 * @returns die "Original"-Daten
	 */
	public get data(): T {
		return this._config.data();
	}


	/**
	 * Diese Methode ruft, sofern sie gesetzt ist, die Patch-Methode auf, um bei Änderungen an dem Pending-State
	 * automatisch einen Patch abzusetzen.
	 *
	 * Diese Methode kann bei Bedarf für komplexere Implementierungen in spezialisierten Klassen auch
	 * überschrieben werden.
	 *
	 * @returns true, wenn keine Patch-Methode vorhanden ist, der Pending-State leer ist oder die Patch-Methode erfolgreich ausgeführt wurde
	 *          false, wenn eine erfolgreiche Validierung für den Patch gefordert ist, diese jedoch fehlschlägt, oder der Patch selber fehlschlägt
	 */
	public async patch(): Promise<boolean> {
		if (this._config.patch === undefined) {
			return true;
		}
		const pending = this.pending;
		if (Object.keys(pending).length <= 0) {
			return true;
		}
		if (this._config.checkValidBeforePatch && this.hatFehler()) {
			return false;
		}
		const result = await this._config.patch(pending);
		this._pending.value = {};
		return result;
	}


	/**
	 * Aktualisiert den Pending-State und führt die Validierungen für dieses DTO aus.
	 * Ist eine Patch-Methode konfiguriert, so wird nach der Validierung der Patch ausgeführt,
	 * wenn es sich um einen von außen eingespieltes Update handelt oder auf eine einzelne Prop wirkt
	 * und die in der Liste der Autopatch-Props vorkommt.
	 *
	 * @param update   das Update für den Pending-State
	 * @param prop     das Attribut auf welches sich die Änderung im update bezieht, sofern es
	 *                 sich um eine Einzeländerung handelt.
	 */
	public async applyToPending(update: Partial<T>, prop?: keyof T): Promise<void> {
		if ((prop !== undefined) && (Object.keys(update).length !== 1)) {
			throw new Error("Ist der Parameter prop gesetzt, so muss das update genau ein Attribut enthalten.");
		}
		this._pending.value = { ...this._pending.value, ...update };
		this._validation.validate(prop);
		if ((prop === undefined) || this.setOfAutopatchProps.has(prop)) {
			await this.patch();
		}
	}


	/**
	 * Gibt den aktuellen Pending-State zurück.
	 *
	 * @returns der aktuelle Pending-State
	 */
	public get pending(): Partial<T> {
		return this._pending.value;
	}

	/**
	 * Setzt den Pending-State und führt anschließend die Validierungen.
	 * Es wird in keinem Fall eine Patch-Methode ausgeführt.
	 *
	 * @param update   das Update für den Pending-State
	 */
	public set pending(state: Partial<T>) {
		this._pending.value = state;
		this._validation.validate();
	}


	/**
	 * Gibt das Proxy-Objekt zurück, welches für den lesenden und schreibenden Zugriff auf die Daten von Seiten
	 * der UI verwendet werden sollte.
	 *
	 * @returns das Proxy-Objekt
	 */
	public get proxy(): T {
		return this._proxy.value;
	}


	/**
	 * Gibt alle Fehler zurück, welche bei der Validierung aufgetreten sind.
	 *
	 * @param prop   das Attribut, für welches die Fehlerliste erzeugt werden soll
	 *
	 * @returns die Fehlerliste für das Attribut
	 */
	public getFehler(prop: keyof T): List<ValidatorFehler> {
		return this._validation.getFehler(prop);
	}

	/**
	 * Führt eine Validierung durch und aktualisiert die Fehlerlisten.
	 */
	public validate(): void {
		this._validation.validate();
	}

	/**
	 * Gibt alle Fehler zurück, die bei den Validierungen aller Attribute aufgetreten sind.
	 *
	 * @returns die Fehlerliste mit allen Fehlern in Bezug auf das Proxy-DTO
	 */
	public getAlleFehler(): List<ValidatorFehler> {
		return this._validation.getAlleFehler();
	}

	/**
	 * Gibt zurück, ob bei der Validierung aller Attribute mindestens ein Fehler aufgetreten ist.
	 *
	 * @returns true, wenn mindestens ein Fehler aufgetreten ist, und ansonsten false
	 */
	public hatFehler(): boolean {
		return !this._validation.getAlleFehler().isEmpty();
	}

	/**
	 * Wechselt den Status für das übergebene Attribut, ob eine Valididerung
	 * stattfindet oder nicht.
	 *
	 * @param prop   das Attribut
	 */
	public toggleValidation(prop: keyof T): void {
		this._validation.toggle(prop);
	}

}
