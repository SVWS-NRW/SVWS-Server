import type { RouteNode } from "~/router/RouteNode";

/**
 * Ein Guard, der eine Navigation unterbrechen kann, z.B. weil auf der aktuellen Seite ungespeicherte Änderungen vorliegen.
 * Der {@link RouteManager} fragt vor jeder Navigation alle registrierten Guards. Blockiert ein Guard, so wartet der
 * RouteManager auf dessen Bestätigung, bevor die Navigation fortgesetzt wird.
 */
export interface NavigationGuard {

	/**
	 * Gibt an, ob die Navigation vom aktuellen Knoten zum Zielknoten bestätigt werden muss.
	 *
	 * @param to     der Zielknoten der Navigation
	 * @param from   der aktuelle Knoten
	 *
	 * @returns true, wenn die Navigation bestätigt werden muss
	 */
	isBlocked(to: RouteNode<any, any>, from: RouteNode<any, any>): boolean;

	/**
	 * Holt die Bestätigung für die blockierte Navigation ein, z.B. durch eine Rückfrage beim Benutzer, und führt
	 * die dazu gewählten Aktionen aus (z.B. Verwerfen oder Speichern).
	 *
	 * @param to   der Zielknoten der Navigation
	 *
	 * @returns true, wenn die Navigation fortgesetzt werden soll, false, wenn sie abgebrochen wird
	 */
	queryConfirmation(to: RouteNode<any, any>): Promise<boolean>;

}
