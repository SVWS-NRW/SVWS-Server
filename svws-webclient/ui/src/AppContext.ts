import { DeveloperNotificationException } from '@core/core/exceptions/DeveloperNotificationException';
import { inject } from 'vue';
import type { App, InjectionKey } from 'vue';
import { createRouter, createWebHashHistory } from 'vue-router';
import type { Router } from 'vue-router';

/**
 * Die Klasse AppContext ermöglich den Zugriff auf die jeweilige vue-App in den einzelnen Clients.
 * Die vue-Instanz wird dabei über die Methode init in diese Klasse injiziert.
 */
export class AppContext {

	// Der Name des Containers, wo die vue-App gemountet wird
	private static readonly CONTAINER: string = '#app';

	// Die Instanz dieser Klasse
	private static _instance: AppContext | null = null;

	// Die Instanz der vue-App
	private readonly vueApp: App;

	// Die Instanz des vue-Routers
	private readonly vueRouter: Router;

	// Gibt an, ob die vue-App bereits gemountet wurde oder nicht
	private isMounted: boolean = false;


	/**
	 * Der private Konstruktor setzt die vue-App und den vue-Router für den Singleton
	 *
	 * @param app      die Instanz der vue-App
	 * @param router   die Instanz des vue-Routers
	 */
	private constructor(app: App, router: Router) {
		this.vueApp = app;
		this.vueRouter = router;
	}


	/**
	 * Initialisiert den globalen AppContext mit der übergebenen vue-App. Für diese
	 * App wird automatisch eine zugehörige vue-Router-Instanz erzeugt.
	 *
	 * Der Aufruf darf nur einmalig erfolgen. Bei einem zweiten Versuch wird eine Exception
	 * geschmissen.
	 *
	 * @param app      die Instanz der vue-App
	 */
	public static init(app: App): AppContext {
		// init darf nur einmalig aufgerufen werden
		if (AppContext._instance !== null) {
			throw new DeveloperNotificationException('Der AppContext wurde bereits initialisiert und darf nicht mehrfach initialisiert werden!');
		}

		// Erstelle die Router-Instanz und registriere diese
		const router = createRouter({
			history: createWebHashHistory(import.meta.env.BASE_URL),
			routes: [],
		});

		// Erzeuge die Instanz und gib diese auch zurück
		AppContext._instance = new AppContext(app, router);
		return AppContext._instance;
	}

	/**
	 * Gibt die mit init erzeugte Instanz des AppContext zurück.
	 * Wurde init noch nicht aufgerufen, so wird eine Exception geschmissen.
	 */
	public static get instance(): AppContext {
		if (AppContext._instance === null) {
			throw new DeveloperNotificationException('Der AppContext ist noch nicht initialisiert! Rufen Sie zuerst AppContext.init(app) in der main.ts auf.');
		}
		return AppContext._instance;
	}


	/**
	 * Gibt die globale Instanz der Vue-App zurück.
	 */
	public get app(): App {
		return this.vueApp;
	}

	/**
	 * Gibt die globale Instanz des Vue-Routers zurück.
	 */
	public get router(): Router {
		return this.vueRouter;
	}

	/**
	 * Mounted einmalig die Root-Komponente der Vue-App an den Container.
	 * Vor dem Mounten wird gewartet bis der Router fertig initialisiert und bereit ist.
	 * Ein zweiter Aufruf ist nicht zulässig und es wird dann eine Exception geschmissen.
	 * Das Hinzufügen eines States über provide ist danach im globalen Anwendungskontext nicht mehr möglich.
	 */
	public async mount(): Promise<void> {
		if (this.isMounted) {
			throw new DeveloperNotificationException('Die App wurde bereits gemountet!');
		}
		// Wartet asynchron, bis der Router initialisiert ist
		this.vueApp.use(this.vueRouter);
		await this.vueRouter.isReady();

		this.vueApp.mount(AppContext.CONTAINER);
		this.isMounted = true;
	}

	/**
	 * Führt die übergebene Funktion innerhalb des globalen Vue-App-Kontextes aus.
	 *
	 * @param fn   die im Kontext auszuführende Funktion mit dem Rückgabe-Wert vom Typ T
	 *
	 * @returns der Rückgabewert vom Typ T der Funktion
	 */
	public run<T>(fn: () => T): T {
		return this.vueApp.runWithContext(fn);
	}

	/**
	 * Globales Provide: Stellt einen State bzw. einen anderen Wert im globalen Vue-Kontext bereit.
	 * Diese Methode stellt die korrekte Reihenfolge sicher, so dass das Provide vor dem
	 * Mounten der vue-app erfolgen muss. Wird diese Reihenfolge nicht eingehalten, so wird
	 * eine Excption geschmissen.
	 *
	 * @param <T>   der Typ des bereitgestellten Objektes
	 * @param key   der Schlüssel unter welchem der State bzw. Wert zur Verfügung gestellt wird
	 */
	public provide<T>(key: InjectionKey<T> | string, value: T): void {
		if (this.isMounted) {
			throw new DeveloperNotificationException('Es können keine Abhängigkeiten via provide bereitgestellt werden, nachdem die App gemountet wurde!');
		}
		this.vueApp.provide(key, value);
	}

	/**
	 * Globales Inject: Holt den State bzw. den Wert mit dem übergebenen Schlüssel aus dem globalen Vue-Kontext.
	 *
	 * @param <T>   der Typ des injizierten Objektes
	 * @param key   der Schlüssel unter welchem der State bzw. Wert zur Verfügung gestellt wird
	 *
	 * @returns der State bzw. Wert vom Typ T, wenn unter dem Key vorher ein provide stattgefunden hat und ansonsten undefined
	 */
	public inject<T>(key: InjectionKey<T> | string): T | undefined {
		return this.run(() => inject(key));
	}

}
