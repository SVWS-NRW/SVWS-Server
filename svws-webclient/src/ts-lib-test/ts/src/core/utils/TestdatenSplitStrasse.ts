/**
 * DTO-Klasse für das einlesen von Testdaten aus
 * einer CSV-Resource für das Testen des Aufteilens 
 * von Strassennamen.
 */
 export class TestdatenSplitStrasse {

	/** Die Strasseninformation als ein kombinierter String */
	public strasse : String = "";

	/** Der Namensteil der Strasseninformation */
	public name : String = "";

	/** Der Teil mit der Hausnummer der Strasseninformation */
	public hausNr : String = "";
	
	/** Der Teil mit dem Hausnummerzusatz der Strasseninformation */
	public zusatz : String = "";

}
