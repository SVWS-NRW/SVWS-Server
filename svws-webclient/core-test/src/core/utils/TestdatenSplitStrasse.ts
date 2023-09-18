/**
 * DTO-Klasse für das einlesen von Testdaten aus
 * einer CSV-Resource für das Testen des Aufteilens
 * von Strassennamen.
 */
export class TestdatenSplitStrasse {

	/** Die Strasseninformation als ein kombinierter String */
	public strasse : string = "";

	/** Der Namensteil der Strasseninformation */
	public name : string = "";

	/** Der Teil mit der Hausnummer der Strasseninformation */
	public hausNr : string = "";

	/** Der Teil mit dem Hausnummerzusatz der Strasseninformation */
	public zusatz : string = "";

}
