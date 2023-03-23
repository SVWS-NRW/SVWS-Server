import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap } from '../../../java/util/HashMap';
import { VerkehrsspracheKatalogEintrag } from '../../../core/data/schule/VerkehrsspracheKatalogEintrag';

export class Verkehrssprache extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<Verkehrssprache> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, Verkehrssprache> = new Map<string, Verkehrssprache>();

	/**
	 * Afar (aar)
	 */
	public static readonly AAR : Verkehrssprache = new Verkehrssprache("AAR", 0, [new VerkehrsspracheKatalogEintrag(1000, "aar", "Afar", "aa", null, null)]);

	/**
	 * Abchasisch (abk)
	 */
	public static readonly ABK : Verkehrssprache = new Verkehrssprache("ABK", 1, [new VerkehrsspracheKatalogEintrag(2000, "abk", "Abchasisch", "ab", null, null)]);

	/**
	 * Abron (abr) - ISO 639-3
	 */
	public static readonly ABR : Verkehrssprache = new Verkehrssprache("ABR", 2, [new VerkehrsspracheKatalogEintrag(3000, "abr", "Abron", null, null, null)]);

	/**
	 * Achinesisch (ace)
	 */
	public static readonly ACE : Verkehrssprache = new Verkehrssprache("ACE", 3, [new VerkehrsspracheKatalogEintrag(4000, "ace", "Achinesisch", null, null, null)]);

	/**
	 * Acoli (ach)
	 */
	public static readonly ACH : Verkehrssprache = new Verkehrssprache("ACH", 4, [new VerkehrsspracheKatalogEintrag(5000, "ach", "Acoli", null, null, null)]);

	/**
	 * Adangme (ada)
	 */
	public static readonly ADA : Verkehrssprache = new Verkehrssprache("ADA", 5, [new VerkehrsspracheKatalogEintrag(6000, "ada", "Adangme", null, null, null)]);

	/**
	 * Adygeisch (ady)
	 */
	public static readonly ADY : Verkehrssprache = new Verkehrssprache("ADY", 6, [new VerkehrsspracheKatalogEintrag(7000, "ady", "Adygeisch", null, null, null)]);

	/**
	 * Afrihili (afh)
	 */
	public static readonly AFH : Verkehrssprache = new Verkehrssprache("AFH", 7, [new VerkehrsspracheKatalogEintrag(8000, "afh", "Afrihili", null, null, null)]);

	/**
	 * Afrikaans (afr)
	 */
	public static readonly AFR : Verkehrssprache = new Verkehrssprache("AFR", 8, [new VerkehrsspracheKatalogEintrag(9000, "afr", "Afrikaans", "af", null, null)]);

	/**
	 * Awutu (afu) - ISO 639-3
	 */
	public static readonly AFU : Verkehrssprache = new Verkehrssprache("AFU", 9, [new VerkehrsspracheKatalogEintrag(10000, "afu", "Awutu", null, null, null)]);

	/**
	 * Assyrisch-neuaramäischer Dialekt (aii) - ISO 639-3
	 */
	public static readonly AII : Verkehrssprache = new Verkehrssprache("AII", 10, [new VerkehrsspracheKatalogEintrag(11000, "aii", "Assyrisch-neuaramäischer Dialekt", null, null, null)]);

	/**
	 * Ainu (ain)
	 */
	public static readonly AIN : Verkehrssprache = new Verkehrssprache("AIN", 11, [new VerkehrsspracheKatalogEintrag(12000, "ain", "Ainu", null, null, null)]);

	/**
	 * Aja (aja) - ISO 639-3
	 */
	public static readonly AJA : Verkehrssprache = new Verkehrssprache("AJA", 12, [new VerkehrsspracheKatalogEintrag(13000, "aja", "Aja", null, null, null)]);

	/**
	 * Akan (aka)
	 */
	public static readonly AKA : Verkehrssprache = new Verkehrssprache("AKA", 13, [new VerkehrsspracheKatalogEintrag(14000, "aka", "Akan", "ak", null, null)]);

	/**
	 * Akkadisch (akk)
	 */
	public static readonly AKK : Verkehrssprache = new Verkehrssprache("AKK", 14, [new VerkehrsspracheKatalogEintrag(15000, "akk", "Akkadisch", null, null, null)]);

	/**
	 * Natchamba (aks) - ISO 639-3
	 */
	public static readonly AKS : Verkehrssprache = new Verkehrssprache("AKS", 15, [new VerkehrsspracheKatalogEintrag(16000, "aks", "Natchamba", null, null, null)]);

	/**
	 * Aleutisch (ale)
	 */
	public static readonly ALE : Verkehrssprache = new Verkehrssprache("ALE", 16, [new VerkehrsspracheKatalogEintrag(17000, "ale", "Aleutisch", null, null, null)]);

	/**
	 * Südaltaisch (alt)
	 */
	public static readonly ALT : Verkehrssprache = new Verkehrssprache("ALT", 17, [new VerkehrsspracheKatalogEintrag(18000, "alt", "Südaltaisch", null, null, null)]);

	/**
	 * Amharisch (amh)
	 */
	public static readonly AMH : Verkehrssprache = new Verkehrssprache("AMH", 18, [new VerkehrsspracheKatalogEintrag(19000, "amh", "Amharisch", "am", null, null)]);

	/**
	 * Altenglisch (ang)
	 */
	public static readonly ANG : Verkehrssprache = new Verkehrssprache("ANG", 19, [new VerkehrsspracheKatalogEintrag(20000, "ang", "Altenglisch", null, null, null)]);

	/**
	 * Angika (anp)
	 */
	public static readonly ANP : Verkehrssprache = new Verkehrssprache("ANP", 20, [new VerkehrsspracheKatalogEintrag(21000, "anp", "Angika", null, null, null)]);

	/**
	 * Anuak (anu)
	 */
	public static readonly ANU : Verkehrssprache = new Verkehrssprache("ANU", 21, [new VerkehrsspracheKatalogEintrag(22000, "anu", "Anuak", null, null, null)]);

	/**
	 * Anyin (any)
	 */
	public static readonly ANY : Verkehrssprache = new Verkehrssprache("ANY", 22, [new VerkehrsspracheKatalogEintrag(23000, "any", "Anyin", null, null, null)]);

	/**
	 * Arabisch (ara)
	 */
	public static readonly ARA : Verkehrssprache = new Verkehrssprache("ARA", 23, [new VerkehrsspracheKatalogEintrag(24000, "ara", "Arabisch", "ar", null, null)]);

	/**
	 * Aramäisch (arc)
	 */
	public static readonly ARC : Verkehrssprache = new Verkehrssprache("ARC", 24, [new VerkehrsspracheKatalogEintrag(25000, "arc", "Aramäisch", null, null, null)]);

	/**
	 * Aragonesisch (arg)
	 */
	public static readonly ARG : Verkehrssprache = new Verkehrssprache("ARG", 25, [new VerkehrsspracheKatalogEintrag(26000, "arg", "Aragonesisch", "an", null, null)]);

	/**
	 * Mapudungun (arn)
	 */
	public static readonly ARN : Verkehrssprache = new Verkehrssprache("ARN", 26, [new VerkehrsspracheKatalogEintrag(27000, "arn", "Mapudungun", null, null, null)]);

	/**
	 * Algerisch-Arabisch (arq) - ISO 639-3
	 */
	public static readonly ARQ : Verkehrssprache = new Verkehrssprache("ARQ", 27, [new VerkehrsspracheKatalogEintrag(28000, "arq", "Algerisch-Arabisch", null, null, null)]);

	/**
	 * Arawak (arw)
	 */
	public static readonly ARW : Verkehrssprache = new Verkehrssprache("ARW", 28, [new VerkehrsspracheKatalogEintrag(29000, "arw", "Arawak", null, null, null)]);

	/**
	 * Marokkanisch-Arabisch (ary) - ISO 639-3
	 */
	public static readonly ARY : Verkehrssprache = new Verkehrssprache("ARY", 29, [new VerkehrsspracheKatalogEintrag(30000, "ary", "Marokkanisch-Arabisch", null, null, null)]);

	/**
	 * Tsischingini (asg) - ISO 639-3
	 */
	public static readonly ASG : Verkehrssprache = new Verkehrssprache("ASG", 30, [new VerkehrsspracheKatalogEintrag(31000, "asg", "Tsischingini", null, null, null)]);

	/**
	 * Assamesisch (asm)
	 */
	public static readonly ASM : Verkehrssprache = new Verkehrssprache("ASM", 31, [new VerkehrsspracheKatalogEintrag(32000, "asm", "Assamesisch", "as", null, null)]);

	/**
	 * Asturisch (ast)
	 */
	public static readonly AST : Verkehrssprache = new Verkehrssprache("AST", 32, [new VerkehrsspracheKatalogEintrag(33000, "ast", "Asturisch", null, null, null)]);

	/**
	 * Awarisch (ava)
	 */
	public static readonly AVA : Verkehrssprache = new Verkehrssprache("AVA", 33, [new VerkehrsspracheKatalogEintrag(34000, "ava", "Awarisch", "av", null, null)]);

	/**
	 * Avestisch (ave)
	 */
	public static readonly AVE : Verkehrssprache = new Verkehrssprache("AVE", 34, [new VerkehrsspracheKatalogEintrag(35000, "ave", "Avestisch", null, null, null)]);

	/**
	 * Awadhi (awa)
	 */
	public static readonly AWA : Verkehrssprache = new Verkehrssprache("AWA", 35, [new VerkehrsspracheKatalogEintrag(36000, "awa", "Awadhi", null, null, null)]);

	/**
	 * Aymara (aym)
	 */
	public static readonly AYM : Verkehrssprache = new Verkehrssprache("AYM", 36, [new VerkehrsspracheKatalogEintrag(37000, "aym", "Aymara", "ay", null, null)]);

	/**
	 * Süd-Aserbaidschanisch (azb) - ISO 639-3
	 */
	public static readonly AZB : Verkehrssprache = new Verkehrssprache("AZB", 37, [new VerkehrsspracheKatalogEintrag(38000, "azb", "Süd-Aserbaidschanisch", null, null, null)]);

	/**
	 * Aserbaidschanisch (aze)
	 */
	public static readonly AZE : Verkehrssprache = new Verkehrssprache("AZE", 38, [new VerkehrsspracheKatalogEintrag(39000, "aze", "Aserbaidschanisch", "az", null, null)]);

	/**
	 * Banda (bad)
	 */
	public static readonly BAD : Verkehrssprache = new Verkehrssprache("BAD", 39, [new VerkehrsspracheKatalogEintrag(40000, "bad", "Banda", null, null, null)]);

	/**
	 * Bamileke (bai)
	 */
	public static readonly BAI : Verkehrssprache = new Verkehrssprache("BAI", 40, [new VerkehrsspracheKatalogEintrag(41000, "bai", "Bamileke", null, null, null)]);

	/**
	 * Baschkirisch (bak)
	 */
	public static readonly BAK : Verkehrssprache = new Verkehrssprache("BAK", 41, [new VerkehrsspracheKatalogEintrag(42000, "bak", "Baschkirisch", "ba", null, null)]);

	/**
	 * Belutschisch (bal)
	 */
	public static readonly BAL : Verkehrssprache = new Verkehrssprache("BAL", 42, [new VerkehrsspracheKatalogEintrag(43000, "bal", "Belutschisch", null, null, null)]);

	/**
	 * Bambara (bam)
	 */
	public static readonly BAM : Verkehrssprache = new Verkehrssprache("BAM", 43, [new VerkehrsspracheKatalogEintrag(44000, "bam", "Bambara", "bm", null, null)]);

	/**
	 * Balinesisch (ban)
	 */
	public static readonly BAN : Verkehrssprache = new Verkehrssprache("BAN", 44, [new VerkehrsspracheKatalogEintrag(45000, "ban", "Balinesisch", null, null, null)]);

	/**
	 * Bairisch (bar) - ISO 639-3
	 */
	public static readonly BAR : Verkehrssprache = new Verkehrssprache("BAR", 45, [new VerkehrsspracheKatalogEintrag(45800, "bar", "Bairisch", null, null, null)]);

	/**
	 * Basa (bas)
	 */
	public static readonly BAS : Verkehrssprache = new Verkehrssprache("BAS", 46, [new VerkehrsspracheKatalogEintrag(46000, "bas", "Basa", null, null, null)]);

	/**
	 * Bamun (bax) - ISO 639-3
	 */
	public static readonly BAX : Verkehrssprache = new Verkehrssprache("BAX", 47, [new VerkehrsspracheKatalogEintrag(47000, "bax", "Bamun", null, null, null)]);

	/**
	 * Baatonum (bba) - ISO 639-3
	 */
	public static readonly BBA : Verkehrssprache = new Verkehrssprache("BBA", 48, [new VerkehrsspracheKatalogEintrag(48000, "bba", "Baatonum", null, null, null)]);

	/**
	 * Ghomálá (bbj) - ISO 639-3
	 */
	public static readonly BBJ : Verkehrssprache = new Verkehrssprache("BBJ", 49, [new VerkehrsspracheKatalogEintrag(49000, "bbj", "Ghomálá", null, null, null)]);

	/**
	 * Baoulé (bci) - ISO 639-3
	 */
	public static readonly BCI : Verkehrssprache = new Verkehrssprache("BCI", 50, [new VerkehrsspracheKatalogEintrag(50000, "bci", "Baoulé", null, null, null)]);

	/**
	 * Beja (bej)
	 */
	public static readonly BEJ : Verkehrssprache = new Verkehrssprache("BEJ", 51, [new VerkehrsspracheKatalogEintrag(51000, "bej", "Beja", null, null, null)]);

	/**
	 * Weißrussisch (bel)
	 */
	public static readonly BEL : Verkehrssprache = new Verkehrssprache("BEL", 52, [new VerkehrsspracheKatalogEintrag(52000, "bel", "Weißrussisch", "be", null, null)]);

	/**
	 * Bemba (bem)
	 */
	public static readonly BEM : Verkehrssprache = new Verkehrssprache("BEM", 53, [new VerkehrsspracheKatalogEintrag(53000, "bem", "Bemba", null, null, null)]);

	/**
	 * Bengali (ben)
	 */
	public static readonly BEN : Verkehrssprache = new Verkehrssprache("BEN", 54, [new VerkehrsspracheKatalogEintrag(54000, "ben", "Bengali", "bn", null, null)]);

	/**
	 * Berbersprachen (Andere) (ber)
	 */
	public static readonly BER : Verkehrssprache = new Verkehrssprache("BER", 55, [new VerkehrsspracheKatalogEintrag(55000, "ber", "Berbersprachen (Andere)", null, null, null)]);

	/**
	 * Guiberoua Béte (bet) - ISO 639-3
	 */
	public static readonly BET : Verkehrssprache = new Verkehrssprache("BET", 56, [new VerkehrsspracheKatalogEintrag(56000, "bet", "Guiberoua Béte", null, null, null)]);

	/**
	 * Daloa Bété (bev) - ISO 639-3
	 */
	public static readonly BEV : Verkehrssprache = new Verkehrssprache("BEV", 57, [new VerkehrsspracheKatalogEintrag(57000, "bev", "Daloa Bété", null, null, null)]);

	/**
	 * Bari (bfa) - ISO 639-3
	 */
	public static readonly BFA : Verkehrssprache = new Verkehrssprache("BFA", 58, [new VerkehrsspracheKatalogEintrag(58000, "bfa", "Bari", null, null, null)]);

	/**
	 * Bhojpuri (bho)
	 */
	public static readonly BHO : Verkehrssprache = new Verkehrssprache("BHO", 59, [new VerkehrsspracheKatalogEintrag(59000, "bho", "Bhojpuri", null, null, null)]);

	/**
	 * Bhele (bhy) - ISO 639-3
	 */
	public static readonly BHY : Verkehrssprache = new Verkehrssprache("BHY", 60, [new VerkehrsspracheKatalogEintrag(60000, "bhy", "Bhele", null, null, null)]);

	/**
	 * Bissa (bib) - ISO 639-3
	 */
	public static readonly BIB : Verkehrssprache = new Verkehrssprache("BIB", 61, [new VerkehrsspracheKatalogEintrag(61000, "bib", "Bissa", null, null, null)]);

	/**
	 * Bihari (bih)
	 */
	public static readonly BIH : Verkehrssprache = new Verkehrssprache("BIH", 62, [new VerkehrsspracheKatalogEintrag(62000, "bih", "Bihari", "bh", null, null)]);

	/**
	 * Bikol (bik)
	 */
	public static readonly BIK : Verkehrssprache = new Verkehrssprache("BIK", 63, [new VerkehrsspracheKatalogEintrag(63000, "bik", "Bikol", null, null, null)]);

	/**
	 * Bini (bin)
	 */
	public static readonly BIN : Verkehrssprache = new Verkehrssprache("BIN", 64, [new VerkehrsspracheKatalogEintrag(64000, "bin", "Bini", null, null, null)]);

	/**
	 * Bislama (bis)
	 */
	public static readonly BIS : Verkehrssprache = new Verkehrssprache("BIS", 65, [new VerkehrsspracheKatalogEintrag(65000, "bis", "Bislama", "bi", null, null)]);

	/**
	 * Lame (bma) - ISO 639-3
	 */
	public static readonly BMA : Verkehrssprache = new Verkehrssprache("BMA", 66, [new VerkehrsspracheKatalogEintrag(66000, "bma", "Lame", null, null, null)]);

	/**
	 * Bomu (bmq) - ISO 639-3
	 */
	public static readonly BMQ : Verkehrssprache = new Verkehrssprache("BMQ", 67, [new VerkehrsspracheKatalogEintrag(67000, "bmq", "Bomu", null, null, null)]);

	/**
	 * Bantu (bnt)
	 */
	public static readonly BNT : Verkehrssprache = new Verkehrssprache("BNT", 68, [new VerkehrsspracheKatalogEintrag(68000, "bnt", "Bantu", null, null, null)]);

	/**
	 * Tibetisch (bod)
	 */
	public static readonly BOD : Verkehrssprache = new Verkehrssprache("BOD", 69, [new VerkehrsspracheKatalogEintrag(69000, "bod", "Tibetisch", "bo", null, null)]);

	/**
	 * Bosnisch (bos)
	 */
	public static readonly BOS : Verkehrssprache = new Verkehrssprache("BOS", 70, [new VerkehrsspracheKatalogEintrag(70000, "bos", "Bosnisch", "bs", null, null)]);

	/**
	 * Bretonisch (bre)
	 */
	public static readonly BRE : Verkehrssprache = new Verkehrssprache("BRE", 71, [new VerkehrsspracheKatalogEintrag(71000, "bre", "Bretonisch", "br", null, null)]);

	/**
	 * Gagnoa Bété (btg) - ISO 639-3
	 */
	public static readonly BTG : Verkehrssprache = new Verkehrssprache("BTG", 72, [new VerkehrsspracheKatalogEintrag(72000, "btg", "Gagnoa Bété", null, null, null)]);

	/**
	 * Batak (btk)
	 */
	public static readonly BTK : Verkehrssprache = new Verkehrssprache("BTK", 73, [new VerkehrsspracheKatalogEintrag(73000, "btk", "Batak", null, null, null)]);

	/**
	 * Burjatisch (bua)
	 */
	public static readonly BUA : Verkehrssprache = new Verkehrssprache("BUA", 74, [new VerkehrsspracheKatalogEintrag(74000, "bua", "Burjatisch", null, null, null)]);

	/**
	 * Ntcham (bud) - ISO 639-3
	 */
	public static readonly BUD : Verkehrssprache = new Verkehrssprache("BUD", 75, [new VerkehrsspracheKatalogEintrag(75000, "bud", "Ntcham", null, null, null)]);

	/**
	 * Bushoong (buf) - ISO 639-3
	 */
	public static readonly BUF : Verkehrssprache = new Verkehrssprache("BUF", 76, [new VerkehrsspracheKatalogEintrag(76000, "buf", "Bushoong", null, null, null)]);

	/**
	 * Buginesisch (bug)
	 */
	public static readonly BUG : Verkehrssprache = new Verkehrssprache("BUG", 77, [new VerkehrsspracheKatalogEintrag(77000, "bug", "Buginesisch", null, null, null)]);

	/**
	 * Bulgarisch (bul)
	 */
	public static readonly BUL : Verkehrssprache = new Verkehrssprache("BUL", 78, [new VerkehrsspracheKatalogEintrag(78000, "bul", "Bulgarisch", "bg", null, null)]);

	/**
	 * Bulu (bum) - ISO 639-3
	 */
	public static readonly BUM : Verkehrssprache = new Verkehrssprache("BUM", 79, [new VerkehrsspracheKatalogEintrag(79000, "bum", "Bulu", null, null, null)]);

	/**
	 * Bubi (buw) - ISO 639-3
	 */
	public static readonly BUW : Verkehrssprache = new Verkehrssprache("BUW", 80, [new VerkehrsspracheKatalogEintrag(80000, "buw", "Bubi", null, null, null)]);

	/**
	 * Buli (bwu) - ISO 639-3
	 */
	public static readonly BWU : Verkehrssprache = new Verkehrssprache("BWU", 81, [new VerkehrsspracheKatalogEintrag(81000, "bwu", "Buli", null, null, null)]);

	/**
	 * Bilin (byn)
	 */
	public static readonly BYN : Verkehrssprache = new Verkehrssprache("BYN", 82, [new VerkehrsspracheKatalogEintrag(82000, "byn", "Bilin", null, null, null)]);

	/**
	 * Medumba (byv) - ISO 639-3
	 */
	public static readonly BYV : Verkehrssprache = new Verkehrssprache("BYV", 83, [new VerkehrsspracheKatalogEintrag(83000, "byv", "Medumba", null, null, null)]);

	/**
	 * Basa (bzw) - ISO 639-3
	 */
	public static readonly BZW : Verkehrssprache = new Verkehrssprache("BZW", 84, [new VerkehrsspracheKatalogEintrag(84000, "bzw", "Basa", null, null, null)]);

	/**
	 * Katalanisch (cat)
	 */
	public static readonly CAT : Verkehrssprache = new Verkehrssprache("CAT", 85, [new VerkehrsspracheKatalogEintrag(85000, "cat", "Katalanisch", "ca", null, null)]);

	/**
	 * Kaukasisch (cau)
	 */
	public static readonly CAU : Verkehrssprache = new Verkehrssprache("CAU", 86, [new VerkehrsspracheKatalogEintrag(86000, "cau", "Kaukasisch", null, null, null)]);

	/**
	 * Cebuano (ceb)
	 */
	public static readonly CEB : Verkehrssprache = new Verkehrssprache("CEB", 87, [new VerkehrsspracheKatalogEintrag(87000, "ceb", "Cebuano", null, null, null)]);

	/**
	 * Tschechisch (ces)
	 */
	public static readonly CES : Verkehrssprache = new Verkehrssprache("CES", 88, [new VerkehrsspracheKatalogEintrag(88000, "ces", "Tschechisch", "cs", null, null)]);

	/**
	 * Tschetschenisch (che)
	 */
	public static readonly CHE : Verkehrssprache = new Verkehrssprache("CHE", 89, [new VerkehrsspracheKatalogEintrag(89000, "che", "Tschetschenisch", "ce", null, null)]);

	/**
	 * Tschuwaschisch (chv)
	 */
	public static readonly CHV : Verkehrssprache = new Verkehrssprache("CHV", 90, [new VerkehrsspracheKatalogEintrag(90000, "chv", "Tschuwaschisch", "cv", null, null)]);

	/**
	 * Chokwe (cjk) - ISO 639-3
	 */
	public static readonly CJK : Verkehrssprache = new Verkehrssprache("CJK", 91, [new VerkehrsspracheKatalogEintrag(91000, "cjk", "Chokwe", null, null, null)]);

	/**
	 * Anufo (cko) - ISO 639-3
	 */
	public static readonly CKO : Verkehrssprache = new Verkehrssprache("CKO", 92, [new VerkehrsspracheKatalogEintrag(92000, "cko", "Anufo", null, null, null)]);

	/**
	 * Chaldäisches Aramäisch (cld) - ISO 639-3
	 */
	public static readonly CLD : Verkehrssprache = new Verkehrssprache("CLD", 93, [new VerkehrsspracheKatalogEintrag(93000, "cld", "Chaldäisches Aramäisch", null, null, null)]);

	/**
	 * Montenegrinisch (cnr)
	 */
	public static readonly CNR : Verkehrssprache = new Verkehrssprache("CNR", 94, [new VerkehrsspracheKatalogEintrag(94000, "cnr", "Montenegrinisch", null, null, null)]);

	/**
	 * Kornisch (cor)
	 */
	public static readonly COR : Verkehrssprache = new Verkehrssprache("COR", 95, [new VerkehrsspracheKatalogEintrag(95000, "cor", "Kornisch", "kw", null, null)]);

	/**
	 * Korsisch (cos)
	 */
	public static readonly COS : Verkehrssprache = new Verkehrssprache("COS", 96, [new VerkehrsspracheKatalogEintrag(96000, "cos", "Korsisch", "co", null, null)]);

	/**
	 * Kreolisch-Englisch (cpe)
	 */
	public static readonly CPE : Verkehrssprache = new Verkehrssprache("CPE", 97, [new VerkehrsspracheKatalogEintrag(97000, "cpe", "Kreolisch-Englisch", null, null, null)]);

	/**
	 * Kreolisch-Französisch (cpf)
	 */
	public static readonly CPF : Verkehrssprache = new Verkehrssprache("CPF", 98, [new VerkehrsspracheKatalogEintrag(98000, "cpf", "Kreolisch-Französisch", null, null, null)]);

	/**
	 * Kreolisch-Portugiesisch (cpp)
	 */
	public static readonly CPP : Verkehrssprache = new Verkehrssprache("CPP", 99, [new VerkehrsspracheKatalogEintrag(99000, "cpp", "Kreolisch-Portugiesisch", null, null, null)]);

	/**
	 * Cree (cre)
	 */
	public static readonly CRE : Verkehrssprache = new Verkehrssprache("CRE", 100, [new VerkehrsspracheKatalogEintrag(100000, "cre", "Cree", "cr", null, null)]);

	/**
	 * Kreolische Sprachen (Andere) (crp)
	 */
	public static readonly CRP : Verkehrssprache = new Verkehrssprache("CRP", 101, [new VerkehrsspracheKatalogEintrag(101000, "crp", "Kreolische Sprachen (Andere)", null, null, null)]);

	/**
	 * Walisisch (cym)
	 */
	public static readonly CYM : Verkehrssprache = new Verkehrssprache("CYM", 102, [new VerkehrsspracheKatalogEintrag(102000, "cym", "Walisisch", "cy", null, null)]);

	/**
	 * Dagbani (dag) - ISO 639-3
	 */
	public static readonly DAG : Verkehrssprache = new Verkehrssprache("DAG", 103, [new VerkehrsspracheKatalogEintrag(103000, "dag", "Dagbani", null, null, null)]);

	/**
	 * Dänisch (dan)
	 */
	public static readonly DAN : Verkehrssprache = new Verkehrssprache("DAN", 104, [new VerkehrsspracheKatalogEintrag(104000, "dan", "Dänisch", "da", null, null)]);

	/**
	 * Darginisch (dar)
	 */
	public static readonly DAR : Verkehrssprache = new Verkehrssprache("DAR", 105, [new VerkehrsspracheKatalogEintrag(105000, "dar", "Darginisch", null, null, null)]);

	/**
	 * Dajakisch (day)
	 */
	public static readonly DAY : Verkehrssprache = new Verkehrssprache("DAY", 106, [new VerkehrsspracheKatalogEintrag(106000, "day", "Dajakisch", null, null, null)]);

	/**
	 * Dendi (ddn) - ISO 639-3
	 */
	public static readonly DDN : Verkehrssprache = new Verkehrssprache("DDN", 107, [new VerkehrsspracheKatalogEintrag(107000, "ddn", "Dendi", null, null, null)]);

	/**
	 * Delaware (del)
	 */
	public static readonly DEL : Verkehrssprache = new Verkehrssprache("DEL", 108, [new VerkehrsspracheKatalogEintrag(108000, "del", "Delaware", null, null, null)]);

	/**
	 * Deutsch (deu)
	 */
	public static readonly DEU : Verkehrssprache = new Verkehrssprache("DEU", 109, [new VerkehrsspracheKatalogEintrag(109000, "deu", "Deutsch", "de", null, null)]);

	/**
	 * Süd-Dagaare (dga) - ISO 639-3
	 */
	public static readonly DGA : Verkehrssprache = new Verkehrssprache("DGA", 110, [new VerkehrsspracheKatalogEintrag(110000, "dga", "Süd-Dagaare", null, null, null)]);

	/**
	 * Dinka (din)
	 */
	public static readonly DIN : Verkehrssprache = new Verkehrssprache("DIN", 111, [new VerkehrsspracheKatalogEintrag(111000, "din", "Dinka", null, null, null)]);

	/**
	 * Maledivisch (div)
	 */
	public static readonly DIV : Verkehrssprache = new Verkehrssprache("DIV", 112, [new VerkehrsspracheKatalogEintrag(112000, "div", "Maledivisch", "dv", null, null)]);

	/**
	 * Zarma (dje) - ISO 639-3
	 */
	public static readonly DJE : Verkehrssprache = new Verkehrssprache("DJE", 113, [new VerkehrsspracheKatalogEintrag(113000, "dje", "Zarma", null, null, null)]);

	/**
	 * Dan (dnj) - ISO 639-3
	 */
	public static readonly DNJ : Verkehrssprache = new Verkehrssprache("DNJ", 114, [new VerkehrsspracheKatalogEintrag(114000, "dnj", "Dan", null, null, null)]);

	/**
	 * Tommo So Dogon (dto) - ISO 639-3
	 */
	public static readonly DTO : Verkehrssprache = new Verkehrssprache("DTO", 115, [new VerkehrsspracheKatalogEintrag(115000, "dto", "Tommo So Dogon", null, null, null)]);

	/**
	 * Duala (dua)
	 */
	public static readonly DUA : Verkehrssprache = new Verkehrssprache("DUA", 116, [new VerkehrsspracheKatalogEintrag(116000, "dua", "Duala", null, null, null)]);

	/**
	 * Diola (dyo) - ISO 639-3
	 */
	public static readonly DYO : Verkehrssprache = new Verkehrssprache("DYO", 117, [new VerkehrsspracheKatalogEintrag(117000, "dyo", "Diola", null, null, null)]);

	/**
	 * Dyula (dyu)
	 */
	public static readonly DYU : Verkehrssprache = new Verkehrssprache("DYU", 118, [new VerkehrsspracheKatalogEintrag(118000, "dyu", "Dyula", null, null, null)]);

	/**
	 * Dazaga (dzg) - ISO 639-3
	 */
	public static readonly DZG : Verkehrssprache = new Verkehrssprache("DZG", 119, [new VerkehrsspracheKatalogEintrag(119000, "dzg", "Dazaga", null, null, null)]);

	/**
	 * Dzongkha (dzo)
	 */
	public static readonly DZO : Verkehrssprache = new Verkehrssprache("DZO", 120, [new VerkehrsspracheKatalogEintrag(120000, "dzo", "Dzongkha", "dz", null, null)]);

	/**
	 * Efik (efi)
	 */
	public static readonly EFI : Verkehrssprache = new Verkehrssprache("EFI", 121, [new VerkehrsspracheKatalogEintrag(121000, "efi", "Efik", null, null, null)]);

	/**
	 * Ekajuk (eka)
	 */
	public static readonly EKA : Verkehrssprache = new Verkehrssprache("EKA", 122, [new VerkehrsspracheKatalogEintrag(122000, "eka", "Ekajuk", null, null, null)]);

	/**
	 * Griechisch (ell)
	 */
	public static readonly ELL : Verkehrssprache = new Verkehrssprache("ELL", 123, [new VerkehrsspracheKatalogEintrag(123000, "ell", "Griechisch", "el", null, null)]);

	/**
	 * Englisch (eng)
	 */
	public static readonly ENG : Verkehrssprache = new Verkehrssprache("ENG", 124, [new VerkehrsspracheKatalogEintrag(124000, "eng", "Englisch", "en", null, null)]);

	/**
	 * Estnisch (est)
	 */
	public static readonly EST : Verkehrssprache = new Verkehrssprache("EST", 125, [new VerkehrsspracheKatalogEintrag(125000, "est", "Estnisch", "et", null, null)]);

	/**
	 * Eton (eto) - ISO 639-3
	 */
	public static readonly ETO : Verkehrssprache = new Verkehrssprache("ETO", 126, [new VerkehrsspracheKatalogEintrag(126000, "eto", "Eton", null, null, null)]);

	/**
	 * Ejagham (etu) - ISO 639-3
	 */
	public static readonly ETU : Verkehrssprache = new Verkehrssprache("ETU", 127, [new VerkehrsspracheKatalogEintrag(127000, "etu", "Ejagham", null, null, null)]);

	/**
	 * Baskisch (eus)
	 */
	public static readonly EUS : Verkehrssprache = new Verkehrssprache("EUS", 128, [new VerkehrsspracheKatalogEintrag(128000, "eus", "Baskisch", "eu", null, null)]);

	/**
	 * Ewe (ewe)
	 */
	public static readonly EWE : Verkehrssprache = new Verkehrssprache("EWE", 129, [new VerkehrsspracheKatalogEintrag(129000, "ewe", "Ewe", "ee", null, null)]);

	/**
	 * Ewondo (ewo)
	 */
	public static readonly EWO : Verkehrssprache = new Verkehrssprache("EWO", 130, [new VerkehrsspracheKatalogEintrag(130000, "ewo", "Ewondo", null, null, null)]);

	/**
	 * Fali (fal) - ISO 639-3
	 */
	public static readonly FAL : Verkehrssprache = new Verkehrssprache("FAL", 131, [new VerkehrsspracheKatalogEintrag(131000, "fal", "Fali", null, null, null)]);

	/**
	 * Fang (fan)
	 */
	public static readonly FAN : Verkehrssprache = new Verkehrssprache("FAN", 132, [new VerkehrsspracheKatalogEintrag(132000, "fan", "Fang", null, null, null)]);

	/**
	 * Färöisch (fao)
	 */
	public static readonly FAO : Verkehrssprache = new Verkehrssprache("FAO", 133, [new VerkehrsspracheKatalogEintrag(133000, "fao", "Färöisch", "fo", null, null)]);

	/**
	 * Fanti (fat)
	 */
	public static readonly FAT : Verkehrssprache = new Verkehrssprache("FAT", 134, [new VerkehrsspracheKatalogEintrag(134000, "fat", "Fanti", null, null, null)]);

	/**
	 * Fidschi (fij)
	 */
	public static readonly FIJ : Verkehrssprache = new Verkehrssprache("FIJ", 135, [new VerkehrsspracheKatalogEintrag(135000, "fij", "Fidschi", "fj", null, null)]);

	/**
	 * Finnisch (fin)
	 */
	public static readonly FIN : Verkehrssprache = new Verkehrssprache("FIN", 136, [new VerkehrsspracheKatalogEintrag(136000, "fin", "Finnisch", "fi", null, null)]);

	/**
	 * Fali (fll) - ISO 639-3
	 */
	public static readonly FLL : Verkehrssprache = new Verkehrssprache("FLL", 137, [new VerkehrsspracheKatalogEintrag(137000, "fll", "Fali", null, null, null)]);

	/**
	 * Fe´fe´ (fmp) - ISO 639-3
	 */
	public static readonly FMP : Verkehrssprache = new Verkehrssprache("FMP", 138, [new VerkehrsspracheKatalogEintrag(138000, "fmp", "Fe´fe´", null, null, null)]);

	/**
	 * Fon (fon)
	 */
	public static readonly FON : Verkehrssprache = new Verkehrssprache("FON", 139, [new VerkehrsspracheKatalogEintrag(139000, "fon", "Fon", null, null, null)]);

	/**
	 * Französisch (fra)
	 */
	public static readonly FRA : Verkehrssprache = new Verkehrssprache("FRA", 140, [new VerkehrsspracheKatalogEintrag(140000, "fra", "Französisch", "fr", null, null)]);

	/**
	 * Westfriesisch (fry)
	 */
	public static readonly FRY : Verkehrssprache = new Verkehrssprache("FRY", 141, [new VerkehrsspracheKatalogEintrag(141000, "fry", "Westfriesisch", "fy", null, null)]);

	/**
	 * Adamaua-Fulfulde (fub) - ISO 639-3
	 */
	public static readonly FUB : Verkehrssprache = new Verkehrssprache("FUB", 142, [new VerkehrsspracheKatalogEintrag(142000, "fub", "Adamaua-Fulfulde", null, null, null)]);

	/**
	 * Pulaar (fuc) - ISO 639-3
	 */
	public static readonly FUC : Verkehrssprache = new Verkehrssprache("FUC", 143, [new VerkehrsspracheKatalogEintrag(143000, "fuc", "Pulaar", null, null, null)]);

	/**
	 * Pular (fuf) - ISO 639-3
	 */
	public static readonly FUF : Verkehrssprache = new Verkehrssprache("FUF", 144, [new VerkehrsspracheKatalogEintrag(144000, "fuf", "Pular", null, null, null)]);

	/**
	 * Fulfulde (ful)
	 */
	public static readonly FUL : Verkehrssprache = new Verkehrssprache("FUL", 145, [new VerkehrsspracheKatalogEintrag(145000, "ful", "Fulfulde", "ff", null, null)]);

	/**
	 * Fur (fvr)
	 */
	public static readonly FVR : Verkehrssprache = new Verkehrssprache("FVR", 146, [new VerkehrsspracheKatalogEintrag(146000, "fvr", "Fur", null, null, null)]);

	/**
	 * Ga (gaa)
	 */
	public static readonly GAA : Verkehrssprache = new Verkehrssprache("GAA", 147, [new VerkehrsspracheKatalogEintrag(147000, "gaa", "Ga", null, null, null)]);

	/**
	 * Gayo (gay)
	 */
	public static readonly GAY : Verkehrssprache = new Verkehrssprache("GAY", 148, [new VerkehrsspracheKatalogEintrag(148000, "gay", "Gayo", null, null, null)]);

	/**
	 * Gbaya (gba)
	 */
	public static readonly GBA : Verkehrssprache = new Verkehrssprache("GBA", 149, [new VerkehrsspracheKatalogEintrag(149000, "gba", "Gbaya", null, null, null)]);

	/**
	 * Gen (gej) - ISO 639-3
	 */
	public static readonly GEJ : Verkehrssprache = new Verkehrssprache("GEJ", 150, [new VerkehrsspracheKatalogEintrag(150000, "gej", "Gen", null, null, null)]);

	/**
	 * Schottisch-gälisch (gla)
	 */
	public static readonly GLA : Verkehrssprache = new Verkehrssprache("GLA", 151, [new VerkehrsspracheKatalogEintrag(151000, "gla", "Schottisch-gälisch", "gd", null, null)]);

	/**
	 * Irisch (gle)
	 */
	public static readonly GLE : Verkehrssprache = new Verkehrssprache("GLE", 152, [new VerkehrsspracheKatalogEintrag(152000, "gle", "Irisch", "ga", null, null)]);

	/**
	 * Galicisch (glg)
	 */
	public static readonly GLG : Verkehrssprache = new Verkehrssprache("GLG", 153, [new VerkehrsspracheKatalogEintrag(153000, "glg", "Galicisch", "gl", null, null)]);

	/**
	 * Guro (goa) - ISO 639-3
	 */
	public static readonly GOA : Verkehrssprache = new Verkehrssprache("GOA", 154, [new VerkehrsspracheKatalogEintrag(154000, "goa", "Guro", null, null, null)]);

	/**
	 * Gondi (gon)
	 */
	public static readonly GON : Verkehrssprache = new Verkehrssprache("GON", 155, [new VerkehrsspracheKatalogEintrag(155000, "gon", "Gondi", null, null, null)]);

	/**
	 * Gorontalesisch (gor)
	 */
	public static readonly GOR : Verkehrssprache = new Verkehrssprache("GOR", 156, [new VerkehrsspracheKatalogEintrag(156000, "gor", "Gorontalesisch", null, null, null)]);

	/**
	 * Grebo (grb)
	 */
	public static readonly GRB : Verkehrssprache = new Verkehrssprache("GRB", 157, [new VerkehrsspracheKatalogEintrag(157000, "grb", "Grebo", null, null, null)]);

	/**
	 * Guarani (grn)
	 */
	public static readonly GRN : Verkehrssprache = new Verkehrssprache("GRN", 158, [new VerkehrsspracheKatalogEintrag(158000, "grn", "Guarani", "gn", null, null)]);

	/**
	 * Yocoboué Dida (gud) - ISO 639-3
	 */
	public static readonly GUD : Verkehrssprache = new Verkehrssprache("GUD", 159, [new VerkehrsspracheKatalogEintrag(159000, "gud", "Yocoboué Dida", null, null, null)]);

	/**
	 * Gujarati (guj)
	 */
	public static readonly GUJ : Verkehrssprache = new Verkehrssprache("GUJ", 160, [new VerkehrsspracheKatalogEintrag(160000, "guj", "Gujarati", "gu", null, null)]);

	/**
	 * Farefare (gur) - ISO 639-3
	 */
	public static readonly GUR : Verkehrssprache = new Verkehrssprache("GUR", 161, [new VerkehrsspracheKatalogEintrag(161000, "gur", "Farefare", null, null, null)]);

	/**
	 * Gun (guw) - ISO 639-3
	 */
	public static readonly GUW : Verkehrssprache = new Verkehrssprache("GUW", 162, [new VerkehrsspracheKatalogEintrag(162000, "guw", "Gun", null, null, null)]);

	/**
	 * Gourmanchéma (gux) - ISO 639-3
	 */
	public static readonly GUX : Verkehrssprache = new Verkehrssprache("GUX", 163, [new VerkehrsspracheKatalogEintrag(163000, "gux", "Gourmanchéma", null, null, null)]);

	/**
	 * Gurung (gvr) - ISO 639-3
	 */
	public static readonly GVR : Verkehrssprache = new Verkehrssprache("GVR", 164, [new VerkehrsspracheKatalogEintrag(164000, "gvr", "Gurung", null, null, null)]);

	/**
	 * Gorani (hac) - ISO 639-3
	 */
	public static readonly HAC : Verkehrssprache = new Verkehrssprache("HAC", 165, [new VerkehrsspracheKatalogEintrag(165000, "hac", "Gorani", null, null, null)]);

	/**
	 * Harari (har) - ISO 639-3
	 */
	public static readonly HAR : Verkehrssprache = new Verkehrssprache("HAR", 166, [new VerkehrsspracheKatalogEintrag(166000, "har", "Harari", null, null, null)]);

	/**
	 * Haitianisch (hat)
	 */
	public static readonly HAT : Verkehrssprache = new Verkehrssprache("HAT", 167, [new VerkehrsspracheKatalogEintrag(167000, "hat", "Haitianisch", "ht", null, null)]);

	/**
	 * Hausa (hau)
	 */
	public static readonly HAU : Verkehrssprache = new Verkehrssprache("HAU", 168, [new VerkehrsspracheKatalogEintrag(168000, "hau", "Hausa", "ha", null, null)]);

	/**
	 * Serbokroatisch (hbs)
	 */
	public static readonly HBS : Verkehrssprache = new Verkehrssprache("HBS", 169, [new VerkehrsspracheKatalogEintrag(169000, "hbs", "Serbokroatisch", null, null, null)]);

	/**
	 * Hebräisch (heb)
	 */
	public static readonly HEB : Verkehrssprache = new Verkehrssprache("HEB", 170, [new VerkehrsspracheKatalogEintrag(170000, "heb", "Hebräisch", "he", null, null)]);

	/**
	 * Herero (her)
	 */
	public static readonly HER : Verkehrssprache = new Verkehrssprache("HER", 171, [new VerkehrsspracheKatalogEintrag(171000, "her", "Herero", "hz", null, null)]);

	/**
	 * Hiligaynon (hil)
	 */
	public static readonly HIL : Verkehrssprache = new Verkehrssprache("HIL", 172, [new VerkehrsspracheKatalogEintrag(172000, "hil", "Hiligaynon", null, null, null)]);

	/**
	 * Hindi (hin)
	 */
	public static readonly HIN : Verkehrssprache = new Verkehrssprache("HIN", 173, [new VerkehrsspracheKatalogEintrag(173000, "hin", "Hindi", "hi", null, null)]);

	/**
	 * Hmong (hmn)
	 */
	public static readonly HMN : Verkehrssprache = new Verkehrssprache("HMN", 174, [new VerkehrsspracheKatalogEintrag(174000, "hmn", "Hmong", null, null, null)]);

	/**
	 * Hiri Motu (hmo)
	 */
	public static readonly HMO : Verkehrssprache = new Verkehrssprache("HMO", 175, [new VerkehrsspracheKatalogEintrag(175000, "hmo", "Hiri Motu", "ho", null, null)]);

	/**
	 * Kroatisch (hrv)
	 */
	public static readonly HRV : Verkehrssprache = new Verkehrssprache("HRV", 176, [new VerkehrsspracheKatalogEintrag(176000, "hrv", "Kroatisch", "hr", null, null)]);

	/**
	 * Ungarisch (hun)
	 */
	public static readonly HUN : Verkehrssprache = new Verkehrssprache("HUN", 177, [new VerkehrsspracheKatalogEintrag(177000, "hun", "Ungarisch", "hu", null, null)]);

	/**
	 * Armenisch (hye)
	 */
	public static readonly HYE : Verkehrssprache = new Verkehrssprache("HYE", 178, [new VerkehrsspracheKatalogEintrag(178000, "hye", "Armenisch", "hy", null, null)]);

	/**
	 * Iban (iba)
	 */
	public static readonly IBA : Verkehrssprache = new Verkehrssprache("IBA", 179, [new VerkehrsspracheKatalogEintrag(179000, "iba", "Iban", null, null, null)]);

	/**
	 * Igbo (ibo)
	 */
	public static readonly IBO : Verkehrssprache = new Verkehrssprache("IBO", 180, [new VerkehrsspracheKatalogEintrag(180000, "ibo", "Igbo", null, null, null)]);

	/**
	 * Sichuan-Yi (iii)
	 */
	public static readonly III : Verkehrssprache = new Verkehrssprache("III", 181, [new VerkehrsspracheKatalogEintrag(181000, "iii", "Sichuan-Yi", "ii", null, null)]);

	/**
	 * Ijo (ijo)
	 */
	public static readonly IJO : Verkehrssprache = new Verkehrssprache("IJO", 182, [new VerkehrsspracheKatalogEintrag(182000, "ijo", "Ijo", null, null, null)]);

	/**
	 * Ijo-Südost (ijs) - ISO 639-3
	 */
	public static readonly IJS : Verkehrssprache = new Verkehrssprache("IJS", 183, [new VerkehrsspracheKatalogEintrag(183000, "ijs", "Ijo-Südost", null, null, null)]);

	/**
	 * Inuktitut (iku)
	 */
	public static readonly IKU : Verkehrssprache = new Verkehrssprache("IKU", 184, [new VerkehrsspracheKatalogEintrag(184000, "iku", "Inuktitut", "iu", null, null)]);

	/**
	 * Ilokano (ilo)
	 */
	public static readonly ILO : Verkehrssprache = new Verkehrssprache("ILO", 185, [new VerkehrsspracheKatalogEintrag(185000, "ilo", "Ilokano", null, null, null)]);

	/**
	 * Indonesisch (ind)
	 */
	public static readonly IND : Verkehrssprache = new Verkehrssprache("IND", 186, [new VerkehrsspracheKatalogEintrag(186000, "ind", "Indonesisch", "id", null, null)]);

	/**
	 * Inguschisch (inh)
	 */
	public static readonly INH : Verkehrssprache = new Verkehrssprache("INH", 187, [new VerkehrsspracheKatalogEintrag(187000, "inh", "Inguschisch", null, null, null)]);

	/**
	 * Inupiaq (ipk)
	 */
	public static readonly IPK : Verkehrssprache = new Verkehrssprache("IPK", 188, [new VerkehrsspracheKatalogEintrag(188000, "ipk", "Inupiaq", "ik", null, null)]);

	/**
	 * Esan (ish)
	 */
	public static readonly ISH : Verkehrssprache = new Verkehrssprache("ISH", 189, [new VerkehrsspracheKatalogEintrag(189000, "ish", "Esan", null, null, null)]);

	/**
	 * Isländisch (isl)
	 */
	public static readonly ISL : Verkehrssprache = new Verkehrssprache("ISL", 190, [new VerkehrsspracheKatalogEintrag(190000, "isl", "Isländisch", "is", null, null)]);

	/**
	 * Isoko (iso) - ISO 639-3
	 */
	public static readonly ISO : Verkehrssprache = new Verkehrssprache("ISO", 191, [new VerkehrsspracheKatalogEintrag(191000, "iso", "Isoko", null, null, null)]);

	/**
	 * Italienisch (ita)
	 */
	public static readonly ITA : Verkehrssprache = new Verkehrssprache("ITA", 192, [new VerkehrsspracheKatalogEintrag(192000, "ita", "Italienisch", "it", null, null)]);

	/**
	 * Isekiri (its) - ISO 639-3
	 */
	public static readonly ITS : Verkehrssprache = new Verkehrssprache("ITS", 193, [new VerkehrsspracheKatalogEintrag(193000, "its", "Isekiri", null, null, null)]);

	/**
	 * Hyam (jab) - ISO 639-3
	 */
	public static readonly JAB : Verkehrssprache = new Verkehrssprache("JAB", 194, [new VerkehrsspracheKatalogEintrag(194000, "jab", "Hyam", null, null, null)]);

	/**
	 * Javanisch (jav)
	 */
	public static readonly JAV : Verkehrssprache = new Verkehrssprache("JAV", 195, [new VerkehrsspracheKatalogEintrag(195000, "jav", "Javanisch", "jv", null, null)]);

	/**
	 * Japanisch (jpn)
	 */
	public static readonly JPN : Verkehrssprache = new Verkehrssprache("JPN", 196, [new VerkehrsspracheKatalogEintrag(196000, "jpn", "Japanisch", "ja", null, null)]);

	/**
	 * Kabylisch (kab)
	 */
	public static readonly KAB : Verkehrssprache = new Verkehrssprache("KAB", 197, [new VerkehrsspracheKatalogEintrag(197000, "kab", "Kabylisch", null, null, null)]);

	/**
	 * Grönländisch (kal)
	 */
	public static readonly KAL : Verkehrssprache = new Verkehrssprache("KAL", 198, [new VerkehrsspracheKatalogEintrag(198000, "kal", "Grönländisch", "kl", null, null)]);

	/**
	 * Kamba (kam)
	 */
	public static readonly KAM : Verkehrssprache = new Verkehrssprache("KAM", 199, [new VerkehrsspracheKatalogEintrag(199000, "kam", "Kamba", null, null, null)]);

	/**
	 * Kannada (kan)
	 */
	public static readonly KAN : Verkehrssprache = new Verkehrssprache("KAN", 200, [new VerkehrsspracheKatalogEintrag(200000, "kan", "Kannada", "kn", null, null)]);

	/**
	 * Xaasongaxango (kao) - ISO 639-3
	 */
	public static readonly KAO : Verkehrssprache = new Verkehrssprache("KAO", 201, [new VerkehrsspracheKatalogEintrag(201000, "kao", "Xaasongaxango", null, null, null)]);

	/**
	 * Kashmiri (kas)
	 */
	public static readonly KAS : Verkehrssprache = new Verkehrssprache("KAS", 202, [new VerkehrsspracheKatalogEintrag(202000, "kas", "Kashmiri", "ks", null, null)]);

	/**
	 * Georgisch (kat)
	 */
	public static readonly KAT : Verkehrssprache = new Verkehrssprache("KAT", 203, [new VerkehrsspracheKatalogEintrag(203000, "kat", "Georgisch", "ka", null, null)]);

	/**
	 * Kanuri (kau)
	 */
	public static readonly KAU : Verkehrssprache = new Verkehrssprache("KAU", 204, [new VerkehrsspracheKatalogEintrag(204000, "kau", "Kanuri", "kr", null, null)]);

	/**
	 * Kasachisch (kaz)
	 */
	public static readonly KAZ : Verkehrssprache = new Verkehrssprache("KAZ", 205, [new VerkehrsspracheKatalogEintrag(205000, "kaz", "Kasachisch", "kk", null, null)]);

	/**
	 * Kabardinisch (kbd)
	 */
	public static readonly KBD : Verkehrssprache = new Verkehrssprache("KBD", 206, [new VerkehrsspracheKatalogEintrag(206000, "kbd", "Kabardinisch", null, null, null)]);

	/**
	 * Kanembu (kbl) - ISO 639-3
	 */
	public static readonly KBL : Verkehrssprache = new Verkehrssprache("KBL", 207, [new VerkehrsspracheKatalogEintrag(207000, "kbl", "Kanembu", null, null, null)]);

	/**
	 * Kabiyé (kbp) - ISO 639-3
	 */
	public static readonly KBP : Verkehrssprache = new Verkehrssprache("KBP", 208, [new VerkehrsspracheKatalogEintrag(208000, "kbp", "Kabiyé", null, null, null)]);

	/**
	 * Chimakonde (kde) - ISO 639-3
	 */
	public static readonly KDE : Verkehrssprache = new Verkehrssprache("KDE", 209, [new VerkehrsspracheKatalogEintrag(209000, "kde", "Chimakonde", null, null, null)]);

	/**
	 * Tem (kdh) - ISO 639-3
	 */
	public static readonly KDH : Verkehrssprache = new Verkehrssprache("KDH", 210, [new VerkehrsspracheKatalogEintrag(210000, "kdh", "Tem", null, null, null)]);

	/**
	 * Kenyang (ken) - ISO 639-3
	 */
	public static readonly KEN : Verkehrssprache = new Verkehrssprache("KEN", 211, [new VerkehrsspracheKatalogEintrag(211000, "ken", "Kenyang", null, null, null)]);

	/**
	 * Khasi (kha)
	 */
	public static readonly KHA : Verkehrssprache = new Verkehrssprache("KHA", 212, [new VerkehrsspracheKatalogEintrag(212000, "kha", "Khasi", null, null, null)]);

	/**
	 * Kuturmi (khj) - ISO 639-3
	 */
	public static readonly KHJ : Verkehrssprache = new Verkehrssprache("KHJ", 213, [new VerkehrsspracheKatalogEintrag(213000, "khj", "Kuturmi", null, null, null)]);

	/**
	 * Khmer (khm)
	 */
	public static readonly KHM : Verkehrssprache = new Verkehrssprache("KHM", 214, [new VerkehrsspracheKatalogEintrag(214000, "khm", "Khmer", "km", null, null)]);

	/**
	 * Kikuyu (kik)
	 */
	public static readonly KIK : Verkehrssprache = new Verkehrssprache("KIK", 215, [new VerkehrsspracheKatalogEintrag(215000, "kik", "Kikuyu", "ki", null, null)]);

	/**
	 * Kinyarwanda (kin)
	 */
	public static readonly KIN : Verkehrssprache = new Verkehrssprache("KIN", 216, [new VerkehrsspracheKatalogEintrag(216000, "kin", "Kinyarwanda", "rw", null, null)]);

	/**
	 * Kirgisisch (kir)
	 */
	public static readonly KIR : Verkehrssprache = new Verkehrssprache("KIR", 217, [new VerkehrsspracheKatalogEintrag(217000, "kir", "Kirgisisch", "ky", null, null)]);

	/**
	 * Kisi (kiz) - ISO 639-3
	 */
	public static readonly KIZ : Verkehrssprache = new Verkehrssprache("KIZ", 218, [new VerkehrsspracheKatalogEintrag(218000, "kiz", "Kisi", null, null, null)]);

	/**
	 * Klao (klu) - ISO 639-3
	 */
	public static readonly KLU : Verkehrssprache = new Verkehrssprache("KLU", 219, [new VerkehrsspracheKatalogEintrag(219000, "klu", "Klao", null, null, null)]);

	/**
	 * Kimbundu (kmb)
	 */
	public static readonly KMB : Verkehrssprache = new Verkehrssprache("KMB", 220, [new VerkehrsspracheKatalogEintrag(220000, "kmb", "Kimbundu", null, null, null)]);

	/**
	 * Kono (kno) - ISO 639-3
	 */
	public static readonly KNO : Verkehrssprache = new Verkehrssprache("KNO", 221, [new VerkehrsspracheKatalogEintrag(221000, "kno", "Kono", null, null, null)]);

	/**
	 * Konkani (kok)
	 */
	public static readonly KOK : Verkehrssprache = new Verkehrssprache("KOK", 222, [new VerkehrsspracheKatalogEintrag(222000, "kok", "Konkani", null, null, null)]);

	/**
	 * Komi (kom)
	 */
	public static readonly KOM : Verkehrssprache = new Verkehrssprache("KOM", 223, [new VerkehrsspracheKatalogEintrag(223000, "kom", "Komi", "kv", null, null)]);

	/**
	 * Kongo (kon)
	 */
	public static readonly KON : Verkehrssprache = new Verkehrssprache("KON", 224, [new VerkehrsspracheKatalogEintrag(224000, "kon", "Kongo", "kg", null, null)]);

	/**
	 * Koreanisch (kor)
	 */
	public static readonly KOR : Verkehrssprache = new Verkehrssprache("KOR", 225, [new VerkehrsspracheKatalogEintrag(225000, "kor", "Koreanisch", "ko", null, null)]);

	/**
	 * Lagwan (kot) - ISO 639-3
	 */
	public static readonly KOT : Verkehrssprache = new Verkehrssprache("KOT", 226, [new VerkehrsspracheKatalogEintrag(226000, "kot", "Lagwan", null, null, null)]);

	/**
	 * Kpelle (kpe)
	 */
	public static readonly KPE : Verkehrssprache = new Verkehrssprache("KPE", 227, [new VerkehrsspracheKatalogEintrag(227000, "kpe", "Kpelle", null, null, null)]);

	/**
	 * Kpan (kpk) - ISO 639-3
	 */
	public static readonly KPK : Verkehrssprache = new Verkehrssprache("KPK", 228, [new VerkehrsspracheKatalogEintrag(228000, "kpk", "Kpan", null, null, null)]);

	/**
	 * Nord-Kissi (kqs) - ISO 639-3
	 */
	public static readonly KQS : Verkehrssprache = new Verkehrssprache("KQS", 229, [new VerkehrsspracheKatalogEintrag(229000, "kqs", "Nord-Kissi", null, null, null)]);

	/**
	 * Kru-Sprachen (kro)
	 */
	public static readonly KRO : Verkehrssprache = new Verkehrssprache("KRO", 230, [new VerkehrsspracheKatalogEintrag(230000, "kro", "Kru-Sprachen", null, null, null)]);

	/**
	 * Kurukh (kru)
	 */
	public static readonly KRU : Verkehrssprache = new Verkehrssprache("KRU", 231, [new VerkehrsspracheKatalogEintrag(231000, "kru", "Kurukh", null, null, null)]);

	/**
	 * Kölsch (ksh) - ISO 639-3
	 */
	public static readonly KSH : Verkehrssprache = new Verkehrssprache("KSH", 232, [new VerkehrsspracheKatalogEintrag(231500, "ksh", "Kölsch", null, null, null)]);

	/**
	 * Süd-Kissi (kss) - ISO 639-3
	 */
	public static readonly KSS : Verkehrssprache = new Verkehrssprache("KSS", 233, [new VerkehrsspracheKatalogEintrag(232000, "kss", "Süd-Kissi", null, null, null)]);

	/**
	 * Kuanyama (kua)
	 */
	public static readonly KUA : Verkehrssprache = new Verkehrssprache("KUA", 234, [new VerkehrsspracheKatalogEintrag(233000, "kua", "Kuanyama", "kj", null, null)]);

	/**
	 * Kumykisch (kum)
	 */
	public static readonly KUM : Verkehrssprache = new Verkehrssprache("KUM", 235, [new VerkehrsspracheKatalogEintrag(234000, "kum", "Kumykisch", null, null, null)]);

	/**
	 * Kurdisch (kur)
	 */
	public static readonly KUR : Verkehrssprache = new Verkehrssprache("KUR", 236, [new VerkehrsspracheKatalogEintrag(235000, "kur", "Kurdisch", "ku", null, null)]);

	/**
	 * Kwese (kws) - ISO 639-3
	 */
	public static readonly KWS : Verkehrssprache = new Verkehrssprache("KWS", 237, [new VerkehrsspracheKatalogEintrag(236000, "kws", "Kwese", null, null, null)]);

	/**
	 * Kulango Bondoukou (kzc) - ISO 639-3
	 */
	public static readonly KZC : Verkehrssprache = new Verkehrssprache("KZC", 238, [new VerkehrsspracheKatalogEintrag(237000, "kzc", "Kulango Bondoukou", null, null, null)]);

	/**
	 * Lahnda (lah)
	 */
	public static readonly LAH : Verkehrssprache = new Verkehrssprache("LAH", 239, [new VerkehrsspracheKatalogEintrag(238000, "lah", "Lahnda", null, null, null)]);

	/**
	 * Lango (laj) - ISO 639-3
	 */
	public static readonly LAJ : Verkehrssprache = new Verkehrssprache("LAJ", 240, [new VerkehrsspracheKatalogEintrag(239000, "laj", "Lango", null, null, null)]);

	/**
	 * Lamba (lam)
	 */
	public static readonly LAM : Verkehrssprache = new Verkehrssprache("LAM", 241, [new VerkehrsspracheKatalogEintrag(240000, "lam", "Lamba", null, null, null)]);

	/**
	 * Laotisch (lao)
	 */
	public static readonly LAO : Verkehrssprache = new Verkehrssprache("LAO", 242, [new VerkehrsspracheKatalogEintrag(241000, "lao", "Laotisch", "lo", null, null)]);

	/**
	 * Larteh (lar) - ISO 639-3
	 */
	public static readonly LAR : Verkehrssprache = new Verkehrssprache("LAR", 243, [new VerkehrsspracheKatalogEintrag(242000, "lar", "Larteh", null, null, null)]);

	/**
	 * Lama (las) - ISO 639-3
	 */
	public static readonly LAS : Verkehrssprache = new Verkehrssprache("LAS", 244, [new VerkehrsspracheKatalogEintrag(243000, "las", "Lama", null, null, null)]);

	/**
	 * Lettisch (lav)
	 */
	public static readonly LAV : Verkehrssprache = new Verkehrssprache("LAV", 245, [new VerkehrsspracheKatalogEintrag(244000, "lav", "Lettisch", "lv", null, null)]);

	/**
	 * Lakisch (lbe) - ISO 639-3
	 */
	public static readonly LBE : Verkehrssprache = new Verkehrssprache("LBE", 246, [new VerkehrsspracheKatalogEintrag(245000, "lbe", "Lakisch", null, null, null)]);

	/**
	 * Laari (ldi) - ISO 639-3
	 */
	public static readonly LDI : Verkehrssprache = new Verkehrssprache("LDI", 247, [new VerkehrsspracheKatalogEintrag(246000, "ldi", "Laari", null, null, null)]);

	/**
	 * Lyélé (lee) - ISO 639-3
	 */
	public static readonly LEE : Verkehrssprache = new Verkehrssprache("LEE", 248, [new VerkehrsspracheKatalogEintrag(247000, "lee", "Lyélé", null, null, null)]);

	/**
	 * Lelemi (lef) - ISO 639-3
	 */
	public static readonly LEF : Verkehrssprache = new Verkehrssprache("LEF", 249, [new VerkehrsspracheKatalogEintrag(248000, "lef", "Lelemi", null, null, null)]);

	/**
	 * Lesgisch (lez)
	 */
	public static readonly LEZ : Verkehrssprache = new Verkehrssprache("LEZ", 250, [new VerkehrsspracheKatalogEintrag(249000, "lez", "Lesgisch", null, null, null)]);

	/**
	 * Lugbara (lgg) - ISO 639-3
	 */
	public static readonly LGG : Verkehrssprache = new Verkehrssprache("LGG", 251, [new VerkehrsspracheKatalogEintrag(250000, "lgg", "Lugbara", null, null, null)]);

	/**
	 * Limba (lia) - ISO 639-3
	 */
	public static readonly LIA : Verkehrssprache = new Verkehrssprache("LIA", 252, [new VerkehrsspracheKatalogEintrag(251000, "lia", "Limba", null, null, null)]);

	/**
	 * Ligbi (lig) - ISO 639-3
	 */
	public static readonly LIG : Verkehrssprache = new Verkehrssprache("LIG", 253, [new VerkehrsspracheKatalogEintrag(252000, "lig", "Ligbi", null, null, null)]);

	/**
	 * Limburgisch (lim)
	 */
	public static readonly LIM : Verkehrssprache = new Verkehrssprache("LIM", 254, [new VerkehrsspracheKatalogEintrag(253000, "lim", "Limburgisch", "li", null, null)]);

	/**
	 * Lingala (lin)
	 */
	public static readonly LIN : Verkehrssprache = new Verkehrssprache("LIN", 255, [new VerkehrsspracheKatalogEintrag(254000, "lin", "Lingala", "ln", null, null)]);

	/**
	 * Litauisch (lit)
	 */
	public static readonly LIT : Verkehrssprache = new Verkehrssprache("LIT", 256, [new VerkehrsspracheKatalogEintrag(255000, "lit", "Litauisch", "lt", null, null)]);

	/**
	 * Banda-Bambari (liy) - ISO 639-3
	 */
	public static readonly LIY : Verkehrssprache = new Verkehrssprache("LIY", 257, [new VerkehrsspracheKatalogEintrag(256000, "liy", "Banda-Bambari", null, null, null)]);

	/**
	 * Lamnso (lns) - ISO 639-3
	 */
	public static readonly LNS : Verkehrssprache = new Verkehrssprache("LNS", 258, [new VerkehrsspracheKatalogEintrag(257000, "lns", "Lamnso", null, null, null)]);

	/**
	 * Lobiri (lob) - ISO 639-3
	 */
	public static readonly LOB : Verkehrssprache = new Verkehrssprache("LOB", 259, [new VerkehrsspracheKatalogEintrag(258000, "lob", "Lobiri", null, null, null)]);

	/**
	 * Loko (lok) - ISO 639-3
	 */
	public static readonly LOK : Verkehrssprache = new Verkehrssprache("LOK", 260, [new VerkehrsspracheKatalogEintrag(259000, "lok", "Loko", null, null, null)]);

	/**
	 * Lomongo (lol)
	 */
	public static readonly LOL : Verkehrssprache = new Verkehrssprache("LOL", 261, [new VerkehrsspracheKatalogEintrag(260000, "lol", "Lomongo", null, null, null)]);

	/**
	 * Lozi (loz)
	 */
	public static readonly LOZ : Verkehrssprache = new Verkehrssprache("LOZ", 262, [new VerkehrsspracheKatalogEintrag(261000, "loz", "Lozi", null, null, null)]);

	/**
	 * Lusengo (lse) - ISO 639-3
	 */
	public static readonly LSE : Verkehrssprache = new Verkehrssprache("LSE", 263, [new VerkehrsspracheKatalogEintrag(262000, "lse", "Lusengo", null, null, null)]);

	/**
	 * Luxemburgisch (ltz)
	 */
	public static readonly LTZ : Verkehrssprache = new Verkehrssprache("LTZ", 264, [new VerkehrsspracheKatalogEintrag(263000, "ltz", "Luxemburgisch", "lb", null, null)]);

	/**
	 * Luba-Lulua (lua)
	 */
	public static readonly LUA : Verkehrssprache = new Verkehrssprache("LUA", 265, [new VerkehrsspracheKatalogEintrag(264000, "lua", "Luba-Lulua", null, null, null)]);

	/**
	 * Luba-Katanga (lub)
	 */
	public static readonly LUB : Verkehrssprache = new Verkehrssprache("LUB", 266, [new VerkehrsspracheKatalogEintrag(265000, "lub", "Luba-Katanga", "lu", null, null)]);

	/**
	 * Luganda (lug)
	 */
	public static readonly LUG : Verkehrssprache = new Verkehrssprache("LUG", 267, [new VerkehrsspracheKatalogEintrag(266000, "lug", "Luganda", "lg", null, null)]);

	/**
	 * Lunda (lun)
	 */
	public static readonly LUN : Verkehrssprache = new Verkehrssprache("LUN", 268, [new VerkehrsspracheKatalogEintrag(267000, "lun", "Lunda", null, null, null)]);

	/**
	 * Luo (luo)
	 */
	public static readonly LUO : Verkehrssprache = new Verkehrssprache("LUO", 269, [new VerkehrsspracheKatalogEintrag(268000, "luo", "Luo", null, null, null)]);

	/**
	 * Lushai (lus)
	 */
	public static readonly LUS : Verkehrssprache = new Verkehrssprache("LUS", 270, [new VerkehrsspracheKatalogEintrag(269000, "lus", "Lushai", null, null, null)]);

	/**
	 * Luhya (luy) - ISO 639-3
	 */
	public static readonly LUY : Verkehrssprache = new Verkehrssprache("LUY", 271, [new VerkehrsspracheKatalogEintrag(270000, "luy", "Luhya", null, null, null)]);

	/**
	 * Luwo (lwo) - ISO 639-3
	 */
	public static readonly LWO : Verkehrssprache = new Verkehrssprache("LWO", 272, [new VerkehrsspracheKatalogEintrag(271000, "lwo", "Luwo", null, null, null)]);

	/**
	 * Maduresisch (mad)
	 */
	public static readonly MAD : Verkehrssprache = new Verkehrssprache("MAD", 273, [new VerkehrsspracheKatalogEintrag(272000, "mad", "Maduresisch", null, null, null)]);

	/**
	 * Magadhi (mag)
	 */
	public static readonly MAG : Verkehrssprache = new Verkehrssprache("MAG", 274, [new VerkehrsspracheKatalogEintrag(273000, "mag", "Magadhi", null, null, null)]);

	/**
	 * Marshallesisch (mah)
	 */
	public static readonly MAH : Verkehrssprache = new Verkehrssprache("MAH", 275, [new VerkehrsspracheKatalogEintrag(274000, "mah", "Marshallesisch", "mh", null, null)]);

	/**
	 * Maithili (mai)
	 */
	public static readonly MAI : Verkehrssprache = new Verkehrssprache("MAI", 276, [new VerkehrsspracheKatalogEintrag(275000, "mai", "Maithili", null, null, null)]);

	/**
	 * Makassarisch (mak)
	 */
	public static readonly MAK : Verkehrssprache = new Verkehrssprache("MAK", 277, [new VerkehrsspracheKatalogEintrag(276000, "mak", "Makassarisch", null, null, null)]);

	/**
	 * Malayalam (mal)
	 */
	public static readonly MAL : Verkehrssprache = new Verkehrssprache("MAL", 278, [new VerkehrsspracheKatalogEintrag(277000, "mal", "Malayalam", "ml", null, null)]);

	/**
	 * Mandingo (man)
	 */
	public static readonly MAN : Verkehrssprache = new Verkehrssprache("MAN", 279, [new VerkehrsspracheKatalogEintrag(278000, "man", "Mandingo", null, null, null)]);

	/**
	 * Marathi (mar)
	 */
	public static readonly MAR : Verkehrssprache = new Verkehrssprache("MAR", 280, [new VerkehrsspracheKatalogEintrag(279000, "mar", "Marathi", "mr", null, null)]);

	/**
	 * Masai (mas)
	 */
	public static readonly MAS : Verkehrssprache = new Verkehrssprache("MAS", 281, [new VerkehrsspracheKatalogEintrag(280000, "mas", "Masai", null, null, null)]);

	/**
	 * Mbo (mbo) - ISO 639-3
	 */
	public static readonly MBO : Verkehrssprache = new Verkehrssprache("MBO", 282, [new VerkehrsspracheKatalogEintrag(281000, "mbo", "Mbo", null, null, null)]);

	/**
	 * Maba (mde) - ISO 639-3
	 */
	public static readonly MDE : Verkehrssprache = new Verkehrssprache("MDE", 283, [new VerkehrsspracheKatalogEintrag(282000, "mde", "Maba", null, null, null)]);

	/**
	 * Mandar (mdr)
	 */
	public static readonly MDR : Verkehrssprache = new Verkehrssprache("MDR", 284, [new VerkehrsspracheKatalogEintrag(283000, "mdr", "Mandar", null, null, null)]);

	/**
	 * Mbere (mdt) - ISO 639-3
	 */
	public static readonly MDT : Verkehrssprache = new Verkehrssprache("MDT", 285, [new VerkehrsspracheKatalogEintrag(284000, "mdt", "Mbere", null, null, null)]);

	/**
	 * Mbosi (mdw) - ISO 639-3
	 */
	public static readonly MDW : Verkehrssprache = new Verkehrssprache("MDW", 286, [new VerkehrsspracheKatalogEintrag(285000, "mdw", "Mbosi", null, null, null)]);

	/**
	 * Mende (men)
	 */
	public static readonly MEN : Verkehrssprache = new Verkehrssprache("MEN", 287, [new VerkehrsspracheKatalogEintrag(286000, "men", "Mende", null, null, null)]);

	/**
	 * Meru (mer) - ISO 639-3
	 */
	public static readonly MER : Verkehrssprache = new Verkehrssprache("MER", 288, [new VerkehrsspracheKatalogEintrag(287000, "mer", "Meru", null, null, null)]);

	/**
	 * Mano (mev) - ISO 639-3
	 */
	public static readonly MEV : Verkehrssprache = new Verkehrssprache("MEV", 289, [new VerkehrsspracheKatalogEintrag(288000, "mev", "Mano", null, null, null)]);

	/**
	 * Moba (mfq) - ISO 639-3
	 */
	public static readonly MFQ : Verkehrssprache = new Verkehrssprache("MFQ", 290, [new VerkehrsspracheKatalogEintrag(289000, "mfq", "Moba", null, null, null)]);

	/**
	 * Moru (mgd) - ISO 639-3
	 */
	public static readonly MGD : Verkehrssprache = new Verkehrssprache("MGD", 291, [new VerkehrsspracheKatalogEintrag(290000, "mgd", "Moru", null, null, null)]);

	/**
	 * Mambae (mgm) - ISO 639-3
	 */
	public static readonly MGM : Verkehrssprache = new Verkehrssprache("MGM", 292, [new VerkehrsspracheKatalogEintrag(291000, "mgm", "Mambae", null, null, null)]);

	/**
	 * Minangkabau (min)
	 */
	public static readonly MIN : Verkehrssprache = new Verkehrssprache("MIN", 293, [new VerkehrsspracheKatalogEintrag(292000, "min", "Minangkabau", null, null, null)]);

	/**
	 * Mazedonisch (mkd)
	 */
	public static readonly MKD : Verkehrssprache = new Verkehrssprache("MKD", 294, [new VerkehrsspracheKatalogEintrag(293000, "mkd", "Mazedonisch", "mk", null, null)]);

	/**
	 * Kituba (mkw) - ISO 639-3
	 */
	public static readonly MKW : Verkehrssprache = new Verkehrssprache("MKW", 295, [new VerkehrsspracheKatalogEintrag(294000, "mkw", "Kituba", null, null, null)]);

	/**
	 * Malagasy (mlg)
	 */
	public static readonly MLG : Verkehrssprache = new Verkehrssprache("MLG", 296, [new VerkehrsspracheKatalogEintrag(295000, "mlg", "Malagasy", "mg", null, null)]);

	/**
	 * Maltesisch (mlt)
	 */
	public static readonly MLT : Verkehrssprache = new Verkehrssprache("MLT", 297, [new VerkehrsspracheKatalogEintrag(296000, "mlt", "Maltesisch", "mt", null, null)]);

	/**
	 * Meitei (mni)
	 */
	public static readonly MNI : Verkehrssprache = new Verkehrssprache("MNI", 298, [new VerkehrsspracheKatalogEintrag(297000, "mni", "Meitei", null, null, null)]);

	/**
	 * Mandinka (mnk) - ISO 639-3
	 */
	public static readonly MNK : Verkehrssprache = new Verkehrssprache("MNK", 299, [new VerkehrsspracheKatalogEintrag(298000, "mnk", "Mandinka", null, null, null)]);

	/**
	 * Moldawisch (mol)
	 */
	public static readonly MOL : Verkehrssprache = new Verkehrssprache("MOL", 300, [new VerkehrsspracheKatalogEintrag(299000, "mol", "Moldawisch", "mo", null, null)]);

	/**
	 * Mongolisch (mon)
	 */
	public static readonly MON : Verkehrssprache = new Verkehrssprache("MON", 301, [new VerkehrsspracheKatalogEintrag(300000, "mon", "Mongolisch", "mn", null, null)]);

	/**
	 * Mossi (mos)
	 */
	public static readonly MOS : Verkehrssprache = new Verkehrssprache("MOS", 302, [new VerkehrsspracheKatalogEintrag(301000, "mos", "Mossi", null, null, null)]);

	/**
	 * Mpade (mpi) - ISO 639-3
	 */
	public static readonly MPI : Verkehrssprache = new Verkehrssprache("MPI", 303, [new VerkehrsspracheKatalogEintrag(302000, "mpi", "Mpade", null, null, null)]);

	/**
	 * Maori (mri)
	 */
	public static readonly MRI : Verkehrssprache = new Verkehrssprache("MRI", 304, [new VerkehrsspracheKatalogEintrag(303000, "mri", "Maori", "mi", null, null)]);

	/**
	 * Malaiisch (msa)
	 */
	public static readonly MSA : Verkehrssprache = new Verkehrssprache("MSA", 305, [new VerkehrsspracheKatalogEintrag(304000, "msa", "Malaiisch", "ms", null, null)]);

	/**
	 * Nyong (muo) - ISO 639-3
	 */
	public static readonly MUO : Verkehrssprache = new Verkehrssprache("MUO", 306, [new VerkehrsspracheKatalogEintrag(305000, "muo", "Nyong", null, null, null)]);

	/**
	 * Sar (mwm) - ISO 639-3
	 */
	public static readonly MWM : Verkehrssprache = new Verkehrssprache("MWM", 307, [new VerkehrsspracheKatalogEintrag(306000, "mwm", "Sar", null, null, null)]);

	/**
	 * Gbe, Maxi (mxl)
	 */
	public static readonly MXL : Verkehrssprache = new Verkehrssprache("MXL", 308, [new VerkehrsspracheKatalogEintrag(307000, "mxl", "Gbe, Maxi", null, null, null)]);

	/**
	 * Burmesisch (mya)
	 */
	public static readonly MYA : Verkehrssprache = new Verkehrssprache("MYA", 309, [new VerkehrsspracheKatalogEintrag(308000, "mya", "Burmesisch", "my", null, null)]);

	/**
	 * Mamara Senufo (myk) - ISO 639-3
	 */
	public static readonly MYK : Verkehrssprache = new Verkehrssprache("MYK", 310, [new VerkehrsspracheKatalogEintrag(309000, "myk", "Mamara Senufo", null, null, null)]);

	/**
	 * Masaba (myx) - ISO 639-3
	 */
	public static readonly MYX : Verkehrssprache = new Verkehrssprache("MYX", 311, [new VerkehrsspracheKatalogEintrag(310000, "myx", "Masaba", null, null, null)]);

	/**
	 * Nakanai (nak) - ISO 639-3
	 */
	public static readonly NAK : Verkehrssprache = new Verkehrssprache("NAK", 312, [new VerkehrsspracheKatalogEintrag(311000, "nak", "Nakanai", null, null, null)]);

	/**
	 * Min Nan (nan) - ISO 639-3
	 */
	public static readonly NAN : Verkehrssprache = new Verkehrssprache("NAN", 313, [new VerkehrsspracheKatalogEintrag(312000, "nan", "Min Nan", null, null, null)]);

	/**
	 * Nama (naq) - ISO 639-3
	 */
	public static readonly NAQ : Verkehrssprache = new Verkehrssprache("NAQ", 314, [new VerkehrsspracheKatalogEintrag(313000, "naq", "Nama", null, null, null)]);

	/**
	 * Nauruanisch (nau)
	 */
	public static readonly NAU : Verkehrssprache = new Verkehrssprache("NAU", 315, [new VerkehrsspracheKatalogEintrag(314000, "nau", "Nauruanisch", "na", null, null)]);

	/**
	 * Navajo (nav)
	 */
	public static readonly NAV : Verkehrssprache = new Verkehrssprache("NAV", 316, [new VerkehrsspracheKatalogEintrag(315000, "nav", "Navajo", "nv", null, null)]);

	/**
	 * Nyemba (nba) - ISO 639-3
	 */
	public static readonly NBA : Verkehrssprache = new Verkehrssprache("NBA", 317, [new VerkehrsspracheKatalogEintrag(316000, "nba", "Nyemba", null, null, null)]);

	/**
	 * Ndebele-Süd (nbl)
	 */
	public static readonly NBL : Verkehrssprache = new Verkehrssprache("NBL", 318, [new VerkehrsspracheKatalogEintrag(317000, "nbl", "Ndebele-Süd", "nr", null, null)]);

	/**
	 * Ndebele-Nord (nde)
	 */
	public static readonly NDE : Verkehrssprache = new Verkehrssprache("NDE", 319, [new VerkehrsspracheKatalogEintrag(318000, "nde", "Ndebele-Nord", "nd", null, null)]);

	/**
	 * Ndonga (ndo)
	 */
	public static readonly NDO : Verkehrssprache = new Verkehrssprache("NDO", 320, [new VerkehrsspracheKatalogEintrag(319000, "ndo", "Ndonga", "ng", null, null)]);

	/**
	 * Niederdeutsch, Plattdeutsch (nds)
	 */
	public static readonly NDS : Verkehrssprache = new Verkehrssprache("NDS", 321, [new VerkehrsspracheKatalogEintrag(319500, "nds", "Niederdeutsch, Plattdeutsch", null, null, null)]);

	/**
	 * Nepali (nep)
	 */
	public static readonly NEP : Verkehrssprache = new Verkehrssprache("NEP", 322, [new VerkehrsspracheKatalogEintrag(320000, "nep", "Nepali", "ne", null, null)]);

	/**
	 * Newari (new)
	 */
	public static readonly NEW : Verkehrssprache = new Verkehrssprache("NEW", 323, [new VerkehrsspracheKatalogEintrag(321000, "new", "Newari", null, null, null)]);

	/**
	 * Ngbandi-Nord (ngb) - ISO 639-3
	 */
	public static readonly NGB : Verkehrssprache = new Verkehrssprache("NGB", 324, [new VerkehrsspracheKatalogEintrag(322000, "ngb", "Ngbandi-Nord", null, null, null)]);

	/**
	 * Ngemba (nge) - ISO 639-3
	 */
	public static readonly NGE : Verkehrssprache = new Verkehrssprache("NGE", 325, [new VerkehrsspracheKatalogEintrag(323000, "nge", "Ngemba", null, null, null)]);

	/**
	 * Nias (nia)
	 */
	public static readonly NIA : Verkehrssprache = new Verkehrssprache("NIA", 326, [new VerkehrsspracheKatalogEintrag(324000, "nia", "Nias", null, null, null)]);

	/**
	 * Nkonya (nko) - ISO 639-3
	 */
	public static readonly NKO : Verkehrssprache = new Verkehrssprache("NKO", 327, [new VerkehrsspracheKatalogEintrag(325000, "nko", "Nkonya", null, null, null)]);

	/**
	 * Kulango Bouna (nku) - ISO 639-3
	 */
	public static readonly NKU : Verkehrssprache = new Verkehrssprache("NKU", 328, [new VerkehrsspracheKatalogEintrag(326000, "nku", "Kulango Bouna", null, null, null)]);

	/**
	 * Niederländisch (nld)
	 */
	public static readonly NLD : Verkehrssprache = new Verkehrssprache("NLD", 329, [new VerkehrsspracheKatalogEintrag(327000, "nld", "Niederländisch", "nl", null, null)]);

	/**
	 * Nda'nda' (nnz)
	 */
	public static readonly NNZ : Verkehrssprache = new Verkehrssprache("NNZ", 330, [new VerkehrsspracheKatalogEintrag(328000, "nnz", "Nda'nda'", null, null, null)]);

	/**
	 * Norwegisch (nor)
	 */
	public static readonly NOR : Verkehrssprache = new Verkehrssprache("NOR", 331, [new VerkehrsspracheKatalogEintrag(329000, "nor", "Norwegisch", "no", null, null)]);

	/**
	 * Ede Nago (nqg)
	 */
	public static readonly NQG : Verkehrssprache = new Verkehrssprache("NQG", 332, [new VerkehrsspracheKatalogEintrag(330000, "nqg", "Ede Nago", null, null, null)]);

	/**
	 * Nord-Sotho (nso)
	 */
	public static readonly NSO : Verkehrssprache = new Verkehrssprache("NSO", 333, [new VerkehrsspracheKatalogEintrag(331000, "nso", "Nord-Sotho", null, null, null)]);

	/**
	 * Nuer (nus) - ISO 639-3
	 */
	public static readonly NUS : Verkehrssprache = new Verkehrssprache("NUS", 334, [new VerkehrsspracheKatalogEintrag(332000, "nus", "Nuer", null, null, null)]);

	/**
	 * Nyanja (nya)
	 */
	public static readonly NYA : Verkehrssprache = new Verkehrssprache("NYA", 335, [new VerkehrsspracheKatalogEintrag(333000, "nya", "Nyanja", "ny", null, null)]);

	/**
	 * Nyamwezi (nym)
	 */
	public static readonly NYM : Verkehrssprache = new Verkehrssprache("NYM", 336, [new VerkehrsspracheKatalogEintrag(334000, "nym", "Nyamwezi", null, null, null)]);

	/**
	 * Nyankole (nyn)
	 */
	public static readonly NYN : Verkehrssprache = new Verkehrssprache("NYN", 337, [new VerkehrsspracheKatalogEintrag(335000, "nyn", "Nyankole", null, null, null)]);

	/**
	 * Nyoro (nyo)
	 */
	public static readonly NYO : Verkehrssprache = new Verkehrssprache("NYO", 338, [new VerkehrsspracheKatalogEintrag(336000, "nyo", "Nyoro", null, null, null)]);

	/**
	 * Nzima (nzi)
	 */
	public static readonly NZI : Verkehrssprache = new Verkehrssprache("NZI", 339, [new VerkehrsspracheKatalogEintrag(337000, "nzi", "Nzima", null, null, null)]);

	/**
	 * Okzitanisch (oci)
	 */
	public static readonly OCI : Verkehrssprache = new Verkehrssprache("OCI", 340, [new VerkehrsspracheKatalogEintrag(338000, "oci", "Okzitanisch", "oc", null, null)]);

	/**
	 * Khana (ogo)
	 */
	public static readonly OGO : Verkehrssprache = new Verkehrssprache("OGO", 341, [new VerkehrsspracheKatalogEintrag(339000, "ogo", "Khana", null, null, null)]);

	/**
	 * Ojibwe (oji)
	 */
	public static readonly OJI : Verkehrssprache = new Verkehrssprache("OJI", 342, [new VerkehrsspracheKatalogEintrag(340000, "oji", "Ojibwe", "oj", null, null)]);

	/**
	 * Ombo (oml) - ISO 639-3
	 */
	public static readonly OML : Verkehrssprache = new Verkehrssprache("OML", 343, [new VerkehrsspracheKatalogEintrag(341000, "oml", "Ombo", null, null, null)]);

	/**
	 * Oriya (ori)
	 */
	public static readonly ORI : Verkehrssprache = new Verkehrssprache("ORI", 344, [new VerkehrsspracheKatalogEintrag(342000, "ori", "Oriya", "or", null, null)]);

	/**
	 * Oromo (orm)
	 */
	public static readonly ORM : Verkehrssprache = new Verkehrssprache("ORM", 345, [new VerkehrsspracheKatalogEintrag(343000, "orm", "Oromo", "om", null, null)]);

	/**
	 * Ossetisch (oss)
	 */
	public static readonly OSS : Verkehrssprache = new Verkehrssprache("OSS", 346, [new VerkehrsspracheKatalogEintrag(344000, "oss", "Ossetisch", "os", null, null)]);

	/**
	 * Panjabi (pan)
	 */
	public static readonly PAN : Verkehrssprache = new Verkehrssprache("PAN", 347, [new VerkehrsspracheKatalogEintrag(345000, "pan", "Panjabi", "pa", null, null)]);

	/**
	 * Palauisch (pau)
	 */
	public static readonly PAU : Verkehrssprache = new Verkehrssprache("PAU", 348, [new VerkehrsspracheKatalogEintrag(346000, "pau", "Palauisch", null, null, null)]);

	/**
	 * Phende (pem) - ISO 639-3
	 */
	public static readonly PEM : Verkehrssprache = new Verkehrssprache("PEM", 349, [new VerkehrsspracheKatalogEintrag(347000, "pem", "Phende", null, null, null)]);

	/**
	 * Persisch (pes) - ISO 639-3
	 */
	public static readonly PES : Verkehrssprache = new Verkehrssprache("PES", 350, [new VerkehrsspracheKatalogEintrag(348000, "pes", "Persisch", "fa", null, null)]);

	/**
	 * Pali (pli)
	 */
	public static readonly PLI : Verkehrssprache = new Verkehrssprache("PLI", 351, [new VerkehrsspracheKatalogEintrag(349000, "pli", "Pali", null, null, null)]);

	/**
	 * Polnisch (pol)
	 */
	public static readonly POL : Verkehrssprache = new Verkehrssprache("POL", 352, [new VerkehrsspracheKatalogEintrag(350000, "pol", "Polnisch", "pl", null, null)]);

	/**
	 * Portugisisch (por)
	 */
	public static readonly POR : Verkehrssprache = new Verkehrssprache("POR", 353, [new VerkehrsspracheKatalogEintrag(351000, "por", "Portugisisch", "pt", null, null)]);

	/**
	 * Dari (prs) - ISO 639-3
	 */
	public static readonly PRS : Verkehrssprache = new Verkehrssprache("PRS", 354, [new VerkehrsspracheKatalogEintrag(352000, "prs", "Dari", null, null, null)]);

	/**
	 * Paschto (pus)
	 */
	public static readonly PUS : Verkehrssprache = new Verkehrssprache("PUS", 355, [new VerkehrsspracheKatalogEintrag(353000, "pus", "Paschto", "ps", null, null)]);

	/**
	 * Quechua (que)
	 */
	public static readonly QUE : Verkehrssprache = new Verkehrssprache("QUE", 356, [new VerkehrsspracheKatalogEintrag(354000, "que", "Quechua", "qu", null, null)]);

	/**
	 * Rajasthani (raj)
	 */
	public static readonly RAJ : Verkehrssprache = new Verkehrssprache("RAJ", 357, [new VerkehrsspracheKatalogEintrag(355000, "raj", "Rajasthani", null, null, null)]);

	/**
	 * Ruund (rnd) - ISO 639-3
	 */
	public static readonly RND : Verkehrssprache = new Verkehrssprache("RND", 358, [new VerkehrsspracheKatalogEintrag(356000, "rnd", "Ruund", null, null, null)]);

	/**
	 * Rätoromanisch (roh)
	 */
	public static readonly ROH : Verkehrssprache = new Verkehrssprache("ROH", 359, [new VerkehrsspracheKatalogEintrag(357000, "roh", "Rätoromanisch", "rm", null, null)]);

	/**
	 * Romani (rom)
	 */
	public static readonly ROM : Verkehrssprache = new Verkehrssprache("ROM", 360, [new VerkehrsspracheKatalogEintrag(358000, "rom", "Romani", null, null, null)]);

	/**
	 * Rumänisch (ron)
	 */
	public static readonly RON : Verkehrssprache = new Verkehrssprache("RON", 361, [new VerkehrsspracheKatalogEintrag(359000, "ron", "Rumänisch", "ro", null, null)]);

	/**
	 * Rundi (run)
	 */
	public static readonly RUN : Verkehrssprache = new Verkehrssprache("RUN", 362, [new VerkehrsspracheKatalogEintrag(360000, "run", "Rundi", null, null, null)]);

	/**
	 * Russisch (rus)
	 */
	public static readonly RUS : Verkehrssprache = new Verkehrssprache("RUS", 363, [new VerkehrsspracheKatalogEintrag(361000, "rus", "Russisch", "ru", null, null)]);

	/**
	 * Sandawe (sad)
	 */
	public static readonly SAD : Verkehrssprache = new Verkehrssprache("SAD", 364, [new VerkehrsspracheKatalogEintrag(362000, "sad", "Sandawe", null, null, null)]);

	/**
	 * Sango (sag)
	 */
	public static readonly SAG : Verkehrssprache = new Verkehrssprache("SAG", 365, [new VerkehrsspracheKatalogEintrag(363000, "sag", "Sango", "sg", null, null)]);

	/**
	 * Jakutisch (sah)
	 */
	public static readonly SAH : Verkehrssprache = new Verkehrssprache("SAH", 366, [new VerkehrsspracheKatalogEintrag(364000, "sah", "Jakutisch", null, null, null)]);

	/**
	 * Sanskrit (san)
	 */
	public static readonly SAN : Verkehrssprache = new Verkehrssprache("SAN", 367, [new VerkehrsspracheKatalogEintrag(365000, "san", "Sanskrit", null, null, null)]);

	/**
	 * Sasak (sas)
	 */
	public static readonly SAS : Verkehrssprache = new Verkehrssprache("SAS", 368, [new VerkehrsspracheKatalogEintrag(366000, "sas", "Sasak", null, null, null)]);

	/**
	 * Santali (sat)
	 */
	public static readonly SAT : Verkehrssprache = new Verkehrssprache("SAT", 369, [new VerkehrsspracheKatalogEintrag(367000, "sat", "Santali", null, null, null)]);

	/**
	 * Sangu (sbp) - ISO 639-3
	 */
	public static readonly SBP : Verkehrssprache = new Verkehrssprache("SBP", 370, [new VerkehrsspracheKatalogEintrag(368000, "sbp", "Sangu", null, null, null)]);

	/**
	 * Sehwi (sfw) - ISO 639-3
	 */
	public static readonly SFW : Verkehrssprache = new Verkehrssprache("SFW", 371, [new VerkehrsspracheKatalogEintrag(369000, "sfw", "Sehwi", null, null, null)]);

	/**
	 * Sebat Bet Gurage (sgw) - ISO 639-3
	 */
	public static readonly SGW : Verkehrssprache = new Verkehrssprache("SGW", 372, [new VerkehrsspracheKatalogEintrag(370000, "sgw", "Sebat Bet Gurage", null, null, null)]);

	/**
	 * Taschelhit (shi) - ISO 639-3
	 */
	public static readonly SHI : Verkehrssprache = new Verkehrssprache("SHI", 373, [new VerkehrsspracheKatalogEintrag(371000, "shi", "Taschelhit", null, null, null)]);

	/**
	 * Shilluk (shk) - ISO 639-3
	 */
	public static readonly SHK : Verkehrssprache = new Verkehrssprache("SHK", 374, [new VerkehrsspracheKatalogEintrag(372000, "shk", "Shilluk", null, null, null)]);

	/**
	 * Schan (shn)
	 */
	public static readonly SHN : Verkehrssprache = new Verkehrssprache("SHN", 375, [new VerkehrsspracheKatalogEintrag(373000, "shn", "Schan", null, null, null)]);

	/**
	 * Sidamo (sid)
	 */
	public static readonly SID : Verkehrssprache = new Verkehrssprache("SID", 376, [new VerkehrsspracheKatalogEintrag(374000, "sid", "Sidamo", null, null, null)]);

	/**
	 * Singhalesisch (sin)
	 */
	public static readonly SIN : Verkehrssprache = new Verkehrssprache("SIN", 377, [new VerkehrsspracheKatalogEintrag(375000, "sin", "Singhalesisch", "si", null, null)]);

	/**
	 * Sakata (skt) - ISO 639-3
	 */
	public static readonly SKT : Verkehrssprache = new Verkehrssprache("SKT", 378, [new VerkehrsspracheKatalogEintrag(376000, "skt", "Sakata", null, null, null)]);

	/**
	 * Slowakisch (slk)
	 */
	public static readonly SLK : Verkehrssprache = new Verkehrssprache("SLK", 379, [new VerkehrsspracheKatalogEintrag(377000, "slk", "Slowakisch", "sk", null, null)]);

	/**
	 * Lamaholot (slp) - ISO 639-3
	 */
	public static readonly SLP : Verkehrssprache = new Verkehrssprache("SLP", 380, [new VerkehrsspracheKatalogEintrag(378000, "slp", "Lamaholot", null, null, null)]);

	/**
	 * Slowenisch (slv)
	 */
	public static readonly SLV : Verkehrssprache = new Verkehrssprache("SLV", 381, [new VerkehrsspracheKatalogEintrag(379000, "slv", "Slowenisch", "sl", null, null)]);

	/**
	 * Nordsamisch (sme)
	 */
	public static readonly SME : Verkehrssprache = new Verkehrssprache("SME", 382, [new VerkehrsspracheKatalogEintrag(380000, "sme", "Nordsamisch", "se", null, null)]);

	/**
	 * Samoanisch (smo)
	 */
	public static readonly SMO : Verkehrssprache = new Verkehrssprache("SMO", 383, [new VerkehrsspracheKatalogEintrag(381000, "smo", "Samoanisch", "sm", null, null)]);

	/**
	 * Shona (sna)
	 */
	public static readonly SNA : Verkehrssprache = new Verkehrssprache("SNA", 384, [new VerkehrsspracheKatalogEintrag(382000, "sna", "Shona", "sn", null, null)]);

	/**
	 * Sindhi (snd)
	 */
	public static readonly SND : Verkehrssprache = new Verkehrssprache("SND", 385, [new VerkehrsspracheKatalogEintrag(383000, "snd", "Sindhi", "sd", null, null)]);

	/**
	 * Soninke (snk)
	 */
	public static readonly SNK : Verkehrssprache = new Verkehrssprache("SNK", 386, [new VerkehrsspracheKatalogEintrag(384000, "snk", "Soninke", null, null, null)]);

	/**
	 * Selee (snw) - ISO 639-3
	 */
	public static readonly SNW : Verkehrssprache = new Verkehrssprache("SNW", 387, [new VerkehrsspracheKatalogEintrag(385000, "snw", "Selee", null, null, null)]);

	/**
	 * Somali (som)
	 */
	public static readonly SOM : Verkehrssprache = new Verkehrssprache("SOM", 388, [new VerkehrsspracheKatalogEintrag(386000, "som", "Somali", "so", null, null)]);

	/**
	 * Songhai (son)
	 */
	public static readonly SON : Verkehrssprache = new Verkehrssprache("SON", 389, [new VerkehrsspracheKatalogEintrag(387000, "son", "Songhai", null, null, null)]);

	/**
	 * Songe (sop) - ISO 639-3
	 */
	public static readonly SOP : Verkehrssprache = new Verkehrssprache("SOP", 390, [new VerkehrsspracheKatalogEintrag(388000, "sop", "Songe", null, null, null)]);

	/**
	 * Sotho-Süd (sot)
	 */
	public static readonly SOT : Verkehrssprache = new Verkehrssprache("SOT", 391, [new VerkehrsspracheKatalogEintrag(389000, "sot", "Sotho-Süd", "st", null, null)]);

	/**
	 * Spanisch (spa)
	 */
	public static readonly SPA : Verkehrssprache = new Verkehrssprache("SPA", 392, [new VerkehrsspracheKatalogEintrag(390000, "spa", "Spanisch", "es", null, null)]);

	/**
	 * Albanisch (sqi)
	 */
	public static readonly SQI : Verkehrssprache = new Verkehrssprache("SQI", 393, [new VerkehrsspracheKatalogEintrag(391000, "sqi", "Albanisch", "sq", null, null)]);

	/**
	 * Sardisch (srd)
	 */
	public static readonly SRD : Verkehrssprache = new Verkehrssprache("SRD", 394, [new VerkehrsspracheKatalogEintrag(392000, "srd", "Sardisch", "sc", null, null)]);

	/**
	 * Serbisch (srp)
	 */
	public static readonly SRP : Verkehrssprache = new Verkehrssprache("SRP", 395, [new VerkehrsspracheKatalogEintrag(393000, "srp", "Serbisch", "sr", null, null)]);

	/**
	 * Serer (srr)
	 */
	public static readonly SRR : Verkehrssprache = new Verkehrssprache("SRR", 396, [new VerkehrsspracheKatalogEintrag(394000, "srr", "Serer", null, null, null)]);

	/**
	 * Swati (ssw)
	 */
	public static readonly SSW : Verkehrssprache = new Verkehrssprache("SSW", 397, [new VerkehrsspracheKatalogEintrag(395000, "ssw", "Swati", "ss", null, null)]);

	/**
	 * Saho (ssy) - ISO 639-3
	 */
	public static readonly SSY : Verkehrssprache = new Verkehrssprache("SSY", 398, [new VerkehrsspracheKatalogEintrag(396000, "ssy", "Saho", null, null, null)]);

	/**
	 * Sukuma (suk)
	 */
	public static readonly SUK : Verkehrssprache = new Verkehrssprache("SUK", 399, [new VerkehrsspracheKatalogEintrag(397000, "suk", "Sukuma", null, null, null)]);

	/**
	 * Sundanesisch (sun)
	 */
	public static readonly SUN : Verkehrssprache = new Verkehrssprache("SUN", 400, [new VerkehrsspracheKatalogEintrag(398000, "sun", "Sundanesisch", "su", null, null)]);

	/**
	 * Susu (sus)
	 */
	public static readonly SUS : Verkehrssprache = new Verkehrssprache("SUS", 401, [new VerkehrsspracheKatalogEintrag(399000, "sus", "Susu", null, null, null)]);

	/**
	 * Swahili (swa)
	 */
	public static readonly SWA : Verkehrssprache = new Verkehrssprache("SWA", 402, [new VerkehrsspracheKatalogEintrag(400000, "swa", "Swahili", "sw", null, null)]);

	/**
	 * Schwedisch (swe)
	 */
	public static readonly SWE : Verkehrssprache = new Verkehrssprache("SWE", 403, [new VerkehrsspracheKatalogEintrag(401000, "swe", "Schwedisch", "sv", null, null)]);

	/**
	 * Sere (swf) - ISO 639-3
	 */
	public static readonly SWF : Verkehrssprache = new Verkehrssprache("SWF", 404, [new VerkehrsspracheKatalogEintrag(402000, "swf", "Sere", null, null, null)]);

	/**
	 * Seki (syi) - ISO 639-3
	 */
	public static readonly SYI : Verkehrssprache = new Verkehrssprache("SYI", 405, [new VerkehrsspracheKatalogEintrag(403000, "syi", "Seki", null, null, null)]);

	/**
	 * Tahitianisch (tah)
	 */
	public static readonly TAH : Verkehrssprache = new Verkehrssprache("TAH", 406, [new VerkehrsspracheKatalogEintrag(404000, "tah", "Tahitianisch", "ty", null, null)]);

	/**
	 * Tamil (tam)
	 */
	public static readonly TAM : Verkehrssprache = new Verkehrssprache("TAM", 407, [new VerkehrsspracheKatalogEintrag(405000, "tam", "Tamil", "ta", null, null)]);

	/**
	 * Tatarisch (tat)
	 */
	public static readonly TAT : Verkehrssprache = new Verkehrssprache("TAT", 408, [new VerkehrsspracheKatalogEintrag(406000, "tat", "Tatarisch", "tt", null, null)]);

	/**
	 * Teke-Tege (teg) - ISO 639-3
	 */
	public static readonly TEG : Verkehrssprache = new Verkehrssprache("TEG", 409, [new VerkehrsspracheKatalogEintrag(407000, "teg", "Teke-Tege", null, null, null)]);

	/**
	 * Telugu (tel)
	 */
	public static readonly TEL : Verkehrssprache = new Verkehrssprache("TEL", 410, [new VerkehrsspracheKatalogEintrag(408000, "tel", "Telugu", "te", null, null)]);

	/**
	 * Temne (tem)
	 */
	public static readonly TEM : Verkehrssprache = new Verkehrssprache("TEM", 411, [new VerkehrsspracheKatalogEintrag(409000, "tem", "Temne", null, null, null)]);

	/**
	 * Tetum (tet)
	 */
	public static readonly TET : Verkehrssprache = new Verkehrssprache("TET", 412, [new VerkehrsspracheKatalogEintrag(410000, "tet", "Tetum", null, null, null)]);

	/**
	 * Tadschikisch (tgk)
	 */
	public static readonly TGK : Verkehrssprache = new Verkehrssprache("TGK", 413, [new VerkehrsspracheKatalogEintrag(411000, "tgk", "Tadschikisch", "tg", null, null)]);

	/**
	 * Tagalog (tgl)
	 */
	public static readonly TGL : Verkehrssprache = new Verkehrssprache("TGL", 414, [new VerkehrsspracheKatalogEintrag(412000, "tgl", "Tagalog", "tl", null, null)]);

	/**
	 * Thailändisch (tha)
	 */
	public static readonly THA : Verkehrssprache = new Verkehrssprache("THA", 415, [new VerkehrsspracheKatalogEintrag(413000, "tha", "Thailändisch", "th", null, null)]);

	/**
	 * Thuri (thu) - ISO 639-3
	 */
	public static readonly THU : Verkehrssprache = new Verkehrssprache("THU", 416, [new VerkehrsspracheKatalogEintrag(414000, "thu", "Thuri", null, null, null)]);

	/**
	 * Tigre (tig)
	 */
	public static readonly TIG : Verkehrssprache = new Verkehrssprache("TIG", 417, [new VerkehrsspracheKatalogEintrag(415000, "tig", "Tigre", null, null, null)]);

	/**
	 * Tigrinya (tir)
	 */
	public static readonly TIR : Verkehrssprache = new Verkehrssprache("TIR", 418, [new VerkehrsspracheKatalogEintrag(416000, "tir", "Tigrinya", "ti", null, null)]);

	/**
	 * Tiv (tiv)
	 */
	public static readonly TIV : Verkehrssprache = new Verkehrssprache("TIV", 419, [new VerkehrsspracheKatalogEintrag(417000, "tiv", "Tiv", null, null, null)]);

	/**
	 * Tetela (tll) - ISO 639-3
	 */
	public static readonly TLL : Verkehrssprache = new Verkehrssprache("TLL", 420, [new VerkehrsspracheKatalogEintrag(418000, "tll", "Tetela", null, null, null)]);

	/**
	 * Tamascheq (tmh)
	 */
	public static readonly TMH : Verkehrssprache = new Verkehrssprache("TMH", 421, [new VerkehrsspracheKatalogEintrag(419000, "tmh", "Tamascheq", null, null, null)]);

	/**
	 * Tonga (tog)
	 */
	public static readonly TOG : Verkehrssprache = new Verkehrssprache("TOG", 422, [new VerkehrsspracheKatalogEintrag(420000, "tog", "Tonga", "to", null, null)]);

	/**
	 * Tonga (toi) - ISO 639-3
	 */
	public static readonly TOI : Verkehrssprache = new Verkehrssprache("TOI", 423, [new VerkehrsspracheKatalogEintrag(421000, "toi", "Tonga", "to", null, null)]);

	/**
	 * Tongaisch (ton)
	 */
	public static readonly TON : Verkehrssprache = new Verkehrssprache("TON", 424, [new VerkehrsspracheKatalogEintrag(422000, "ton", "Tongaisch", "to", null, null)]);

	/**
	 * Tok Pisin (tpi)
	 */
	public static readonly TPI : Verkehrssprache = new Verkehrssprache("TPI", 425, [new VerkehrsspracheKatalogEintrag(423000, "tpi", "Tok Pisin", null, null, null)]);

	/**
	 * Tswana (tsn)
	 */
	public static readonly TSN : Verkehrssprache = new Verkehrssprache("TSN", 426, [new VerkehrsspracheKatalogEintrag(424000, "tsn", "Tswana", "tn", null, null)]);

	/**
	 * Tsonga (tso)
	 */
	public static readonly TSO : Verkehrssprache = new Verkehrssprache("TSO", 427, [new VerkehrsspracheKatalogEintrag(425000, "tso", "Tsonga", "ts", null, null)]);

	/**
	 * Turkmenisch (tuk)
	 */
	public static readonly TUK : Verkehrssprache = new Verkehrssprache("TUK", 428, [new VerkehrsspracheKatalogEintrag(426000, "tuk", "Turkmenisch", "tk", null, null)]);

	/**
	 * ChiTumbuka (tum)
	 */
	public static readonly TUM : Verkehrssprache = new Verkehrssprache("TUM", 429, [new VerkehrsspracheKatalogEintrag(427000, "tum", "ChiTumbuka", null, null, null)]);

	/**
	 * Türkisch (tur)
	 */
	public static readonly TUR : Verkehrssprache = new Verkehrssprache("TUR", 430, [new VerkehrsspracheKatalogEintrag(428000, "tur", "Türkisch", "tr", null, null)]);

	/**
	 * Altaisch (tut)
	 */
	public static readonly TUT : Verkehrssprache = new Verkehrssprache("TUT", 431, [new VerkehrsspracheKatalogEintrag(429000, "tut", "Altaisch", null, null, null)]);

	/**
	 * Tuvaluisch (tvl)
	 */
	public static readonly TVL : Verkehrssprache = new Verkehrssprache("TVL", 432, [new VerkehrsspracheKatalogEintrag(430000, "tvl", "Tuvaluisch", null, null, null)]);

	/**
	 * Twi (twi)
	 */
	public static readonly TWI : Verkehrssprache = new Verkehrssprache("TWI", 433, [new VerkehrsspracheKatalogEintrag(431000, "twi", "Twi", "tw", null, null)]);

	/**
	 * Tuwinisch (tyv)
	 */
	public static readonly TYV : Verkehrssprache = new Verkehrssprache("TYV", 434, [new VerkehrsspracheKatalogEintrag(432000, "tyv", "Tuwinisch", null, null, null)]);

	/**
	 * Udisch (udi) - ISO 639-3
	 */
	public static readonly UDI : Verkehrssprache = new Verkehrssprache("UDI", 435, [new VerkehrsspracheKatalogEintrag(433000, "udi", "Udisch", null, null, null)]);

	/**
	 * Uigurisch (uig)
	 */
	public static readonly UIG : Verkehrssprache = new Verkehrssprache("UIG", 436, [new VerkehrsspracheKatalogEintrag(434000, "uig", "Uigurisch", "ug", null, null)]);

	/**
	 * Ukrainisch (ukr)
	 */
	public static readonly UKR : Verkehrssprache = new Verkehrssprache("UKR", 437, [new VerkehrsspracheKatalogEintrag(435000, "ukr", "Ukrainisch", "uk", null, null)]);

	/**
	 * Unbekannt (und)
	 */
	public static readonly UND : Verkehrssprache = new Verkehrssprache("UND", 438, [new VerkehrsspracheKatalogEintrag(436000, "und", "Unbekannt", "xx", null, null)]);

	/**
	 * Urdu (urd)
	 */
	public static readonly URD : Verkehrssprache = new Verkehrssprache("URD", 439, [new VerkehrsspracheKatalogEintrag(437000, "urd", "Urdu", "ur", null, null)]);

	/**
	 * Urhobo (urh) - ISO 639-3
	 */
	public static readonly URH : Verkehrssprache = new Verkehrssprache("URH", 440, [new VerkehrsspracheKatalogEintrag(438000, "urh", "Urhobo", null, null, null)]);

	/**
	 * Usbekisch (uzb)
	 */
	public static readonly UZB : Verkehrssprache = new Verkehrssprache("UZB", 441, [new VerkehrsspracheKatalogEintrag(439000, "uzb", "Usbekisch", "uz", null, null)]);

	/**
	 * Vai (vai)
	 */
	public static readonly VAI : Verkehrssprache = new Verkehrssprache("VAI", 442, [new VerkehrsspracheKatalogEintrag(440000, "vai", "Vai", null, null, null)]);

	/**
	 * Venda (ven)
	 */
	public static readonly VEN : Verkehrssprache = new Verkehrssprache("VEN", 443, [new VerkehrsspracheKatalogEintrag(441000, "ven", "Venda", "ve", null, null)]);

	/**
	 * Vietnamesisch (vie)
	 */
	public static readonly VIE : Verkehrssprache = new Verkehrssprache("VIE", 444, [new VerkehrsspracheKatalogEintrag(442000, "vie", "Vietnamesisch", "vi", null, null)]);

	/**
	 * Makhuwa (vmw) - ISO 639-3
	 */
	public static readonly VMW : Verkehrssprache = new Verkehrssprache("VMW", 445, [new VerkehrsspracheKatalogEintrag(443000, "vmw", "Makhuwa", null, null, null)]);

	/**
	 * Wotisch (vot)
	 */
	public static readonly VOT : Verkehrssprache = new Verkehrssprache("VOT", 446, [new VerkehrsspracheKatalogEintrag(444000, "vot", "Wotisch", null, null, null)]);

	/**
	 * Wolaytta (wal)
	 */
	public static readonly WAL : Verkehrssprache = new Verkehrssprache("WAL", 447, [new VerkehrsspracheKatalogEintrag(445000, "wal", "Wolaytta", null, null, null)]);

	/**
	 * Gbe, Waci (wci) - ISO 639-3
	 */
	public static readonly WCI : Verkehrssprache = new Verkehrssprache("WCI", 448, [new VerkehrsspracheKatalogEintrag(446000, "wci", "Gbe, Waci", null, null, null)]);

	/**
	 * Wallonisch (wln)
	 */
	public static readonly WLN : Verkehrssprache = new Verkehrssprache("WLN", 449, [new VerkehrsspracheKatalogEintrag(447000, "wln", "Wallonisch", "wa", null, null)]);

	/**
	 * Wolof (wol)
	 */
	public static readonly WOL : Verkehrssprache = new Verkehrssprache("WOL", 450, [new VerkehrsspracheKatalogEintrag(448000, "wol", "Wolof", "wo", null, null)]);

	/**
	 * Wu-Chinesisch (wuu) - ISO 639-3
	 */
	public static readonly WUU : Verkehrssprache = new Verkehrssprache("WUU", 451, [new VerkehrsspracheKatalogEintrag(449000, "wuu", "Wu-Chinesisch", null, null, null)]);

	/**
	 * Kalmückisch (xal)
	 */
	public static readonly XAL : Verkehrssprache = new Verkehrssprache("XAL", 452, [new VerkehrsspracheKatalogEintrag(450000, "xal", "Kalmückisch", null, null, null)]);

	/**
	 * Xhosa (xho)
	 */
	public static readonly XHO : Verkehrssprache = new Verkehrssprache("XHO", 453, [new VerkehrsspracheKatalogEintrag(451000, "xho", "Xhosa", "xh", null, null)]);

	/**
	 * Soga (xog) - ISO 639-3
	 */
	public static readonly XOG : Verkehrssprache = new Verkehrssprache("XOG", 454, [new VerkehrsspracheKatalogEintrag(452000, "xog", "Soga", null, null, null)]);

	/**
	 * Konkomba (xon) - ISO 639-3
	 */
	public static readonly XON : Verkehrssprache = new Verkehrssprache("XON", 455, [new VerkehrsspracheKatalogEintrag(453000, "xon", "Konkomba", null, null, null)]);

	/**
	 * Jalonke (yal) - ISO 639-3
	 */
	public static readonly YAL : Verkehrssprache = new Verkehrssprache("YAL", 456, [new VerkehrsspracheKatalogEintrag(454000, "yal", "Jalonke", null, null, null)]);

	/**
	 * Yao (yao)
	 */
	public static readonly YAO : Verkehrssprache = new Verkehrssprache("YAO", 457, [new VerkehrsspracheKatalogEintrag(455000, "yao", "Yao", null, null, null)]);

	/**
	 * Jiddisch (yid)
	 */
	public static readonly YID : Verkehrssprache = new Verkehrssprache("YID", 458, [new VerkehrsspracheKatalogEintrag(456000, "yid", "Jiddisch", "yi", null, null)]);

	/**
	 * Yansi (yns) - ISO 639-3
	 */
	public static readonly YNS : Verkehrssprache = new Verkehrssprache("YNS", 459, [new VerkehrsspracheKatalogEintrag(457000, "yns", "Yansi", null, null, null)]);

	/**
	 * Yombe (yom) - ISO 639-3
	 */
	public static readonly YOM : Verkehrssprache = new Verkehrssprache("YOM", 460, [new VerkehrsspracheKatalogEintrag(458000, "yom", "Yombe", null, null, null)]);

	/**
	 * Yoruba (yor)
	 */
	public static readonly YOR : Verkehrssprache = new Verkehrssprache("YOR", 461, [new VerkehrsspracheKatalogEintrag(459000, "yor", "Yoruba", "yo", null, null)]);

	/**
	 * Kantonesisch (yue) - ISO 639-3
	 */
	public static readonly YUE : Verkehrssprache = new Verkehrssprache("YUE", 462, [new VerkehrsspracheKatalogEintrag(460000, "yue", "Kantonesisch", null, null, null)]);

	/**
	 * Zhuang (zha)
	 */
	public static readonly ZHA : Verkehrssprache = new Verkehrssprache("ZHA", 463, [new VerkehrsspracheKatalogEintrag(461000, "zha", "Zhuang", "za", null, null)]);

	/**
	 * Chinesisch (zho)
	 */
	public static readonly ZHO : Verkehrssprache = new Verkehrssprache("ZHO", 464, [new VerkehrsspracheKatalogEintrag(462000, "zho", "Chinesisch", "zh", null, null)]);

	/**
	 * Zande (zne) - ISO 639-3
	 */
	public static readonly ZNE : Verkehrssprache = new Verkehrssprache("ZNE", 465, [new VerkehrsspracheKatalogEintrag(463000, "zne", "Zande", null, null, null)]);

	/**
	 * Zulu (zul)
	 */
	public static readonly ZUL : Verkehrssprache = new Verkehrssprache("ZUL", 466, [new VerkehrsspracheKatalogEintrag(464000, "zul", "Zulu", "zu", null, null)]);

	/**
	 * Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können.
	 */
	public static VERSION : number = 1;

	/**
	 * Der aktuellen Daten der Verkehrssprache, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null
	 */
	public readonly daten : VerkehrsspracheKatalogEintrag;

	/**
	 * Die Historie mit den Einträgen der Verkehrssprache
	 */
	public readonly historie : Array<VerkehrsspracheKatalogEintrag>;

	/**
	 * Eine Hashmap mit allen definierten Verkehrssprachen, zugeordnet zu ihren Kürzeln
	 */
	private static readonly _sprachen : HashMap<string, Verkehrssprache> = new HashMap();

	/**
	 * Eine Hashmap mit allen definierten Verkehrssprachen, zugeordnet zu ihren zweistelligen ISO 639-1-Codes
	 */
	private static readonly _kuerzel2 : HashMap<string, Verkehrssprache> = new HashMap();

	/**
	 * Erzeugt eine neue Verkehrssprache in der Aufzählung.
	 *
	 * @param historie   die Historie der Verkehrssprache, welches ein Array von {@link VerkehrsspracheKatalogEintrag} ist
	 */
	private constructor(name : string, ordinal : number, historie : Array<VerkehrsspracheKatalogEintrag>) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		Verkehrssprache.all_values_by_ordinal.push(this);
		Verkehrssprache.all_values_by_name.set(name, this);
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	/**
	 * Gibt eine Map von den Kürzeln der Verkehrssprachen auf die zugehörigen Verkehrssprachen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Verkehrssprache auf die zugehörigen Verkehrssprache
	 */
	private static getMapSpracheByKuerzel() : HashMap<string, Verkehrssprache> {
		if (Verkehrssprache._sprachen.size() === 0) {
			for (let s of Verkehrssprache.values()) {
				if (s.daten !== null)
					Verkehrssprache._sprachen.put(s.daten.kuerzel, s);
			}
		}
		return Verkehrssprache._sprachen;
	}

	/**
	 * Gibt eine Map von den zweistelligen Kürzeln der Verkehrssprachen auf die zugehörigen Verkehrssprachen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den zweistelligen Kürzeln der Verkehrssprache auf die zugehörigen Verkehrssprache
	 */
	private static getMapSpracheByKuerzel2() : HashMap<string, Verkehrssprache> {
		if (Verkehrssprache._kuerzel2.size() === 0) {
			for (let s of Verkehrssprache.values()) {
				if ((s.daten !== null) && (s.daten.iso2 !== null))
					Verkehrssprache._kuerzel2.put(s.daten.iso2, s);
			}
		}
		return Verkehrssprache._kuerzel2;
	}

	/**
	 * Gibt die Verkehrssprache für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel der Verkehrssprache
	 *
	 * @return die Verkehrssprache oder null, falls das Kürzel unbekannt ist
	 */
	public static getByKuerzel(kuerzel : string | null) : Verkehrssprache | null {
		return Verkehrssprache.getMapSpracheByKuerzel().get(kuerzel);
	}

	/**
	 * Gibt die Verkehrssprache für das angegebene zweistellige ISO 639-1-Kürzel zurück.
	 *
	 * @param kuerzel   das zweistellige Kürzel der Verkehrssprache
	 *
	 * @return die Verkehrssprache oder null, falls das Kürzel unbekannt ist
	 */
	public static getByKuerzelISO2(kuerzel : string | null) : Verkehrssprache | null {
		return Verkehrssprache.getMapSpracheByKuerzel2().get(kuerzel);
	}

	/**
	 * Gibt die Verkehrssprache für das angegebene Kürzel zurück.
	 * Dabei wird anhand der Länge des Kürzels automatisch geprüft, ob
	 * eine Sprache nach ISO 639-1 bzw. ISO 639-2 angegeben wurde.
	 *
	 * @param kuerzel   das Kürzel der Verkehrssprache
	 *
	 * @return die Verkehrssprache oder null, falls das Kürzel unbekannt ist
	 */
	public static getByKuerzelAuto(kuerzel : string | null) : Verkehrssprache | null {
		if (kuerzel === null)
			return null;
		if (kuerzel.length === 2)
			return Verkehrssprache.getMapSpracheByKuerzel2().get(kuerzel);
		return Verkehrssprache.getMapSpracheByKuerzel().get(kuerzel);
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	private name() : string {
		return this.__name;
	}

	/**
	 * Returns the ordinal value of this enumeration value.
	 *
	 * @returns the ordinal value
	 */
	private ordinal() : number {
		return this.__ordinal;
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	public toString() : string {
		return this.__name;
	}

	/**
	 * Returns true if this and the other enumeration values are equal.
	 *
	 * @param other   the other enumeration value
	 *
	 * @returns true if they are equal and false otherwise
	 */
	public equals(other : JavaObject) : boolean {
		if (!(other instanceof Verkehrssprache))
			return false;
		return this === other;
	}

	/**
	 * Returns the ordinal value as hashcode, since the ordinal value is unique.
	 *
	 * @returns the ordinal value as hashcode
	 */
	public hashCode() : number {
		return this.__ordinal;
	}

	/**
	 * Compares this enumeration value with the other enumeration value by their ordinal value.
	 *
	 * @param other   the other enumeration value
	 *
	 * @returns a negative, zero or postive value as this enumeration value is less than, equal to
	 *          or greater than the other enumeration value
	 */
	public compareTo(other : Verkehrssprache) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Verkehrssprache> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : Verkehrssprache | null {
		const tmp : Verkehrssprache | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.schule.Verkehrssprache'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_schule_Verkehrssprache(obj : unknown) : Verkehrssprache {
	return obj as Verkehrssprache;
}
