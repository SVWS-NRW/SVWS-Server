package de.nrw.schule.svws.core.types.schule;

import java.util.HashMap;

import de.nrw.schule.svws.core.data.schule.NationalitaetenKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die Nationalitäten.<br>
 * Die Daten basieren auf der Codeliste Staat aus der Staats- und Gebietssystematik des Statistischen Bundesamtes.<br>
 * Siehe auch:<ul>
 *   <li><a href="https://www.destatis.de/DE/Methoden/Klassifikationen/Staat-Gebietsystematik/Staatsangehoerigkeitsgebietsschluessel_pdf.pdf?__blob=publicationFile">
 *   Stand vom 1.2.2022 </a> </li>
 *   <li> Eine Übersicht bei xrepository.de findet sich unter der Kennung 
 *   "urn:de:bund:destatis:bevoelkerungsstatistik:schluessel:staat". </li>
 *   <li>Übersicht zum <a href="https://www.iso.org/obp/ui/#iso:pub:PUB500001:en"> ISO-Standard 3166</a></li>
 * </ul>
 */
public enum Nationalitaeten {

	/** Staatenlos (XXA) - benutzerdefinierter ISO-Code */
	XXA(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(88088065, "XXA", "XA", "997", "997", "(staatenlos)", "(staatenlos)", "(staatenlos)", "(staatenlos)", null, null)
	}),

	/** Ungeklärt (XXB) - benutzerdefinierter ISO-Code */
	XXB(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(88088066, "XXB", "XB", "998", "998", "(ungeklärt)", "(ungeklärt)", "(ungeklärt)", "(ungeklärt)", null, null)
	}),

	/** Ohne Angabe (XXC) - benutzerdefinierter ISO-Code */
	XXC(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(88088067, "XXC", "XC", "999", "999", "(ohne Angabe)", "(ohne Angabe)", "(ohne Angabe)", "(ohne Angabe)", null, null)
	}),

	/** Afghanistan (AFG) */
	AFG(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(65070071, "AFG", "AF", "004", "423", "Afghanistan", "Afghanistan", "die Islamische Republik Afghanistan", "afghanisch", null, null)
	}),

	/** Ägypten (EGY) */
	EGY(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(69071089, "EGY", "EG", "818", "287", "Ägypten", "Ägypten", "die Arabische Republik Ägypten", "ägyptisch", null, null)
	}),

	/** Albanien (ALB) */
	ALB(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(65076066, "ALB", "AL", "008", "121", "Albanien", "Albanien", "die Republik Albanien", "albanisch", null, null)
	}),

	/** Algerien (DZA) */
	DZA(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(68090065, "DZA", "DZ", "012", "221", "Algerien", "Algerien", "die Demokratische Volksrepublik Algerien", "algerisch", 1961, null)
	}),

	/** Andorra (AND) */
	AND(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(65078068, "AND", "AD", "020", "123", "Andorra", "Andorra", "das Fürstentum Andorra", "andorranisch", null, null)
	}),

	/** Angola (AGO) */
	AGO(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(65071079, "AGO", "AO", "024", "223", "Angola", "Angola", "die Republik Angola", "angolanisch", 1975, null)
	}),

	/** Antigua und Barbuda (ATG) */
	ATG(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(65084071, "ATG", "AG", "028", "320", "Antigua und Barbuda", "Antigua und Barbuda", "Antigua und Barbuda", "antiguanisch", 1981, null)
	}),

	/** Äquatorialguinea (GNQ) */
	GNQ(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(71078081, "GNQ", "GQ", "226", "274", "Äquatorialguinea", "Äquatorialguinea", "die Republik Äquatorialguinea", "äquatorialguineisch", 1968, null)
	}),

	/** Argentinien (ARG) */
	ARG(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(65082071, "ARG", "AR", "032", "323", "Argentinien", "Argentinien", "die Argentinische Republik", "argentinisch", null, null)
	}),

	/** Armenien (ARM) */
	ARM(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(65082077, "ARM", "AM", "051", "422", "Armenien", "Armenien", "die Republik Armenien", "armenisch", 1991, null)
	}),

	/** Aserbaidschan (AZE) */
	AZE(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(65090069, "AZE", "AZ", "031", "425", "Aserbaidschan", "Aserbaidschan", "die Republik Aserbaidschan", "aserbaidschanisch", 1991, null)
	}),

	/** Äthiopien (ETH) */
	ETH(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(69084072, "ETH", "ET", "231", "225", "Äthiopien", "Äthiopien", "die Demokratische Bundesrepublik Äthiopien", "äthiopisch", null, null)
	}),

	/** Australien (AUS) */
	AUS(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(65085083, "AUS", "AU", "036", "523", "Australien", "Australien", "Australien", "australisch", null, null)
	}),

	/** Bahamas (BHS) */
	BHS(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(66072083, "BHS", "BS", "044", "324", "Bahamas", "Bahamas", "das Commonwealth der Bahamas", "bahamaisch", 1972, null)
	}),

	/** Bahrain (BHR) */
	BHR(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(66072082, "BHR", "BH", "048", "424", "Bahrain", "Bahrain", "das Königreich Bahrain", "bahrainisch", 1971, null)
	}),

	/** Bangladesch (BGD) */
	BGD(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(66071068, "BGD", "BD", "050", "460", "Bangladesch", "Bangladesch", "die Volksrepublik Bangladesch", "bangladeschisch", 1970, null)
	}),

	/** Barbados (BRB) */
	BRB(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(66082066, "BRB", "BB", "052", "322", "Barbados", "Barbados", "Barbados", "barbadisch", 1966, null)
	}),

	/** Belarus (BLR) */
	BLR(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(66076082, "BLR", "BY", "112", "169", "Belarus", "Belarus", "die Republik Belarus", "belarussisch", 1991, null)
	}),

	/** Belgien (BEL) */
	BEL(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(66069076, "BEL", "BE", "056", "124", "Belgien", "Belgien", "das Königreich Belgien", "belgisch", null, null)
	}),

	/** Belize (BLZ) */
	BLZ(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(66076090, "BLZ", "BZ", "084", "330", "Belize", "Belize", "Belize", "belizisch", 1981, null)
	}),

	/** Benin (BEN) */
	BEN(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(66069078, "BEN", "BJ", "204", "229", "Benin", "Benin", "die Republik Benin", "beninisch", 1960, null)
	}),

	/** Bhutan (BTN) */
	BTN(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(66084078, "BTN", "BT", "064", "426", "Bhutan", "Bhutan", "das Königreich Bhutan", "bhutanisch", null, null)
	}),

	/** Plurinationaler Staat Bolivien (BOL) */
	BOL(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(66079076, "BOL", "BO", "068", "326", "Bolivien", "Plurinationaler Staat Bolivien", "der Plurinationale Staat Bolivien", "bolivianisch", null, null)
	}),

	/** Bosnien und Herzegowina (BIH) */
	BIH(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(66073072, "BIH", "BA", "070", "122", "Bosnien und Herzegowina", "Bosnien und Herzegowina", "Bosnien und Herzegowina", "bosnisch-herzegowinisch", 1991, null)
	}),

	/** Botsuana (BWA) */
	BWA(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(66087065, "BWA", "BW", "072", "227", "Botsuana", "Botsuana", "die Republik Botsuana", "botsuanisch", 1966, null)
	}),

	/** Brasilien (BRA) */
	BRA(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(66082065, "BRA", "BR", "076", "327", "Brasilien", "Brasilien", "die Föderative Republik Brasilien", "brasilianisch", null, null)
	}),

	/** Brunei Darussalam (BRN) */
	BRN(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(66082078, "BRN", "BN", "096", "429", "Brunei Darussalam", "Brunei Darussalam", "Brunei Darussalam", "bruneiisch", 1983, null)
	}),

	/** Bulgarien (BGR) */
	BGR(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(66071082, "BGR", "BG", "100", "125", "Bulgarien", "Bulgarien", "die Republik Bulgarien", "bulgarisch", null, null)
	}),

	/** Burkina Faso (BFA) */
	BFA(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(66070065, "BFA", "BF", "854", "258", "Burkina Faso", "Burkina Faso", "Burkina Faso", "burkinisch", 1960, null)
	}),

	/** Burundi (BDI) */
	BDI(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(66068073, "BDI", "BI", "108", "291", "Burundi", "Burundi", "die Republik Burundi", "burundisch", 1961, null)
	}),

	/** Cabo Verde (CPV) */
	CPV(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(67080086, "CPV", "CV", "132", "242", "Cabo Verde", "Cabo Verde", "die Republik Cabo Verde", "cabo-verdisch", 1974, null)
	}),

	/** Chile (CHL) */
	CHL(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(67072076, "CHL", "CL", "152", "332", "Chile", "Chile", "die Republik Chile", "chilenisch", null, null)
	}),

	/** China (CHN) */
	CHN(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(67072078, "CHN", "CN", "156", "479", "China", "China", "die Volksrepublik China", "chinesisch", null, null)
	}),

	/** Cookinseln (COK) */
	COK(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(67079075, "COK", "CK", "184", "527", "Cookinseln", "Cookinseln", "die Cookinseln", "neuseeländisch", null, null)
	}),

	/** Costa Rica (CRI) */
	CRI(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(67082073, "CRI", "CR", "188", "334", "Costa Rica", "Costa Rica", "die Republik Costa Rica", "costa-ricanisch", null, null)
	}),

	/** Côte d’Ivoire (CIV) */
	CIV(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(67073086, "CIV", "CI", "384", "231", "Côte d’Ivoire", "Côte d’Ivoire", "die Republik Côte d’Ivoire", "ivorisch", 1960, null)
	}),

	/** Dänemark (DNK) */
	DNK(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(68078075, "DNK", "DK", "208", "126", "Dänemark", "Dänemark", "das Königreich Dänemark", "dänisch", null, null)
	}),

	/** Deutschland (DEU) */
	DEU(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(68069085, "DEU", "DE", "276", "000", "Deutschland", "Deutschland", "die Bundesrepublik Deutschland", "deutsch", 1948, null)
	}),

	/** Dominica (DMA) */
	DMA(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(68077065, "DMA", "DM", "212", "333", "Dominica", "Dominica", "das Commonwealth Dominica", "dominicanisch", 1978, null)
	}),

	/** Dominikanische Republik (DOM) */
	DOM(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(68079077, "DOM", "DO", "214", "335", "Dominikanische Republik", "Dominikanische Republik", "die Dominikanische Republik", "dominikanisch", null, null)
	}),

	/** Dschibuti (DJI) */
	DJI(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(68074073, "DJI", "DJ", "262", "230", "Dschibuti", "Dschibuti", "die Republik Dschibuti", "dschibutisch", 1976, null)
	}),

	/** Ecuador (ECU) */
	ECU(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(69067085, "ECU", "EC", "218", "336", "Ecuador", "Ecuador", "die Republik Ecuador", "ecuadorianisch", null, null)
	}),

	/** El Salvador (SLV) */
	SLV(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(83076086, "SLV", "SV", "222", "337", "El Salvador", "El Salvador", "die Republik El Salvador", "salvadorianisch", null, null)
	}),

	/** Eritrea (ERI) */
	ERI(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(69082073, "ERI", "ER", "232", "224", "Eritrea", "Eritrea", "der Staat Eritrea", "eritreisch", 1992, null)
	}),

	/** Estland (EST) */
	EST(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(69083084, "EST", "EE", "233", "127", "Estland", "Estland", "die Republik Estland", "estnisch", 1991, null)
	}),

	/** Eswatini (SWZ) */
	SWZ(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(83087090, "SWZ", "SZ", "748", "281", "Eswatini", "Eswatini", "das Königreich Eswatini", "eswatinisch", 1968, null)
	}),

	/** Fidschi (FJI) */
	FJI(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(70074073, "FJI", "FJ", "242", "526", "Fidschi", "Fidschi", "die Republik Fidschi", "fidschianisch", 1970, null)
	}),

	/** Finnland (FIN) */
	FIN(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(70073078, "FIN", "FI", "246", "128", "Finnland", "Finnland", "die Republik Finnland", "finnisch", null, null)
	}),

	/** Frankreich (FRA) */
	FRA(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(70082065, "FRA", "FR", "250", "129", "Frankreich", "Frankreich", "die Französische Republik", "französisch", null, null)
	}),

	/** Gabun (GAB) */
	GAB(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(71065066, "GAB", "GA", "266", "236", "Gabun", "Gabun", "die Gabunische Republik", "gabunisch", 1960, null)
	}),

	/** Gambia (GMB) */
	GMB(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(71077066, "GMB", "GM", "270", "237", "Gambia", "Gambia", "die Republik Gambia", "gambisch", 1964, null)
	}),

	/** Georgien (GEO) */
	GEO(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(71069079, "GEO", "GE", "268", "430", "Georgien", "Georgien", "Georgien", "georgisch", 1990, null)
	}),

	/** Ghana (GHA) */
	GHA(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(71072065, "GHA", "GH", "288", "238", "Ghana", "Ghana", "die Republik Ghana", "ghanaisch", 1956, null)
	}),

	/** Grenada (GRD) */
	GRD(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(71082068, "GRD", "GD", "308", "340", "Grenada", "Grenada", "Grenada", "grenadisch", 1973, null)
	}),

	/** Griechenland (GRC) */
	GRC(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(71082067, "GRC", "GR", "300", "134", "Griechenland", "Griechenland", "die Hellenische Republik", "griechisch", null, null)
	}),

	/** Guatemala (GTM) */
	GTM(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(71084077, "GTM", "GT", "320", "345", "Guatemala", "Guatemala", "die Republik Guatemala", "guatemaltekisch", null, null)
	}),

	/** Guinea (GIN) */
	GIN(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(71073078, "GIN", "GN", "324", "261", "Guinea", "Guinea", "die Republik Guinea", "guineisch", 1958, null)
	}),

	/** Guinea-Bissau (GNB) */
	GNB(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(71078066, "GNB", "GW", "624", "259", "Guinea-Bissau", "Guinea-Bissau", "die Republik Guinea-Bissau", "guinea-bissauisch", 1973, null)
	}),

	/** Guyana (GUY) */
	GUY(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(71085089, "GUY", "GY", "328", "328", "Guyana", "Guyana", "die Kooperative Republik Guyana", "guyanisch", 1965, null)
	}),

	/** Haiti (HTI) */
	HTI(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(72084073, "HTI", "HT", "332", "346", "Haiti", "Haiti", "die Republik Haiti", "haitianisch", null, null)
	}),

	/** Honduras (HND) */
	HND(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(72078068, "HND", "HN", "340", "347", "Honduras", "Honduras", "die Republik Honduras", "honduranisch", null, null)
	}),

	/** Hongkong (HKG) */
	HKG(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(72075071, "HKG", "HK", "344", "411", "Hongkong", "Hongkong", "die Sonderverwaltungsregion Hongkong", "chinesisch (Hongkong)", null, null)
	}),

	/** Indien (IND) */
	IND(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(73078068, "IND", "IN", "356", "436", "Indien", "Indien", "die Republik Indien", "indisch", 1947, null)
	}),

	/** Indonesien (IDN) */
	IDN(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(73068078, "IDN", "ID", "360", "437", "Indonesien", "Indonesien", "die Republik Indonesien", "indonesisch", 1945, null)
	}),

	/** Irak (IRQ) */
	IRQ(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(73082081, "IRQ", "IQ", "368", "438", "Irak", "Irak", "die Republik Irak", "irakisch", null, null)
	}),

	/** Islamische Republik Iran (IRN) */
	IRN(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(73082078, "IRN", "IR", "364", "439", "Iran", "Islamische Republik Iran", "die Islamische Republik Iran", "iranisch", null, null)
	}),

	/** Irland (IRL) */
	IRL(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(73082076, "IRL", "IE", "372", "135", "Irland", "Irland", "Irland", "irisch", null, null)
	}),

	/** Island (ISL) */
	ISL(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(73083076, "ISL", "IS", "352", "136", "Island", "Island", "die Republik Island", "isländisch", null, null)
	}),

	/** Israel (ISR) */
	ISR(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(73083082, "ISR", "IL", "376", "441", "Israel", "Israel", "der Staat Israel", "israelisch", 1947, null)
	}),

	/** Italien (ITA) */
	ITA(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(73084065, "ITA", "IT", "380", "137", "Italien", "Italien", "die Italienische Republik", "italienisch", null, null)
	}),

	/** Jamaika (JAM) */
	JAM(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(74065077, "JAM", "JM", "388", "355", "Jamaika", "Jamaika", "Jamaika", "jamaikanisch", 1962, null)
	}),

	/** Japan (JPN) */
	JPN(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(74080078, "JPN", "JP", "392", "442", "Japan", "Japan", "Japan", "japanisch", null, null)
	}),

	/** Jemen (YEM) */
	YEM(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(89069077, "YEM", "YE", "887", "421", "Jemen", "Jemen", "die Republik Jemen", "jemenitisch", 1967, null)
	}),

	/** Jordanien (JOR) */
	JOR(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(74079082, "JOR", "JO", "400", "445", "Jordanien", "Jordanien", "das Haschemitische Königreich Jordanien", "jordanisch", 1945, null)
	}),

	/** Jugoslawien (YUG) */
	YUG(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(89085071, "YUG", "YU", "891", "120", "Jugoslawien", "Jugoslawien", "die Sozialistische Föderative Republik Jugoslawien", "jugoslawisch", 1945, 1991),
		new NationalitaetenKatalogEintrag(1089085071, "YUG", "YU", "891", "138", "Jugoslawien", "Jugoslawien", "die Bundesrepublik Jugoslawien", "jugoslawisch", 1992, 2002)
	}),

	/** Kambodscha (KHM) */
	KHM(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(75072077, "KHM", "KH", "116", "446", "Kambodscha", "Kambodscha", "das Königreich Kambodscha", "kambodschanisch", 1953, null)
	}),

	/** Kamerun (CMR) */
	CMR(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(67077082, "CMR", "CM", "120", "262", "Kamerun", "Kamerun", "die Republik Kamerun", "kamerunisch", 1959, null)
	}),

	/** Kanada (CAN) */
	CAN(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(67065078, "CAN", "CA", "124", "348", "Kanada", "Kanada", "Kanada", "kanadisch", null, null)
	}),

	/** Kasachstan (KAZ) */
	KAZ(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(75065090, "KAZ", "KZ", "398", "444", "Kasachstan", "Kasachstan", "die Republik Kasachstan", "kasachisch", 1991, null)
	}),

	/** Katar (QAT) */
	QAT(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(81065084, "QAT", "QA", "634", "447", "Katar", "Katar", "der Staat Katar", "katarisch", 1971, null)
	}),

	/** Kenia (KEN) */
	KEN(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(75069078, "KEN", "KE", "404", "243", "Kenia", "Kenia", "die Republik Kenia", "kenianisch", 1963, null)
	}),

	/** Kirgisistan (KGZ) */
	KGZ(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(75071090, "KGZ", "KG", "417", "450", "Kirgisistan", "Kirgisistan", "die Kirgisische Republik", "kirgisisch", 1991, null)
	}),

	/** Kiribati (KIR) */
	KIR(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(75073082, "KIR", "KI", "296", "530", "Kiribati", "Kiribati", "die Republik Kiribati", "kiribatisch", 1978, null)
	}),

	/** Kolumbien (COL) */
	COL(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(67079076, "COL", "CO", "170", "349", "Kolumbien", "Kolumbien", "die Republik Kolumbien", "kolumbianisch", null, null)
	}),

	/** Komoren (COM) */
	COM(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(67079077, "COM", "KM", "174", "244", "Komoren", "Komoren", "die Union der Komoren", "komorisch", 1974, null)
	}),

	/** Kongo (COG) */
	COG(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(67079071, "COG", "CG", "178", "245", "Kongo", "Kongo", "die Republik Kongo", "kongolesisch", 1960, null)
	}),

	/** Demokratische Republik Kongo (COD) */
	COD(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(67079068, "COD", "CD", "180", "246", "Kongo, Demokratische Republik", "Demokratische Republik Kongo", "die Demokratische Republik Kongo", "der Demokratischen Republik Kongo", 1959, null)
	}),

	/** Demokratische Volksrepublik Korea (PRK) */
	PRK(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(80082075, "PRK", "KP", "408", "434", "Korea, Demokratische Volksrepublik", "Demokratische Volksrepublik Korea", "die Demokratische Volksrepublik Korea", "der Demokratischen Volksrepublik Korea", 1948, null)
	}),

	/** Republik Korea (KOR) */
	KOR(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(75079082, "KOR", "KR", "410", "467", "Korea, Republik", "Republik Korea", "die Republik Korea", "der Republik Korea", 1948, null)
	}),

	/** Kosovo (XXK) */
	XXK(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(88088075, "XXK", "XK", null, "150", "Kosovo", "Kosovo", "die Republik Kosovo", "kosovarisch", 2007, null)
	}),

	/** Kroatien (HRV) */
	HRV(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(72082086, "HRV", "HR", "191", "130", "Kroatien", "Kroatien", "die Republik Kroatien", "kroatisch", 1990, null)
	}),

	/** Kuba (CUB) */
	CUB(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(67085066, "CUB", "CU", "192", "351", "Kuba", "Kuba", "die Republik Kuba", "kubanisch", null, null)
	}),

	/** Kuwait (KWT) */
	KWT(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(75087084, "KWT", "KW", "414", "448", "Kuwait", "Kuwait", "der Staat Kuwait", "kuwaitisch", 1960, null)
	}),

	/** Demokratische Volksrepublik Laos (LAO) */
	LAO(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(76065079, "LAO", "LA", "418", "449", "Laos", "Demokratische Volksrepublik Laos", "die Demokratische Volksrepublik Laos", "laotisch", 1953, null)
	}),

	/** Lesotho (LSO) */
	LSO(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(76083079, "LSO", "LS", "426", "226", "Lesotho", "Lesotho", "das Königreich Lesotho", "lesothisch", 1966, null)
	}),

	/** Lettland (LVA) */
	LVA(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(76086065, "LVA", "LV", "428", "139", "Lettland", "Lettland", "die Republik Lettland", "lettisch", 1991, null)
	}),

	/** Libanon (LBN) */
	LBN(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(76066078, "LBN", "LB", "422", "451", "Libanon", "Libanon", "die Libanesische Republik", "libanesisch", null, null)
	}),

	/** Liberia (LBR) */
	LBR(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(76066082, "LBR", "LR", "430", "247", "Liberia", "Liberia", "die Republik Liberia", "liberianisch", null, null)
	}),

	/** Libyen (LBY) */
	LBY(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(76066089, "LBY", "LY", "434", "248", "Libyen", "Libyen", "der Staat Libyen", "libysch", 1951, null)
	}),

	/** Liechtenstein (LIE) */
	LIE(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(76073069, "LIE", "LI", "438", "141", "Liechtenstein", "Liechtenstein", "das Fürstentum Liechtenstein", "liechtensteinisch", null, null)
	}),

	/** Litauen (LTU) */
	LTU(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(76084085, "LTU", "LT", "440", "142", "Litauen", "Litauen", "die Republik Litauen", "litauisch", 1989, null)
	}),

	/** Luxemburg (LUX) */
	LUX(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(76085088, "LUX", "LU", "442", "143", "Luxemburg", "Luxemburg", "das Großherzogtum Luxemburg", "luxemburgisch", null, null)
	}),

	/** Macau (MAC) */
	MAC(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(77065067, "MAC", "MO", "446", "412", "Macau", "Macau", "die Sonderverwaltungsregion Macau", "chinesisch (Macau)", null, null)
	}),

	/** Madagaskar (MDG) */
	MDG(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(77068071, "MDG", "MG", "450", "249", "Madagaskar", "Madagaskar", "die Republik Madagaskar", "madagassisch", 1959, null)
	}),

	/** Malawi (MWI) */
	MWI(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(77087073, "MWI", "MW", "454", "256", "Malawi", "Malawi", "die Republik Malawi", "malawisch", 1963, null)
	}),

	/** Malaysia (MYS) */
	MYS(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(77089083, "MYS", "MY", "458", "482", "Malaysia", "Malaysia", "Malaysia", "malaysisch", 1957, null)
	}),

	/** Malediven (MDV) */
	MDV(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(77068086, "MDV", "MV", "462", "454", "Malediven", "Malediven", "die Republik Malediven", "maledivisch", 1964, null)
	}),

	/** Mali (MLI) */
	MLI(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(77076073, "MLI", "ML", "466", "251", "Mali", "Mali", "die Republik Mali", "malisch", 1960, null)
	}),

	/** Malta (MLT) */
	MLT(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(77076084, "MLT", "MT", "470", "145", "Malta", "Malta", "die Republik Malta", "maltesisch", 1964, null)
	}),

	/** Marokko (MAR) */
	MAR(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(77065082, "MAR", "MA", "504", "252", "Marokko", "Marokko", "das Königreich Marokko", "marokkanisch", 1955, null)
	}),

	/** Marshallinseln (MHL) */
	MHL(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(77072076, "MHL", "MH", "584", "544", "Marshallinseln", "Marshallinseln", "die Republik Marshallinseln", "marshallisch", 1990, null)
	}),

	/** Mauretanien (MRT) */
	MRT(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(77082084, "MRT", "MR", "478", "239", "Mauretanien", "Mauretanien", "die Islamische Republik Mauretanien", "mauretanisch", 1960, null)
	}),

	/** Mauritius (MUS) */
	MUS(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(77085083, "MUS", "MU", "480", "253", "Mauritius", "Mauritius", "die Republik Mauritius", "mauritisch", 1967, null)
	}),

	/** Mexiko (MEX) */
	MEX(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(77069088, "MEX", "MX", "484", "353", "Mexiko", "Mexiko", "die Vereinigten Mexikanischen Staaten", "mexikanisch", null, null)
	}),

	/** Föderierte Staaten von Mikronesien (FSM) */
	FSM(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(70083077, "FSM", "FM", "583", "545", "Mikronesien", "Föderierte Staaten von Mikronesien", "die Föderierten Staaten von Mikronesien", "mikronesisch", 1990, null)
	}),

	/** Republik Moldau (MDA) */
	MDA(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(77068065, "MDA", "MD", "498", "146", "Moldau", "Republik Moldau", "die Republik Moldau", "moldauisch", 1991, null)
	}),

	/** Monaco (MCO) */
	MCO(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(77067079, "MCO", "MC", "492", "147", "Monaco", "Monaco", "das Fürstentum Monaco", "monegassisch", null, null)
	}),

	/** Mongolei (MNG) */
	MNG(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(77078071, "MNG", "MN", "496", "457", "Mongolei", "Mongolei", "die Mongolei", "mongolisch", null, null)
	}),

	/** Montenegro (MNE) */
	MNE(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(77078069, "MNE", "ME", "499", "140", "Montenegro", "Montenegro", "Montenegro", "montenegrinisch", 2005, null)
	}),

	/** Mosambik (MOZ) */
	MOZ(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(77079090, "MOZ", "MZ", "508", "254", "Mosambik", "Mosambik", "die Republik Mosambik", "mosambikanisch", 1974, null)
	}),

	/** Myanmar (MMR) */
	MMR(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(77077082, "MMR", "MM", "104", "427", "Myanmar", "Myanmar", "die Republik der Union Myanmar", "myanmarisch", 1947, null)
	}),

	/** Namibia (NAM) */
	NAM(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(78065077, "NAM", "NA", "516", "267", "Namibia", "Namibia", "die Republik Namibia", "namibisch", 1989, null)
	}),

	/** Nauru (NRU) */
	NRU(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(78082085, "NRU", "NR", "520", "531", "Nauru", "Nauru", "die Republik Nauru", "nauruisch", 1967, null)
	}),

	/** Nepal (NPL) */
	NPL(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(78080076, "NPL", "NP", "524", "458", "Nepal", "Nepal", "Nepal", "nepalesisch", null, null)
	}),

	/** Neuseeland (NZL) */
	NZL(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(78090076, "NZL", "NZ", "554", "536", "Neuseeland", "Neuseeland", "Neuseeland", "neuseeländisch", null, null)
	}),

	/** Nicaragua (NIC) */
	NIC(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(78073067, "NIC", "NI", "558", "354", "Nicaragua", "Nicaragua", "die Republik Nicaragua", "nicaraguanisch", null, null)
	}),

	/** Niederlande (NLD) */
	NLD(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(78076068, "NLD", "NL", "528", "148", "Niederlande", "Niederlande", "das Königreich der Niederlande", "niederländisch", null, null)
	}),

	/** Niger (NER) */
	NER(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(78069082, "NER", "NE", "562", "255", "Niger", "Niger", "die Republik Niger", "nigrisch", 1960, null)
	}),

	/** Nigeria (NGA) */
	NGA(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(78071065, "NGA", "NG", "566", "232", "Nigeria", "Nigeria", "die Bundesrepublik Nigeria", "nigerianisch", 1960, null)
	}),

	/** Niue (NIU) */
	NIU(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(78073085, "NIU", "NU", "570", "533", "Niue", "Niue", "Niue", "neuseeländisch", null, null)
	}),

	/** Nordmazedonien (MKD) */
	MKD(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(77075068, "MKD", "MK", "807", "144", "Nordmazedonien", "Nordmazedonien", "die Republik Nordmazedonien", "mazedonisch/Bürger der Republik Nordmazedonien", 1991, null)
	}),

	/** Norwegen (NOR) */
	NOR(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(78079082, "NOR", "NO", "578", "149", "Norwegen", "Norwegen", "das Königreich Norwegen", "norwegisch", null, null)
	}),

	/** Oman (OMN) */
	OMN(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(79077078, "OMN", "OM", "512", "456", "Oman", "Oman", "das Sultanat Oman", "omanisch", null, null)
	}),

	/** Österreich (AUT) */
	AUT(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(65085084, "AUT", "AT", "040", "151", "Österreich", "Österreich", "die Republik Österreich", "österreichisch", null, null)
	}),

	/** Pakistan (PAK) */
	PAK(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(80065075, "PAK", "PK", "586", "461", "Pakistan", "Pakistan", "die Islamische Republik Pakistan", "pakistanisch", 1947, null)
	}),

	/** Palau (PLW) */
	PLW(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(80076087, "PLW", "PW", "585", "537", "Palau", "Palau", "die Republik Palau", "palauisch", 1994, null)
	}),

	/** Panama (PAN) */
	PAN(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(80065078, "PAN", "PA", "591", "357", "Panama", "Panama", "die Republik Panama", "panamaisch", null, null)
	}),

	/** Papua-Neuguinea (PNG) */
	PNG(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(80078071, "PNG", "PG", "598", "538", "Papua-Neuguinea", "Papua-Neuguinea", "der Unabhängige Staat Papua-Neuguinea", "papua-neuguineisch", 1975, null)
	}),

	/** Paraguay (PRY) */
	PRY(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(80082089, "PRY", "PY", "600", "359", "Paraguay", "Paraguay", "die Republik Paraguay", "paraguayisch", null, null)
	}),

	/** Peru (PER) */
	PER(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(80069082, "PER", "PE", "604", "361", "Peru", "Peru", "die Republik Peru", "peruanisch", null, null)
	}),

	/** Philippinen (PHL) */
	PHL(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(80072076, "PHL", "PH", "608", "462", "Philippinen", "Philippinen", "die Republik der Philippinen", "philippinisch", null, null)
	}),

	/** Polen (POL) */
	POL(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(80079076, "POL", "PL", "616", "152", "Polen", "Polen", "die Republik Polen", "polnisch", null, null)
	}),

	/** Portugal (PRT) */
	PRT(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(80082084, "PRT", "PT", "620", "153", "Portugal", "Portugal", "die Portugiesische Republik", "portugiesisch", null, null)
	}),

	/** Ruanda (RWA) */
	RWA(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(82087065, "RWA", "RW", "646", "265", "Ruanda", "Ruanda", "die Republik Ruanda", "ruandisch", 1961, null)
	}),

	/** Rumänien (ROU) */
	ROU(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(82079085, "ROU", "RO", "642", "154", "Rumänien", "Rumänien", "Rumänien", "rumänisch", null, null)
	}),

	/** Russische Föderation (RUS) */
	RUS(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(82085083, "RUS", "RU", "643", "160", "Russische Föderation", "Russische Föderation", "die Russische Föderation", "russisch", 1991, null)
	}),

	/** Salomonen (SLB) */
	SLB(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(83076066, "SLB", "SB", "090", "524", "Salomonen", "Salomonen", "die Salomonen", "salomonisch", 1977, null)
	}),

	/** Sambia (ZMB) */
	ZMB(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(90077066, "ZMB", "ZM", "894", "257", "Sambia", "Sambia", "die Republik Sambia", "sambisch", 1964, null)
	}),

	/** Samoa (WSM) */
	WSM(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(87083077, "WSM", "WS", "882", "543", "Samoa", "Samoa", "der Unabhängige Staat Samoa", "samoanisch", 1961, null)
	}),

	/** San Marino (SMR) */
	SMR(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(83077082, "SMR", "SM", "674", "156", "San Marino", "San Marino", "die Republik San Marino", "san-marinesisch", null, null)
	}),

	/** São Tomé und Príncipe (STP) */
	STP(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(83084080, "STP", "ST", "678", "268", "São Tomé und Príncipe", "São Tomé und Príncipe", "die Demokratische Republik São Tomé und Príncipe", "são-toméisch", 1974, null)
	}),

	/** Saudi-Arabien (SAU) */
	SAU(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(83065085, "SAU", "SA", "682", "472", "Saudi-Arabien", "Saudi-Arabien", "das Königreich Saudi-Arabien", "saudi-arabisch", null, null)
	}),

	/** Schweden (SWE) */
	SWE(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(83087069, "SWE", "SE", "752", "157", "Schweden", "Schweden", "das Königreich Schweden", "schwedisch", null, null)
	}),

	/** Schweiz (CHE) */
	CHE(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(67072069, "CHE", "CH", "756", "158", "Schweiz", "Schweiz", "die Schweizerische Eidgenossenschaft", "schweizerisch", null, null)
	}),

	/** Senegal (SEN) */
	SEN(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(83069078, "SEN", "SN", "686", "269", "Senegal", "Senegal", "die Republik Senegal", "senegalesisch", 1960, null)
	}),

	/** Serbien (SRB) */
	SRB(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(83082066, "SRB", "RS", "688", "133", "Serbien", "Serbien (einschließlich Kosovo)", "die Republik Serbien", "serbisch", 2005, 2006),
		new NationalitaetenKatalogEintrag(1083082066, "SRB", "RS", "688", "170", "Serbien", "Serbien", "die Republik Serbien", "serbisch", 2007, null)
	}),

	/** Serbien und Montenegro (SCG) */
	SCG(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(83067071, "SCG", "CS", "891", "132", "Serbien und Montenegro", "Serbien und Montenegro", "Serbien und Montenegro", "von Serbien und Montenegro", 2002, 2005),
	}),

	/** Seychellen (SYC) */
	SYC(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(83089067, "SYC", "SC", "690", "271", "Seychellen", "Seychellen", "die Republik Seychellen", "seychellisch", 1975, null)
	}),

	/** Sierra Leone (SLE) */
	SLE(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(83076069, "SLE", "SL", "694", "272", "Sierra Leone", "Sierra Leone", "die Republik Sierra Leone", "sierra-leonisch", 1960, null)
	}),

	/** Simbabwe (ZWE) */
	ZWE(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(90087069, "ZWE", "ZW", "716", "233", "Simbabwe", "Simbabwe", "die Republik Simbabwe", "simbabwisch", 1979, null)
	}),

	/** Singapur (SGP) */
	SGP(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(83071080, "SGP", "SG", "702", "474", "Singapur", "Singapur", "die Republik Singapur", "singapurisch", 1979, null)
	}),

	/** Slowakei (SVK) */
	SVK(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(83086075, "SVK", "SK", "703", "155", "Slowakei", "Slowakei", "die Slowakische Republik", "slowakisch", 1992, null)
	}),

	/** Slowenien (SVN) */
	SVN(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(83086078, "SVN", "SI", "705", "131", "Slowenien", "Slowenien", "die Republik Slowenien", "slowenisch", 1990, null)
	}),

	/** Somalia (SOM) */
	SOM(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(83079077, "SOM", "SO", "706", "273", "Somalia", "Somalia", "die Bundesrepublik Somalia", "somalisch", 1959, null)
	}),

	/** Sowjetunion (SUN) */
	SUN(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(83085078, "SUN", "SU", "810", "159", "Sowjetunion", "Sowjetunion", "die Union der Sozialistischen Sowjetrepubliken", "sowjetisch", null, 1991)
	}),

	/** Spanien (ESP) */
	ESP(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(69083080, "ESP", "ES", "724", "161", "Spanien", "Spanien", "das Königreich Spanien", "spanisch", null, null)
	}),

	/** Sri Lanka (LKA) */
	LKA(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(76075065, "LKA", "LK", "144", "431", "Sri Lanka", "Sri Lanka", "die Demokratische Sozialistische Republik Sri Lanka", "sri-lankisch", 1947, null)
	}),

	/** St. Kitts und Nevis (KNA) */
	KNA(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(75078065, "KNA", "KN", "659", "370", "St. Kitts und Nevis", "St. Kitts und Nevis", "die Föderation St. Kitts und Nevis", "von St. Kitts und Nevis", 1983, null)
	}),

	/** St. Lucia (LCA) */
	LCA(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(76067065, "LCA", "LC", "662", "366", "St. Lucia", "St. Lucia", "St. Lucia", "lucianisch", 1978, null)
	}),

	/** St. Vincent und die Grenadinen (VCT) */
	VCT(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(86067084, "VCT", "VC", "670", "369", "St. Vincent und die Grenadinen", "St. Vincent und die Grenadinen", "St. Vincent und die Grenadinen", "vincentisch", 1979, null)
	}),

	/** Südafrika (ZAF) */
	ZAF(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(90065070, "ZAF", "ZA", "710", "263", "Südafrika", "Südafrika", "die Republik Südafrika", "südafrikanisch", null, null)
	}),

	/** Sudan (SDN) */
	SDN(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(83068078, "SDN", "SD", "729", "276", "Sudan", "Sudan (einschließlich Südsudan)", "die Republik Sudan", "sudanesisch", 1955, 2010),
		new NationalitaetenKatalogEintrag(1083068078, "SDN", "SD", "729", "277", "Sudan", "Sudan", "die Republik Sudan", "sudanesisch", 2011, null)
	}),

	/** Südsudan (SSD) */
	SSD(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(83083068, "SSD", "SS", "728", "278", "Südsudan", "Südsudan", "die Republik Südsudan", "südsudanesisch", 2010, null)
	}),

	/** Suriname (SUR) */
	SUR(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(83085082, "SUR", "SR", "740", "364", "Suriname", "Suriname", "die Republik Suriname", "surinamisch", 1975, null)
	}),

	/** Syrien (SYR) */
	SYR(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(83089082, "SYR", "SY", "760", "475", "Syrien", "Arabische Republik Syrien", "die Arabische Republik Syrien", "syrisch", 1945, null)
	}),

	/** Tadschikistan (TJK) */
	TJK(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(84074075, "TJK", "TJ", "762", "470", "Tadschikistan", "Tadschikistan", "die Republik Tadschikistan", "tadschikisch", 1991, null)
	}),

	/** Taiwan (TWN) */
	TWN(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(84086078, "TWN", "TW", "465", "158", "Taiwan", "Taiwan", "Taiwan", "taiwanisch", null, null)
	}),

	/** Tansania (TZA) */
	TZA(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(84090065, "TZA", "TZ", "834", "282", "Tansania", "Vereinigte Republik Tansania", "die Vereinigte Republik Tansania", "tansanisch", null, null)
	}),

	/** Thailand (THA) */
	THA(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(84072065, "THA", "TH", "764", "476", "Thailand", "Thailand", "das Königreich Thailand", "thailändisch", null, null)
	}),

	/** Timor-Leste (TLS) */
	TLS(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(84076083, "TLS", "TL", "626", "483", "Timor-Leste", "Timor-Leste", "die Demokratische Republik Timor-Leste", "von Timor-Leste", 2001, null)
	}),

	/** Togo (TGO) */
	TGO(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(84071079, "TGO", "TG", "768", "283", "Togo", "Togo", "die Republik Togo", "togoisch", 1959, null)
	}),

	/** Tonga (TON) */
	TON(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(84079078, "TON", "TO", "776", "541", "Tonga", "Tonga", "das Königreich Tonga", "tongaisch", 1969, null)
	}),

	/** Trinidad und Tobago (TTO) */
	TTO(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(84084079, "TTO", "TT", "780", "371", "Trinidad und Tobago", "Trinidad und Tobago", "die Republik Trinidad und Tobago", "von Trinidad und Tobago", 1962, null)
	}),

	/** Tschad (TCD) */
	TCD(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(84067068, "TCD", "TD", "148", "284", "Tschad", "Tschad", "die Republik Tschad", "tschadisch", 1960, null)
	}),

	/** Tschechien (CZE) */
	CZE(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(67090069, "CZE", "CZ", "203", "164", "Tschechien", "Tschechien", "die Tschechische Republik", "tschechisch", 1992, null)
	}),

	/** Tschechoslowakei (CSK) */
	CSK(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(67083075, "CSK", "CS", "200", "162", "Tschechoslowakei", "Tschechoslowakei", "die Tschechoslowakische Sozialistische Republik", "tschechoslowakisch", null, 1992)
	}),

	/** Tunesien (TUN) */
	TUN(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(84085078, "TUN", "TN", "788", "285", "Tunesien", "Tunesien", "die Tunesische Republik", "tunesisch", 1955, null)
	}),

	/** Türkei (TUR) */
	TUR(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(84085082, "TUR", "TR", "792", "163", "Türkei", "Türkei", "die Republik Türkei", "türkisch", null, null)
	}),

	/** Turkmenistan (TKM) */
	TKM(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(84075077, "TKM", "TM", "795", "471", "Turkmenistan", "Turkmenistan", "Turkmenistan", "turkmenisch", 1991, null)
	}),

	/** Tuvalu (TUV) */
	TUV(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(84085086, "TUV", "TV", "798", "540", "Tuvalu", "Tuvalu", "Tuvalu", "tuvaluisch", 1978, null)
	}),

	/** Uganda (UGA) */
	UGA(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(85071065, "UGA", "UG", "800", "286", "Uganda", "Uganda", "die Republik Uganda", "ugandisch", 1962, null)
	}),

	/** Ukraine (UKR) */
	UKR(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(85075082, "UKR", "UA", "804", "166", "Ukraine", "Ukraine", "die Ukraine", "ukrainisch", 1991, null)
	}),

	/** Ungarn (HUN) */
	HUN(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(72085078, "HUN", "HU", "348", "165", "Ungarn", "Ungarn", "Ungarn", "ungarisch", null, null)
	}),

	/** Uruguay (URY) */
	URY(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(85082089, "URY", "UY", "858", "365", "Uruguay", "Uruguay", "die Republik Östlich des Uruguay", "uruguayisch", null, null)
	}),

	/** Usbekistan (UZB) */
	UZB(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(85090066, "UZB", "UZ", "860", "477", "Usbekistan", "Usbekistan", "die Republik Usbekistan", "usbekisch", 1991, null)
	}),

	/** Vanuatu (VUT) */
	VUT(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(86085084, "VUT", "VU", "548", "532", "Vanuatu", "Vanuatu", "die Republik Vanuatu", "vanuatuisch", 1979, null)
	}),

	/** Vatikanstadt (VAT) */
	VAT(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(86065084, "VAT", "VA", "336", "167", "Vatikanstadt", "Vatikanstadt", "der Staat Vatikanstadt", "vatikanisch", null, null)
	}),

	/** Venezuela (VEN) */
	VEN(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(86069078, "VEN", "VE", "862", "367", "Venezuela", "Bolivarische Republik Venezuela", "die Bolivarische Republik Venezuela", "venezolanisch", null, null)
	}),

	/** Vereinigte Arabische Emirate (ARE) */
	ARE(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(65082069, "ARE", "AE", "784", "469", "Vereinigte Arabische Emirate", "Vereinigte Arabische Emirate", "die Vereinigten Arabischen Emirate (Abu Dhabi, Adschman, Dubai, Fudschaira, Ras al Chaima, Schardscha, Umm al Kaiwain)", "der Vereinigten Arabischen Emirate", 1971, null)
	}),

	/** Vereinigte Staaten (USA) */
	USA(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(85083065, "USA", "US", "840", "368", "Vereinigte Staaten", "Vereinigte Staaten", "die Vereinigten Staaten von Amerika", "amerikanisch", null, null)
	}),

	/** Vereinigtes Königreich (GBR) */
	GBR(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(71066082, "GBR", "GB", "826", "168", "Vereinigtes Königreich", "Vereinigtes Königreich", "das Vereinigte Königreich Großbritannien und Nordirland", "britisch", null, null)
	}),

	/** Vietnam (VNM) */
	VNM(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(86078077, "VNM", "VN", "704", "432", "Vietnam", "Vietnam", "die Sozialistische Republik Vietnam", "vietnamesisch", 1975, null)
	}),

	/** Zentralafrikanische Republik (CAF) */
	CAF(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(67065070, "CAF", "CF", "140", "289", "Zentralafrikanische Republik", "Zentralafrikanische Republik", "die Zentralafrikanische Republik", "zentralafrikanisch", 1960, null)
	}),

	/** Zypern (CYP) */
	CYP(new NationalitaetenKatalogEintrag[] {
		new NationalitaetenKatalogEintrag(67089080, "CYP", "CY", "196", "181", "Zypern", "Zypern", "die Republik Zypern", "zyprisch", 1960, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	
	
	/** Der letzten gültigen Daten der Nationalität für den ISO-Code */
	public final @NotNull NationalitaetenKatalogEintrag daten;
	
	/** Die Historie mit den Einträgen der Nationalitäten für den ISO-Code */
	public final @NotNull NationalitaetenKatalogEintrag@NotNull[] historie;	

	/** Eine Hashmap mit allen definierten Nationalitäten, zugeordnet zu dem dreistelligen ISO-Code */ 
	private static final @NotNull HashMap<@NotNull String, @NotNull Nationalitaeten> _mapISO3 = new HashMap<>();

	/** Eine Hashmap mit allen definierten Nationalitäten, zugeordnet zu dem zweistelligen ISO-Code */ 
	private static final @NotNull HashMap<@NotNull String, @NotNull Nationalitaeten> _mapISO2 = new HashMap<>();

	/** Eine Hashmap mit allen definierten Nationalitäten, zugeordnet zu DESTATIS-Code */ 
	private static final @NotNull HashMap<@NotNull String, @NotNull Nationalitaeten> _mapDESTATIS = new HashMap<>();

	

	/**
	 * Erzeugt eine neue Nationalität in der Aufzählung.
	 * 
	 * @param historie   die Historie der Nationalitäten, welches ein Array von {@link NationalitaetenKatalogEintrag} ist  
	 */
	private Nationalitaeten(final @NotNull NationalitaetenKatalogEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist 
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt eine Map von den dreistelligen ISO-Codes der Nationalitäten auf die zugehörigen Nationalitäten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln der Nationalitäten auf die zugehörigen Nationalitäten
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull Nationalitaeten> getMapISO3() {
		if (_mapISO3.size() == 0) {
			for (final Nationalitaeten s : Nationalitaeten.values()) {
				if (s.daten != null)
					_mapISO3.put(s.daten.iso3, s);
			}
		}
		return _mapISO3;
	}


	/**
	 * Gibt eine Map von den zweistelligen ISO-Codes der Nationalitäten auf die zugehörigen Nationalitäten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln der Nationalitäten auf die zugehörigen Nationalitäten
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull Nationalitaeten> getMapISO2() {
		if (_mapISO2.size() == 0) {
			for (final Nationalitaeten s : Nationalitaeten.values()) {
				if (s.daten != null)
					_mapISO2.put(s.daten.iso2, s);
			}
		}
		return _mapISO2;
	}


	/**
	 * Gibt eine Map von den DESTATIS-Codes der Nationalitäten auf die zugehörigen Nationalitäten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln der Nationalitäten auf die zugehörigen Nationalitäten
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull Nationalitaeten> getMapDESTATIS() {
		if (_mapDESTATIS.size() == 0) {
			for (final Nationalitaeten s : Nationalitaeten.values()) {
				if (s.daten != null)
					_mapDESTATIS.put(s.daten.codeDEStatis, s);
			}
		}
		return _mapDESTATIS;
	}


	/**
	 * Gibt die Nationalität für den angegebenen dreistelligen ISO-Code nach ISO 3166-1 zurück.
	 * 
	 * @param code   der ISO-Code
	 * 
	 * @return die Nationalität oder null, falls der Code unbekannt ist
	 */
	public static Nationalitaeten getByISO3(final String code) {
		return getMapISO3().get(code);
	}

	/**
	 * Gibt die Nationalität für den angegebenen zweistelligen ISO-Code nach ISO 3166-1 zurück.
	 * 
	 * @param code   der ISO-Code
	 * 
	 * @return die Nationalität oder null, falls der Code unbekannt ist
	 */
	public static Nationalitaeten getByISO2(final String code) {
		return getMapISO2().get(code);
	}

	/**
	 * Gibt die Nationalität für den angegebenen DESTATIS-Code zurück.
	 * 
	 * @param code   der DESTATIS-Code
	 * 
	 * @return die Nationalität oder null, falls der Code unbekannt ist
	 */
	public static Nationalitaeten getByDESTATIS(final String code) {
		return getMapDESTATIS().get(code);
	}

}
