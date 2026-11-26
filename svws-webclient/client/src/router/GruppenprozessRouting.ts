import { ViewType } from "@ui/ui/nav/ViewType";

import { GruppenprozessNavigationGuard } from "~/router/GruppenprozessNavigationGuard";
import { RouteManager } from "~/router/RouteManager";
import type { RouteNode } from "~/router/RouteNode";
import type { GenericGruppenprozessState } from "~/states/GenericGruppenprozessState";

/**
 * Das Routing der Gruppenprozesse zu einer Auswahl, gebündelt an der Route der Auswahl, damit die einzelnen
 * Gruppenprozess-Tabs (mit und ohne Bereich) dafür nichts tun müssen:
 * - Beim Betreten der Auswahl wird der {@link GruppenprozessNavigationGuard} angemeldet, der beim Verlassen mit ausstehenden
 *   Änderungen nachfragt.
 * - Verlässt die Navigation die Gruppenprozesse der Auswahl (zu einer anderen Ansicht oder Auswahl), so werden die Bereiche
 *   abgemeldet und deren Daten und ausstehende Änderungen verworfen. Ein Wechsel zwischen den Tabs der Gruppenprozesse
 *   behält dagegen die Bereiche und die ausstehenden Änderungen.
 */
export class GruppenprozessRouting {

	/** Die Route der Auswahl, zu der die Gruppenprozess-Routen als Kinder gehören */
	private readonly _auswahlRoute: RouteNode<any, any>;

	/** Liefert den Orchestrator der Gruppenprozesse der Auswahl */
	private readonly _gruppenprozesse: () => GenericGruppenprozessState<any>;

	/** Meldet den Guard beim RouteManager ab, solange er angemeldet ist */
	private _guardAbmelden: (() => void) | undefined = undefined;


	/**
	 * Erzeugt das Routing der Gruppenprozesse.
	 *
	 * @param auswahlRoute      die Route der Auswahl, zu der die Gruppenprozess-Routen gehören
	 * @param gruppenprozesse   liefert den Orchestrator der Gruppenprozesse der Auswahl
	 */
	public constructor(auswahlRoute: RouteNode<any, any>, gruppenprozesse: () => GenericGruppenprozessState<any>) {
		this._auswahlRoute = auswahlRoute;
		this._gruppenprozesse = gruppenprozesse;
	}

	private istGruppenprozessRoute(node: RouteNode<any, any>): boolean {
		return node.hasType(ViewType.GRUPPENPROZESSE) && (node.parent === this._auswahlRoute);
	}

	/**
	 * Wird beim Aktualisieren der Auswahl-Route aufgerufen: Meldet den Guard an, sofern noch nicht geschehen, und räumt auf,
	 * wenn die Navigation die Gruppenprozesse verlässt.
	 *
	 * @param to     der Zielknoten der Navigation
	 * @param from   der Knoten, von dem aus navigiert wird
	 */
	public update(to: RouteNode<any, any>, from: RouteNode<any, any> | undefined): void {
		this._guardAbmelden ??= RouteManager.registerGuard(new GruppenprozessNavigationGuard(this._auswahlRoute, this._gruppenprozesse()));
		if ((from !== undefined) && this.istGruppenprozessRoute(from) && !this.istGruppenprozessRoute(to)) {
			this._gruppenprozesse().reset();
		}
	}

	/**
	 * Wird beim Verlassen der Auswahl-Route aufgerufen: Meldet den Guard ab und setzt die Gruppenprozesse zurück.
	 */
	public leave(): void {
		this._guardAbmelden?.();
		this._guardAbmelden = undefined;
		this._gruppenprozesse().reset();
	}

}
