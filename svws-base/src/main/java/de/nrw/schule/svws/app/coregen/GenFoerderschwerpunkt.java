package de.nrw.schule.svws.app.coregen;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import de.nrw.schule.svws.csv.statkue.DataStatkueFoerderschwerpunkt;

/**
 * Der Core-Type-Updater für die Förderschwerpunkte 
 */
public class GenFoerderschwerpunkt extends CoreTypeUpdater {

	/**
	 * Erzeugt einen neuen Generator mit dem angebebenen logger für 
	 * den Core-Type Foerderschwerpunkt
	 */
	protected GenFoerderschwerpunkt() {
		super("Foerderschwerpunkt", "statkue/Foerderschwerpunkt.java");
	}


	@Override
	public void generate(StringBuilder sb) {
		logger.logLn("- Filtere Duplikate...");
		HashMap<String, DataStatkueFoerderschwerpunkt> isDistinctMap = new HashMap<>();
		List<DataStatkueFoerderschwerpunkt> fsps = DataStatkueFoerderschwerpunkt.get();
		for (DataStatkueFoerderschwerpunkt dto : fsps) {
			String key = dto.FSP + "/" + dto.SF + "/" + dto.Beschreibung + "/" + dto.gueltigVon + "/" + dto.gueltigBis;
			if (isDistinctMap.get(key) == null)
				isDistinctMap.put(key, dto);
		}
		logger.logLn("- Sortiere DTOs...");
		fsps = isDistinctMap.values().stream()
				.sorted((a,b) -> {
					int tmp = a.FSP.compareTo(b.FSP);
					if (tmp != 0)
						return tmp;
					tmp = a.SF.compareTo(b.SF);
					if (tmp != 0)
						return tmp;
					tmp = a.Beschreibung.compareTo(b.Beschreibung);
					if (tmp != 0)
						return tmp;
					if (a.gueltigVon == null)
						return -1;
					tmp = a.gueltigVon.compareTo(b.gueltigVon);
					if (tmp != 0)
						return tmp;
					if (a.gueltigBis == null)
						return -1;
					return a.gueltigBis.compareTo(b.gueltigBis);
				})
				.collect(Collectors.toList());
		logger.logLn("- Aggregiere die Informationen der einzelnen Förderschwerpunkte...");
		HashMap<String, String> fspSchulformen = new HashMap<>();
		HashMap<DataStatkueFoerderschwerpunkt, String> fspJavaName = new HashMap<>();
		HashMap<String, String> fspBezeichnungen = new HashMap<>();
		HashMap<String, DataStatkueFoerderschwerpunkt> fspDTOs = new HashMap<>();
		for (DataStatkueFoerderschwerpunkt dto : fsps) {
			// Ermittle den Java-Namen des Core-Type-Eintrags
			String javaName = ("**".equals(dto.FSP)) ? "KEINER" : dto.FSP.replace("Ä", "AE").replace("Ö", "OE").replace("Ü", "UE");
			fspJavaName.put(dto, javaName);
			fspDTOs.put(javaName, dto);
			// Aggregiere weitere Informationen
			String schulformen = fspSchulformen.get(javaName);
			fspSchulformen.put(javaName, ((schulformen == null) ? "" : schulformen + ", ")
					+ System.lineSeparator()
					+ "\t\t\tSchulform." + dto.SF + ".daten.kuerzel");
			String bezeichnung = fspBezeichnungen.get(javaName);
			if (bezeichnung == null)
				fspBezeichnungen.put(javaName, dto.Beschreibung);
		}
		logger.logLn("- Generiere Java-Code...");
		List<String> listJavaNames = fsps.stream().map(f -> fspJavaName.get(f).trim()).distinct().collect(Collectors.toList());
		for (int i = 0; i < listJavaNames.size(); i++) {
			String javaName = listJavaNames.get(i);
			DataStatkueFoerderschwerpunkt dto = fspDTOs.get(javaName);
			String sf = fspSchulformen.get(javaName);
			sb.append("\t/** Förderschwerpunkt - " + fspBezeichnungen.get(javaName) + " */" + System.lineSeparator());
			sb.append("\t" + javaName + "(new FoerderschwerpunktKatalogEintrag[] {" + System.lineSeparator());
			sb.append("\t\tnew FoerderschwerpunktKatalogEintrag(" + (i*1000) + ", \"" + dto.FSP + "\", \"" + fspBezeichnungen.get(javaName) + "\", ");
			if (sf == null) {
				sb.append("null");
			} else {
				sb.append("Arrays.asList(");
				sb.append(sf);
				sb.append(System.lineSeparator());
				sb.append("\t\t)");
			}
			sb.append(", " + dto.gueltigVon);
			sb.append(", " + dto.gueltigBis);
			sb.append(")");
			sb.append(System.lineSeparator());
			sb.append("\t})" + ((i < listJavaNames.size() - 1) ? "," : ";") + System.lineSeparator());
			sb.append(System.lineSeparator());
		}
	}
	
}
