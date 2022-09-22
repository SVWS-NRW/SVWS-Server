package de.nrw.schule.svws.app.coregen;

import java.util.List;
import java.util.stream.Collectors;

import de.nrw.schule.svws.csv.statkue.DataStatkueSchuelerVerkehrsprache;

/**
 * Der Core-Type-Updater für die Verkehrsprachen in den Familien 
 */
public class GenSchuelerVerkehrssprache extends CoreTypeUpdater {

	/**
	 * Erzeugt einen neuen Generator mit dem angebebenen logger für 
	 * den Core-Type Verkehrssprachen
	 */
	protected GenSchuelerVerkehrssprache() {
		super("Verkehrssprache", "schule/Verkehrssprache.java");
	}


	@Override
	public void generate(StringBuilder sb) {
		logger.logLn("- Sortiere DTOs...");
		List<DataStatkueSchuelerVerkehrsprache> sprachen = DataStatkueSchuelerVerkehrsprache.get().stream()
				.sorted((a,b) -> {
					int tmp = a.Kurztext.compareTo(b.Kurztext);
					if (tmp != 0)
						return tmp;
					return Integer.compare(a.ID, b.ID);
				})
				.collect(Collectors.toList());
		logger.logLn("- Generiere Java-Code für spezielle Kursarten...");
		for (int i = 0; i < sprachen.size(); i++) {
			DataStatkueSchuelerVerkehrsprache sprache = sprachen.get(i);
			sb.append("\t/** Sprache " + sprache.Langtext + " (" + sprache.Kurztext + ") */" + System.lineSeparator());
			sb.append("\t" + sprache.Kurztext.toUpperCase() + "(new VerkehrsspracheKatalogEintrag[]{" + System.lineSeparator());
			sb.append("\t\tnew VerkehrsspracheKatalogEintrag(" + sprache.ID + ", ");
			sb.append("\"" + sprache.Kurztext + "\", ");
			sb.append("\"" + sprache.Langtext + "\", ");
			sb.append("Arrays.asList(");
			if (sprache.Gesprochen_in != null) {
				String[] spr = sprache.Gesprochen_in.split(",");
				if ((spr.length > 0) && ((spr.length > 1) || !"".equals(spr[0].trim()))) {
					for (int j = 0; j < spr.length; j++) {
						if (j > 0)
							sb.append(", ");
						sb.append("\"" + spr[j].trim() + "\"");
					}
				}
			}
			sb.append("), ");
			if (sprache.gueltigVon == null)
				sb.append("null, ");
			else
				sb.append(sprache.gueltigVon + ", ");
			if (sprache.gueltigBis == null)
				sb.append("null)");
			else
				sb.append(sprache.gueltigBis + ")");
			sb.append(System.lineSeparator());
			sb.append("\t})");
			if (i + 1 < sprachen.size())
				sb.append(",");
			else
				sb.append(";");
			sb.append(System.lineSeparator());
			sb.append(System.lineSeparator());
		}
	}

}
