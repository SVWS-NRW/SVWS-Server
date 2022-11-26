package de.nrw.schule.svws.core.types.schule;

import java.util.HashMap;

import de.nrw.schule.svws.core.data.schule.VerkehrsspracheKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die Verkehrssprachen. Die Daten bilden einen 
 * Ausschnitt aus dem ISO-Standard 639-2 
 * (siehe https://de.wikipedia.org/wiki/Liste_der_ISO-639-2-Codes).
 * Teilweise wurden Codes aus dem ISO-Standard 639-3 
 * (siehe https://de.wikipedia.org/wiki/Überblicksliste_der_ISO-639-3-Codes) 
 * ergänzt. 
 */
public enum Verkehrssprache {

	/** Afar (aar) */
	AAR(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(1000, "aar", "Afar", "aa", null, null)
	}),

	/** Abchasisch (abk) */
	ABK(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(2000, "abk", "Abchasisch", "ab", null, null)
	}),

	/** Abron (abr) - ISO 639-3 */
	ABR(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(3000, "abr", "Abron", null, null, null)
	}),

	/** Achinesisch (ace) */
	ACE(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(4000, "ace", "Achinesisch", null, null, null)
	}),

	/** Acoli (ach) */
	ACH(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(5000, "ach", "Acoli", null, null, null)
	}),

	/** Adangme (ada) */
	ADA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(6000, "ada", "Adangme", null, null, null)
	}),

	/** Adygeisch (ady) */
	ADY(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(7000, "ady", "Adygeisch", null, null, null)
	}),

	/** Afrihili (afh) */
	AFH(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(8000, "afh", "Afrihili", null, null, null)
	}),

	/** Afrikaans (afr) */
	AFR(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(9000, "afr", "Afrikaans", "af", null, null)
	}),

	/** Awutu (afu) - ISO 639-3 */
	AFU(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(10000, "afu", "Awutu", null, null, null)
	}),

	/** Assyrisch-neuaramäischer Dialekt (aii) - ISO 639-3 */
	AII(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(11000, "aii", "Assyrisch-neuaramäischer Dialekt", null, null, null)
	}),

	/** Ainu (ain) */
	AIN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(12000, "ain", "Ainu", null, null, null)
	}),

	/** Aja (aja) - ISO 639-3 */
	AJA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(13000, "aja", "Aja", null, null, null)
	}),

	/** Akan (aka) */
	AKA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(14000, "aka", "Akan", "ak", null, null)
	}),

	/** Akkadisch (akk) */
	AKK(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(15000, "akk", "Akkadisch", null, null, null)
	}),

	/** Natchamba (aks) - ISO 639-3 */
	AKS(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(16000, "aks", "Natchamba", null, null, null)
	}),

	/** Aleutisch (ale) */
	ALE(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(17000, "ale", "Aleutisch", null, null, null)
	}),

	/** Südaltaisch (alt) */
	ALT(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(18000, "alt", "Südaltaisch", null, null, null)
	}),

	/** Amharisch (amh) */
	AMH(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(19000, "amh", "Amharisch", "am", null, null)
	}),

	/** Altenglisch (ang) */
	ANG(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(20000, "ang", "Altenglisch", null, null, null)
	}),

	/** Angika (anp) */
	ANP(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(21000, "anp", "Angika", null, null, null)
	}),

	/** Anuak (anu) */
	ANU(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(22000, "anu", "Anuak", null, null, null)
	}),

	/** Anyin (any) */
	ANY(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(23000, "any", "Anyin", null, null, null)
	}),

	/** Arabisch (ara) */
	ARA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(24000, "ara", "Arabisch", "ar", null, null)
	}),

	/** Aramäisch (arc) */
	ARC(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(25000, "arc", "Aramäisch", null, null, null)
	}),

	/** Aragonesisch (arg) */
	ARG(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(26000, "arg", "Aragonesisch", "an", null, null)
	}),

	/** Mapudungun (arn) */
	ARN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(27000, "arn", "Mapudungun", null, null, null)
	}),

	/** Algerisch-Arabisch (arq) - ISO 639-3 */
	ARQ(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(28000, "arq", "Algerisch-Arabisch", null, null, null)
	}),

	/** Arawak (arw) */
	ARW(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(29000, "arw", "Arawak", null, null, null)
	}),

	/** Marokkanisch-Arabisch (ary) - ISO 639-3 */
	ARY(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(30000, "ary", "Marokkanisch-Arabisch", null, null, null)
	}),

	/** Tsischingini (asg) - ISO 639-3 */
	ASG(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(31000, "asg", "Tsischingini", null, null, null)
	}),

	/** Assamesisch (asm) */
	ASM(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(32000, "asm", "Assamesisch", "as", null, null)
	}),

	/** Asturisch (ast) */
	AST(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(33000, "ast", "Asturisch", null, null, null)
	}),

	/** Awarisch (ava) */
	AVA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(34000, "ava", "Awarisch", "av", null, null)
	}),

	/** Avestisch (ave) */
	AVE(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(35000, "ave", "Avestisch", null, null, null)
	}),

	/** Awadhi (awa) */
	AWA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(36000, "awa", "Awadhi", null, null, null)
	}),

	/** Aymara (aym) */
	AYM(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(37000, "aym", "Aymara", "ay", null, null)
	}),

	/** Süd-Aserbaidschanisch (azb) - ISO 639-3 */
	AZB(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(38000, "azb", "Süd-Aserbaidschanisch", null, null, null)
	}),

	/** Aserbaidschanisch (aze) */
	AZE(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(39000, "aze", "Aserbaidschanisch", "az", null, null)
	}),

	/** Banda (bad) */
	BAD(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(40000, "bad", "Banda", null, null, null)
	}),

	/** Bamileke (bai) */
	BAI(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(41000, "bai", "Bamileke", null, null, null)
	}),

	/** Baschkirisch (bak) */
	BAK(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(42000, "bak", "Baschkirisch", "ba", null, null)
	}),

	/** Belutschisch (bal) */
	BAL(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(43000, "bal", "Belutschisch", null, null, null)
	}),

	/** Bambara (bam) */
	BAM(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(44000, "bam", "Bambara", "bm", null, null)
	}),

	/** Balinesisch (ban) */
	BAN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(45000, "ban", "Balinesisch", null, null, null)
	}),

	/** Bairisch (bar) - ISO 639-3 */
	BAR(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(45800, "bar", "Bairisch", null, null, null)
	}),

	/** Basa (bas) */
	BAS(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(46000, "bas", "Basa", null, null, null)
	}),

	/** Bamun (bax) - ISO 639-3 */
	BAX(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(47000, "bax", "Bamun", null, null, null)
	}),

	/** Baatonum (bba) - ISO 639-3 */
	BBA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(48000, "bba", "Baatonum", null, null, null)
	}),

	/** Ghomálá (bbj) - ISO 639-3 */
	BBJ(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(49000, "bbj", "Ghomálá", null, null, null)
	}),

	/** Baoulé (bci) - ISO 639-3 */
	BCI(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(50000, "bci", "Baoulé", null, null, null)
	}),

	/** Beja (bej) */
	BEJ(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(51000, "bej", "Beja", null, null, null)
	}),

	/** Weißrussisch (bel) */
	BEL(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(52000, "bel", "Weißrussisch", "be", null, null)
	}),

	/** Bemba (bem) */
	BEM(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(53000, "bem", "Bemba", null, null, null)
	}),

	/** Bengali (ben) */
	BEN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(54000, "ben", "Bengali", "bn", null, null)
	}),

	/** Berbersprachen (Andere) (ber) */
	BER(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(55000, "ber", "Berbersprachen (Andere)", null, null, null)
	}),

	/** Guiberoua Béte (bet) - ISO 639-3 */
	BET(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(56000, "bet", "Guiberoua Béte", null, null, null)
	}),

	/** Daloa Bété (bev) - ISO 639-3 */
	BEV(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(57000, "bev", "Daloa Bété", null, null, null)
	}),

	/** Bari (bfa) - ISO 639-3 */
	BFA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(58000, "bfa", "Bari", null, null, null)
	}),

	/** Bhojpuri (bho) */
	BHO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(59000, "bho", "Bhojpuri", null, null, null)
	}),

	/** Bhele (bhy) - ISO 639-3 */
	BHY(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(60000, "bhy", "Bhele", null, null, null)
	}),

	/** Bissa (bib) - ISO 639-3 */
	BIB(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(61000, "bib", "Bissa", null, null, null)
	}),

	/** Bihari (bih) */
	BIH(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(62000, "bih", "Bihari", "bh", null, null)
	}),

	/** Bikol (bik) */
	BIK(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(63000, "bik", "Bikol", null, null, null)
	}),

	/** Bini (bin) */
	BIN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(64000, "bin", "Bini", null, null, null)
	}),

	/** Bislama (bis) */
	BIS(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(65000, "bis", "Bislama", "bi", null, null)
	}),

	/** Lame (bma) - ISO 639-3 */
	BMA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(66000, "bma", "Lame", null, null, null)
	}),

	/** Bomu (bmq) - ISO 639-3 */
	BMQ(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(67000, "bmq", "Bomu", null, null, null)
	}),

	/** Bantu (bnt) */
	BNT(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(68000, "bnt", "Bantu", null, null, null)
	}),

	/** Tibetisch (bod) */
	BOD(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(69000, "bod", "Tibetisch", "bo", null, null)
	}),

	/** Bosnisch (bos) */
	BOS(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(70000, "bos", "Bosnisch", "bs", null, null)
	}),

	/** Bretonisch (bre) */
	BRE(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(71000, "bre", "Bretonisch", "br", null, null)
	}),

	/** Gagnoa Bété (btg) - ISO 639-3 */
	BTG(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(72000, "btg", "Gagnoa Bété", null, null, null)
	}),

	/** Batak (btk) */
	BTK(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(73000, "btk", "Batak", null, null, null)
	}),

	/** Burjatisch (bua) */
	BUA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(74000, "bua", "Burjatisch", null, null, null)
	}),

	/** Ntcham (bud) - ISO 639-3 */
	BUD(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(75000, "bud", "Ntcham", null, null, null)
	}),

	/** Bushoong (buf) - ISO 639-3 */
	BUF(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(76000, "buf", "Bushoong", null, null, null)
	}),

	/** Buginesisch (bug) */
	BUG(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(77000, "bug", "Buginesisch", null, null, null)
	}),

	/** Bulgarisch (bul) */
	BUL(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(78000, "bul", "Bulgarisch", "bg", null, null)
	}),

	/** Bulu (bum) - ISO 639-3 */
	BUM(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(79000, "bum", "Bulu", null, null, null)
	}),

	/** Bubi (buw) - ISO 639-3 */
	BUW(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(80000, "buw", "Bubi", null, null, null)
	}),

	/** Buli (bwu) - ISO 639-3 */
	BWU(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(81000, "bwu", "Buli", null, null, null)
	}),

	/** Bilin (byn) */
	BYN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(82000, "byn", "Bilin", null, null, null)
	}),

	/** Medumba (byv) - ISO 639-3 */
	BYV(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(83000, "byv", "Medumba", null, null, null)
	}),

	/** Basa (bzw) - ISO 639-3 */
	BZW(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(84000, "bzw", "Basa", null, null, null)
	}),

	/** Katalanisch (cat) */
	CAT(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(85000, "cat", "Katalanisch", "ca", null, null)
	}),

	/** Kaukasisch (cau) */
	CAU(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(86000, "cau", "Kaukasisch", null, null, null)
	}),

	/** Cebuano (ceb) */
	CEB(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(87000, "ceb", "Cebuano", null, null, null)
	}),

	/** Tschechisch (ces) */
	CES(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(88000, "ces", "Tschechisch", "cs", null, null)
	}),

	/** Tschetschenisch (che) */
	CHE(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(89000, "che", "Tschetschenisch", "ce", null, null)
	}),

	/** Tschuwaschisch (chv) */
	CHV(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(90000, "chv", "Tschuwaschisch", "cv", null, null)
	}),

	/** Chokwe (cjk) - ISO 639-3 */
	CJK(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(91000, "cjk", "Chokwe", null, null, null)
	}),

	/** Anufo (cko) - ISO 639-3 */
	CKO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(92000, "cko", "Anufo", null, null, null)
	}),

	/** Chaldäisches Aramäisch (cld) - ISO 639-3 */
	CLD(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(93000, "cld", "Chaldäisches Aramäisch", null, null, null)
	}),

	/** Montenegrinisch (cnr) */
	CNR(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(94000, "cnr", "Montenegrinisch", null, null, null)
	}),

	/** Kornisch (cor) */
	COR(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(95000, "cor", "Kornisch", "kw", null, null)
	}),

	/** Korsisch (cos) */
	COS(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(96000, "cos", "Korsisch", "co", null, null)
	}),

	/** Kreolisch-Englisch (cpe) */
	CPE(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(97000, "cpe", "Kreolisch-Englisch", null, null, null)
	}),

	/** Kreolisch-Französisch (cpf) */
	CPF(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(98000, "cpf", "Kreolisch-Französisch", null, null, null)
	}),

	/** Kreolisch-Portugiesisch (cpp) */
	CPP(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(99000, "cpp", "Kreolisch-Portugiesisch", null, null, null)
	}),

	/** Cree (cre) */
	CRE(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(100000, "cre", "Cree", "cr", null, null)
	}),

	/** Kreolische Sprachen (Andere) (crp) */
	CRP(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(101000, "crp", "Kreolische Sprachen (Andere)", null, null, null)
	}),

	/** Walisisch (cym) */
	CYM(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(102000, "cym", "Walisisch", "cy", null, null)
	}),

	/** Dagbani (dag) - ISO 639-3 */
	DAG(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(103000, "dag", "Dagbani", null, null, null)
	}),

	/** Dänisch (dan) */
	DAN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(104000, "dan", "Dänisch", "da", null, null)
	}),

	/** Darginisch (dar) */
	DAR(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(105000, "dar", "Darginisch", null, null, null)
	}),

	/** Dajakisch (day) */
	DAY(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(106000, "day", "Dajakisch", null, null, null)
	}),

	/** Dendi (ddn) - ISO 639-3 */
	DDN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(107000, "ddn", "Dendi", null, null, null)
	}),

	/** Delaware (del) */
	DEL(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(108000, "del", "Delaware", null, null, null)
	}),

	/** Deutsch (deu) */
	DEU(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(109000, "deu", "Deutsch", "de", null, null)
	}),

	/** Süd-Dagaare (dga) - ISO 639-3 */
	DGA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(110000, "dga", "Süd-Dagaare", null, null, null)
	}),

	/** Dinka (din) */
	DIN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(111000, "din", "Dinka", null, null, null)
	}),

	/** Maledivisch (div) */
	DIV(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(112000, "div", "Maledivisch", "dv", null, null)
	}),

	/** Zarma (dje) - ISO 639-3 */
	DJE(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(113000, "dje", "Zarma", null, null, null)
	}),

	/** Dan (dnj) - ISO 639-3 */
	DNJ(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(114000, "dnj", "Dan", null, null, null)
	}),

	/** Tommo So Dogon (dto) - ISO 639-3 */
	DTO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(115000, "dto", "Tommo So Dogon", null, null, null)
	}),

	/** Duala (dua) */
	DUA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(116000, "dua", "Duala", null, null, null)
	}),

	/** Diola (dyo) - ISO 639-3 */
	DYO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(117000, "dyo", "Diola", null, null, null)
	}),

	/** Dyula (dyu) */
	DYU(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(118000, "dyu", "Dyula", null, null, null)
	}),

	/** Dazaga (dzg) - ISO 639-3 */
	DZG(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(119000, "dzg", "Dazaga", null, null, null)
	}),

	/** Dzongkha (dzo) */
	DZO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(120000, "dzo", "Dzongkha", "dz", null, null)
	}),

	/** Efik (efi) */
	EFI(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(121000, "efi", "Efik", null, null, null)
	}),

	/** Ekajuk (eka) */
	EKA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(122000, "eka", "Ekajuk", null, null, null)
	}),

	/** Griechisch (ell) */
	ELL(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(123000, "ell", "Griechisch", "el", null, null)
	}),

	/** Englisch (eng) */
	ENG(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(124000, "eng", "Englisch", "en", null, null)
	}),

	/** Estnisch (est) */
	EST(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(125000, "est", "Estnisch", "et", null, null)
	}),

	/** Eton (eto) - ISO 639-3 */
	ETO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(126000, "eto", "Eton", null, null, null)
	}),

	/** Ejagham (etu) - ISO 639-3 */
	ETU(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(127000, "etu", "Ejagham", null, null, null)
	}),

	/** Baskisch (eus) */
	EUS(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(128000, "eus", "Baskisch", "eu", null, null)
	}),

	/** Ewe (ewe) */
	EWE(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(129000, "ewe", "Ewe", "ee", null, null)
	}),

	/** Ewondo (ewo) */
	EWO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(130000, "ewo", "Ewondo", null, null, null)
	}),

	/** Fali (fal) - ISO 639-3 */
	FAL(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(131000, "fal", "Fali", null, null, null)
	}),

	/** Fang (fan) */
	FAN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(132000, "fan", "Fang", null, null, null)
	}),

	/** Färöisch (fao) */
	FAO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(133000, "fao", "Färöisch", "fo", null, null)
	}),

	/** Fanti (fat) */
	FAT(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(134000, "fat", "Fanti", null, null, null)
	}),

	/** Fidschi (fij) */
	FIJ(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(135000, "fij", "Fidschi", "fj", null, null)
	}),

	/** Finnisch (fin) */
	FIN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(136000, "fin", "Finnisch", "fi", null, null)
	}),

	/** Fali (fll) - ISO 639-3 */
	FLL(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(137000, "fll", "Fali", null, null, null)
	}),

	/** Fe´fe´ (fmp) - ISO 639-3 */
	FMP(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(138000, "fmp", "Fe´fe´", null, null, null)
	}),

	/** Fon (fon) */
	FON(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(139000, "fon", "Fon", null, null, null)
	}),

	/** Französisch (fra) */
	FRA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(140000, "fra", "Französisch", "fr", null, null)
	}),

	/** Westfriesisch (fry) */
	FRY(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(141000, "fry", "Westfriesisch", "fy", null, null)
	}),

	/** Adamaua-Fulfulde (fub) - ISO 639-3 */
	FUB(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(142000, "fub", "Adamaua-Fulfulde", null, null, null)
	}),

	/** Pulaar (fuc) - ISO 639-3 */
	FUC(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(143000, "fuc", "Pulaar", null, null, null)
	}),

	/** Pular (fuf) - ISO 639-3 */
	FUF(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(144000, "fuf", "Pular", null, null, null)
	}),

	/** Fulfulde (ful) */
	FUL(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(145000, "ful", "Fulfulde", "ff", null, null)
	}),

	/** Fur (fvr) */
	FVR(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(146000, "fvr", "Fur", null, null, null)
	}),

	/** Ga (gaa) */
	GAA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(147000, "gaa", "Ga", null, null, null)
	}),

	/** Gayo (gay) */
	GAY(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(148000, "gay", "Gayo", null, null, null)
	}),

	/** Gbaya (gba) */
	GBA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(149000, "gba", "Gbaya", null, null, null)
	}),

	/** Gen (gej) - ISO 639-3 */
	GEJ(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(150000, "gej", "Gen", null, null, null)
	}),

	/** Schottisch-gälisch (gla) */
	GLA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(151000, "gla", "Schottisch-gälisch", "gd", null, null)
	}),

	/** Irisch (gle) */
	GLE(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(152000, "gle", "Irisch", "ga", null, null)
	}),

	/** Galicisch (glg) */
	GLG(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(153000, "glg", "Galicisch", "gl", null, null)
	}),

	/** Guro (goa) - ISO 639-3 */
	GOA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(154000, "goa", "Guro", null, null, null)
	}),

	/** Gondi (gon) */
	GON(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(155000, "gon", "Gondi", null, null, null)
	}),

	/** Gorontalesisch (gor) */
	GOR(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(156000, "gor", "Gorontalesisch", null, null, null)
	}),

	/** Grebo (grb) */
	GRB(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(157000, "grb", "Grebo", null, null, null)
	}),

	/** Guarani (grn) */
	GRN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(158000, "grn", "Guarani", "gn", null, null)
	}),

	/** Yocoboué Dida (gud) - ISO 639-3 */
	GUD(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(159000, "gud", "Yocoboué Dida", null, null, null)
	}),

	/** Gujarati (guj) */
	GUJ(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(160000, "guj", "Gujarati", "gu", null, null)
	}),

	/** Farefare (gur) - ISO 639-3 */
	GUR(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(161000, "gur", "Farefare", null, null, null)
	}),

	/** Gun (guw) - ISO 639-3 */
	GUW(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(162000, "guw", "Gun", null, null, null)
	}),

	/** Gourmanchéma (gux) - ISO 639-3 */
	GUX(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(163000, "gux", "Gourmanchéma", null, null, null)
	}),

	/** Gurung (gvr) - ISO 639-3 */
	GVR(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(164000, "gvr", "Gurung", null, null, null)
	}),

	/** Gorani (hac) - ISO 639-3 */
	HAC(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(165000, "hac", "Gorani", null, null, null)
	}),

	/** Harari (har) - ISO 639-3 */
	HAR(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(166000, "har", "Harari", null, null, null)
	}),

	/** Haitianisch (hat) */
	HAT(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(167000, "hat", "Haitianisch", "ht", null, null)
	}),

	/** Hausa (hau) */
	HAU(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(168000, "hau", "Hausa", "ha", null, null)
	}),

	/** Serbokroatisch (hbs) */
	HBS(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(169000, "hbs", "Serbokroatisch", null, null, null)
	}),

	/** Hebräisch (heb) */
	HEB(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(170000, "heb", "Hebräisch", "he", null, null)
	}),

	/** Herero (her) */
	HER(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(171000, "her", "Herero", "hz", null, null)
	}),

	/** Hiligaynon (hil) */
	HIL(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(172000, "hil", "Hiligaynon", null, null, null)
	}),

	/** Hindi (hin) */
	HIN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(173000, "hin", "Hindi", "hi", null, null)
	}),

	/** Hmong (hmn) */
	HMN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(174000, "hmn", "Hmong", null, null, null)
	}),

	/** Hiri Motu (hmo) */
	HMO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(175000, "hmo", "Hiri Motu", "ho", null, null)
	}),

	/** Kroatisch (hrv) */
	HRV(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(176000, "hrv", "Kroatisch", "hr", null, null)
	}),

	/** Ungarisch (hun) */
	HUN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(177000, "hun", "Ungarisch", "hu", null, null)
	}),

	/** Armenisch (hye) */
	HYE(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(178000, "hye", "Armenisch", "hy", null, null)
	}),

	/** Iban (iba) */
	IBA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(179000, "iba", "Iban", null, null, null)
	}),

	/** Igbo (ibo) */
	IBO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(180000, "ibo", "Igbo", null, null, null)
	}),

	/** Sichuan-Yi (iii) */
	III(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(181000, "iii", "Sichuan-Yi", "ii", null, null)
	}),

	/** Ijo (ijo) */
	IJO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(182000, "ijo", "Ijo", null, null, null)
	}),

	/** Ijo-Südost (ijs) - ISO 639-3 */
	IJS(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(183000, "ijs", "Ijo-Südost", null, null, null)
	}),

	/** Inuktitut (iku) */
	IKU(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(184000, "iku", "Inuktitut", "iu", null, null)
	}),

	/** Ilokano (ilo) */
	ILO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(185000, "ilo", "Ilokano", null, null, null)
	}),

	/** Indonesisch (ind) */
	IND(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(186000, "ind", "Indonesisch", "id", null, null)
	}),

	/** Inguschisch (inh) */
	INH(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(187000, "inh", "Inguschisch", null, null, null)
	}),

	/** Inupiaq (ipk) */
	IPK(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(188000, "ipk", "Inupiaq", "ik", null, null)
	}),

	/** Esan (ish) */
	ISH(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(189000, "ish", "Esan", null, null, null)
	}),

	/** Isländisch (isl) */
	ISL(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(190000, "isl", "Isländisch", "is", null, null)
	}),

	/** Isoko (iso) - ISO 639-3 */
	ISO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(191000, "iso", "Isoko", null, null, null)
	}),

	/** Italienisch (ita) */
	ITA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(192000, "ita", "Italienisch", "it", null, null)
	}),

	/** Isekiri (its) - ISO 639-3 */
	ITS(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(193000, "its", "Isekiri", null, null, null)
	}),

	/** Hyam (jab) - ISO 639-3 */
	JAB(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(194000, "jab", "Hyam", null, null, null)
	}),

	/** Javanisch (jav) */
	JAV(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(195000, "jav", "Javanisch", "jv", null, null)
	}),

	/** Japanisch (jpn) */
	JPN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(196000, "jpn", "Japanisch", "ja", null, null)
	}),

	/** Kabylisch (kab) */
	KAB(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(197000, "kab", "Kabylisch", null, null, null)
	}),

	/** Grönländisch (kal) */
	KAL(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(198000, "kal", "Grönländisch", "kl", null, null)
	}),

	/** Kamba (kam) */
	KAM(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(199000, "kam", "Kamba", null, null, null)
	}),

	/** Kannada (kan) */
	KAN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(200000, "kan", "Kannada", "kn", null, null)
	}),

	/** Xaasongaxango (kao) - ISO 639-3 */
	KAO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(201000, "kao", "Xaasongaxango", null, null, null)
	}),

	/** Kashmiri (kas) */
	KAS(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(202000, "kas", "Kashmiri", "ks", null, null)
	}),

	/** Georgisch (kat) */
	KAT(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(203000, "kat", "Georgisch", "ka", null, null)
	}),

	/** Kanuri (kau) */
	KAU(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(204000, "kau", "Kanuri", "kr", null, null)
	}),

	/** Kasachisch (kaz) */
	KAZ(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(205000, "kaz", "Kasachisch", "kk", null, null)
	}),

	/** Kabardinisch (kbd) */
	KBD(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(206000, "kbd", "Kabardinisch", null, null, null)
	}),

	/** Kanembu (kbl) - ISO 639-3 */
	KBL(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(207000, "kbl", "Kanembu", null, null, null)
	}),

	/** Kabiyé (kbp) - ISO 639-3 */
	KBP(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(208000, "kbp", "Kabiyé", null, null, null)
	}),

	/** Chimakonde (kde) - ISO 639-3 */
	KDE(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(209000, "kde", "Chimakonde", null, null, null)
	}),

	/** Tem (kdh) - ISO 639-3 */
	KDH(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(210000, "kdh", "Tem", null, null, null)
	}),

	/** Kenyang (ken) - ISO 639-3 */
	KEN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(211000, "ken", "Kenyang", null, null, null)
	}),

	/** Khasi (kha) */
	KHA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(212000, "kha", "Khasi", null, null, null)
	}),

	/** Kuturmi (khj) - ISO 639-3 */
	KHJ(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(213000, "khj", "Kuturmi", null, null, null)
	}),

	/** Khmer (khm) */
	KHM(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(214000, "khm", "Khmer", "km", null, null)
	}),

	/** Kikuyu (kik) */
	KIK(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(215000, "kik", "Kikuyu", "ki", null, null)
	}),

	/** Kinyarwanda (kin) */
	KIN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(216000, "kin", "Kinyarwanda", "rw", null, null)
	}),

	/** Kirgisisch (kir) */
	KIR(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(217000, "kir", "Kirgisisch", "ky", null, null)
	}),

	/** Kisi (kiz) - ISO 639-3 */
	KIZ(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(218000, "kiz", "Kisi", null, null, null)
	}),

	/** Klao (klu) - ISO 639-3 */
	KLU(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(219000, "klu", "Klao", null, null, null)
	}),

	/** Kimbundu (kmb) */
	KMB(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(220000, "kmb", "Kimbundu", null, null, null)
	}),

	/** Kono (kno) - ISO 639-3 */
	KNO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(221000, "kno", "Kono", null, null, null)
	}),

	/** Konkani (kok) */
	KOK(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(222000, "kok", "Konkani", null, null, null)
	}),

	/** Komi (kom) */
	KOM(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(223000, "kom", "Komi", "kv", null, null)
	}),

	/** Kongo (kon) */
	KON(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(224000, "kon", "Kongo", "kg", null, null)
	}),

	/** Koreanisch (kor) */
	KOR(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(225000, "kor", "Koreanisch", "ko", null, null)
	}),

	/** Lagwan (kot) - ISO 639-3 */
	KOT(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(226000, "kot", "Lagwan", null, null, null)
	}),

	/** Kpelle (kpe) */
	KPE(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(227000, "kpe", "Kpelle", null, null, null)
	}),

	/** Kpan (kpk) - ISO 639-3 */
	KPK(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(228000, "kpk", "Kpan", null, null, null)
	}),

	/** Nord-Kissi (kqs) - ISO 639-3 */
	KQS(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(229000, "kqs", "Nord-Kissi", null, null, null)
	}),

	/** Kru-Sprachen (kro) */
	KRO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(230000, "kro", "Kru-Sprachen", null, null, null)
	}),

	/** Kurukh (kru) */
	KRU(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(231000, "kru", "Kurukh", null, null, null)
	}),

	/** Kölsch (ksh) - ISO 639-3 */
	KSH(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(231500, "ksh", "Kölsch", null, null, null)
	}),

	/** Süd-Kissi (kss) - ISO 639-3 */
	KSS(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(232000, "kss", "Süd-Kissi", null, null, null)
	}),

	/** Kuanyama (kua) */
	KUA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(233000, "kua", "Kuanyama", "kj", null, null)
	}),

	/** Kumykisch (kum) */
	KUM(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(234000, "kum", "Kumykisch", null, null, null)
	}),

	/** Kurdisch (kur) */
	KUR(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(235000, "kur", "Kurdisch", "ku", null, null)
	}),

	/** Kwese (kws) - ISO 639-3 */
	KWS(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(236000, "kws", "Kwese", null, null, null)
	}),

	/** Kulango Bondoukou (kzc) - ISO 639-3 */
	KZC(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(237000, "kzc", "Kulango Bondoukou", null, null, null)
	}),

	/** Lahnda (lah) */
	LAH(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(238000, "lah", "Lahnda", null, null, null)
	}),

	/** Lango (laj) - ISO 639-3 */
	LAJ(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(239000, "laj", "Lango", null, null, null)
	}),

	/** Lamba (lam) */
	LAM(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(240000, "lam", "Lamba", null, null, null)
	}),

	/** Laotisch (lao) */
	LAO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(241000, "lao", "Laotisch", "lo", null, null)
	}),

	/** Larteh (lar) - ISO 639-3 */
	LAR(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(242000, "lar", "Larteh", null, null, null)
	}),

	/** Lama (las) - ISO 639-3 */
	LAS(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(243000, "las", "Lama", null, null, null)
	}),

	/** Lettisch (lav) */
	LAV(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(244000, "lav", "Lettisch", "lv", null, null)
	}),

	/** Lakisch (lbe) - ISO 639-3 */
	LBE(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(245000, "lbe", "Lakisch", null, null, null)
	}),

	/** Laari (ldi) - ISO 639-3 */
	LDI(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(246000, "ldi", "Laari", null, null, null)
	}),

	/** Lyélé (lee) - ISO 639-3 */
	LEE(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(247000, "lee", "Lyélé", null, null, null)
	}),

	/** Lelemi (lef) - ISO 639-3 */
	LEF(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(248000, "lef", "Lelemi", null, null, null)
	}),

	/** Lesgisch (lez) */
	LEZ(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(249000, "lez", "Lesgisch", null, null, null)
	}),

	/** Lugbara (lgg) - ISO 639-3 */
	LGG(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(250000, "lgg", "Lugbara", null, null, null)
	}),

	/** Limba (lia) - ISO 639-3 */
	LIA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(251000, "lia", "Limba", null, null, null)
	}),

	/** Ligbi (lig) - ISO 639-3 */
	LIG(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(252000, "lig", "Ligbi", null, null, null)
	}),

	/** Limburgisch (lim) */
	LIM(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(253000, "lim", "Limburgisch", "li", null, null)
	}),

	/** Lingala (lin) */
	LIN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(254000, "lin", "Lingala", "ln", null, null)
	}),

	/** Litauisch (lit) */
	LIT(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(255000, "lit", "Litauisch", "lt", null, null)
	}),

	/** Banda-Bambari (liy) - ISO 639-3 */
	LIY(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(256000, "liy", "Banda-Bambari", null, null, null)
	}),

	/** Lamnso (lns) - ISO 639-3 */
	LNS(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(257000, "lns", "Lamnso", null, null, null)
	}),

	/** Lobiri (lob) - ISO 639-3 */
	LOB(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(258000, "lob", "Lobiri", null, null, null)
	}),

	/** Loko (lok) - ISO 639-3 */
	LOK(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(259000, "lok", "Loko", null, null, null)
	}),

	/** Lomongo (lol) */
	LOL(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(260000, "lol", "Lomongo", null, null, null)
	}),

	/** Lozi (loz) */
	LOZ(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(261000, "loz", "Lozi", null, null, null)
	}),

	/** Lusengo (lse) - ISO 639-3 */
	LSE(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(262000, "lse", "Lusengo", null, null, null)
	}),

	/** Luxemburgisch (ltz) */
	LTZ(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(263000, "ltz", "Luxemburgisch", "lb", null, null)
	}),

	/** Luba-Lulua (lua) */
	LUA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(264000, "lua", "Luba-Lulua", null, null, null)
	}),

	/** Luba-Katanga (lub) */
	LUB(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(265000, "lub", "Luba-Katanga", "lu", null, null)
	}),

	/** Luganda (lug) */
	LUG(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(266000, "lug", "Luganda", "lg", null, null)
	}),

	/** Lunda (lun) */
	LUN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(267000, "lun", "Lunda", null, null, null)
	}),

	/** Luo (luo) */
	LUO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(268000, "luo", "Luo", null, null, null)
	}),

	/** Lushai (lus) */
	LUS(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(269000, "lus", "Lushai", null, null, null)
	}),

	/** Luhya (luy) - ISO 639-3 */
	LUY(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(270000, "luy", "Luhya", null, null, null)
	}),

	/** Luwo (lwo) - ISO 639-3 */
	LWO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(271000, "lwo", "Luwo", null, null, null)
	}),

	/** Maduresisch (mad) */
	MAD(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(272000, "mad", "Maduresisch", null, null, null)
	}),

	/** Magadhi (mag) */
	MAG(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(273000, "mag", "Magadhi", null, null, null)
	}),

	/** Marshallesisch (mah) */
	MAH(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(274000, "mah", "Marshallesisch", "mh", null, null)
	}),

	/** Maithili (mai) */
	MAI(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(275000, "mai", "Maithili", null, null, null)
	}),

	/** Makassarisch (mak) */
	MAK(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(276000, "mak", "Makassarisch", null, null, null)
	}),

	/** Malayalam (mal) */
	MAL(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(277000, "mal", "Malayalam", "ml", null, null)
	}),

	/** Mandingo (man) */
	MAN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(278000, "man", "Mandingo", null, null, null)
	}),

	/** Marathi (mar) */
	MAR(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(279000, "mar", "Marathi", "mr", null, null)
	}),

	/** Masai (mas) */
	MAS(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(280000, "mas", "Masai", null, null, null)
	}),

	/** Mbo (mbo) - ISO 639-3 */
	MBO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(281000, "mbo", "Mbo", null, null, null)
	}),

	/** Maba (mde) - ISO 639-3 */
	MDE(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(282000, "mde", "Maba", null, null, null)
	}),

	/** Mandar (mdr) */
	MDR(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(283000, "mdr", "Mandar", null, null, null)
	}),

	/** Mbere (mdt) - ISO 639-3 */
	MDT(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(284000, "mdt", "Mbere", null, null, null)
	}),

	/** Mbosi (mdw) - ISO 639-3 */
	MDW(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(285000, "mdw", "Mbosi", null, null, null)
	}),

	/** Mende (men) */
	MEN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(286000, "men", "Mende", null, null, null)
	}),

	/** Meru (mer) - ISO 639-3 */
	MER(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(287000, "mer", "Meru", null, null, null)
	}),

	/** Mano (mev) - ISO 639-3 */
	MEV(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(288000, "mev", "Mano", null, null, null)
	}),

	/** Moba (mfq) - ISO 639-3 */
	MFQ(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(289000, "mfq", "Moba", null, null, null)
	}),

	/** Moru (mgd) - ISO 639-3 */
	MGD(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(290000, "mgd", "Moru", null, null, null)
	}),

	/** Mambae (mgm) - ISO 639-3 */
	MGM(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(291000, "mgm", "Mambae", null, null, null)
	}),

	/** Minangkabau (min) */
	MIN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(292000, "min", "Minangkabau", null, null, null)
	}),

	/** Mazedonisch (mkd) */
	MKD(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(293000, "mkd", "Mazedonisch", "mk", null, null)
	}),

	/** Kituba (mkw) - ISO 639-3 */
	MKW(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(294000, "mkw", "Kituba", null, null, null)
	}),

	/** Malagasy (mlg) */
	MLG(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(295000, "mlg", "Malagasy", "mg", null, null)
	}),

	/** Maltesisch (mlt) */
	MLT(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(296000, "mlt", "Maltesisch", "mt", null, null)
	}),

	/** Meitei (mni) */
	MNI(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(297000, "mni", "Meitei", null, null, null)
	}),

	/** Mandinka (mnk) - ISO 639-3 */
	MNK(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(298000, "mnk", "Mandinka", null, null, null)
	}),

	/** Moldawisch (mol) */
	MOL(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(299000, "mol", "Moldawisch", "mo", null, null)
	}),

	/** Mongolisch (mon) */
	MON(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(300000, "mon", "Mongolisch", "mn", null, null)
	}),

	/** Mossi (mos) */
	MOS(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(301000, "mos", "Mossi", null, null, null)
	}),

	/** Mpade (mpi) - ISO 639-3 */
	MPI(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(302000, "mpi", "Mpade", null, null, null)
	}),

	/** Maori (mri) */
	MRI(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(303000, "mri", "Maori", "mi", null, null)
	}),

	/** Malaiisch (msa) */
	MSA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(304000, "msa", "Malaiisch", "ms", null, null)
	}),

	/** Nyong (muo) - ISO 639-3 */
	MUO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(305000, "muo", "Nyong", null, null, null)
	}),

	/** Sar (mwm) - ISO 639-3 */
	MWM(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(306000, "mwm", "Sar", null, null, null)
	}),

	/** Gbe, Maxi (mxl) */
	MXL(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(307000, "mxl", "Gbe, Maxi", null, null, null)
	}),

	/** Burmesisch (mya) */
	MYA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(308000, "mya", "Burmesisch", "my", null, null)
	}),

	/** Mamara Senufo (myk) - ISO 639-3 */
	MYK(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(309000, "myk", "Mamara Senufo", null, null, null)
	}),

	/** Masaba (myx) - ISO 639-3 */
	MYX(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(310000, "myx", "Masaba", null, null, null)
	}),

	/** Nakanai (nak) - ISO 639-3 */
	NAK(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(311000, "nak", "Nakanai", null, null, null)
	}),

	/** Min Nan (nan) - ISO 639-3 */
	NAN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(312000, "nan", "Min Nan", null, null, null)
	}),

	/** Nama (naq) - ISO 639-3 */
	NAQ(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(313000, "naq", "Nama", null, null, null)
	}),

	/** Nauruanisch (nau) */
	NAU(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(314000, "nau", "Nauruanisch", "na", null, null)
	}),

	/** Navajo (nav) */
	NAV(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(315000, "nav", "Navajo", "nv", null, null)
	}),

	/** Nyemba (nba) - ISO 639-3 */
	NBA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(316000, "nba", "Nyemba", null, null, null)
	}),

	/** Ndebele-Süd (nbl) */
	NBL(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(317000, "nbl", "Ndebele-Süd", "nr", null, null)
	}),

	/** Ndebele-Nord (nde) */
	NDE(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(318000, "nde", "Ndebele-Nord", "nd", null, null)
	}),

	/** Ndonga (ndo) */
	NDO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(319000, "ndo", "Ndonga", "ng", null, null)
	}),

	/** Niederdeutsch, Plattdeutsch (nds) */
	NDS(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(319500, "nds", "Niederdeutsch, Plattdeutsch", null, null, null)
	}),

	/** Nepali (nep) */
	NEP(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(320000, "nep", "Nepali", "ne", null, null)
	}),

	/** Newari (new) */
	NEW(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(321000, "new", "Newari", null, null, null)
	}),

	/** Ngbandi-Nord (ngb) - ISO 639-3 */
	NGB(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(322000, "ngb", "Ngbandi-Nord", null, null, null)
	}),

	/** Ngemba (nge) - ISO 639-3 */
	NGE(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(323000, "nge", "Ngemba", null, null, null)
	}),

	/** Nias (nia) */
	NIA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(324000, "nia", "Nias", null, null, null)
	}),

	/** Nkonya (nko) - ISO 639-3 */
	NKO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(325000, "nko", "Nkonya", null, null, null)
	}),

	/** Kulango Bouna (nku) - ISO 639-3 */
	NKU(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(326000, "nku", "Kulango Bouna", null, null, null)
	}),

	/** Niederländisch (nld) */
	NLD(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(327000, "nld", "Niederländisch", "nl", null, null)
	}),

	/** Nda'nda' (nnz) */
	NNZ(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(328000, "nnz", "Nda'nda'", null, null, null)
	}),

	/** Norwegisch (nor) */
	NOR(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(329000, "nor", "Norwegisch", "no", null, null)
	}),

	/** Ede Nago (nqg) */
	NQG(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(330000, "nqg", "Ede Nago", null, null, null)
	}),

	/** Nord-Sotho (nso) */
	NSO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(331000, "nso", "Nord-Sotho", null, null, null)
	}),

	/** Nuer (nus) - ISO 639-3 */
	NUS(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(332000, "nus", "Nuer", null, null, null)
	}),

	/** Nyanja (nya) */
	NYA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(333000, "nya", "Nyanja", "ny", null, null)
	}),

	/** Nyamwezi (nym) */
	NYM(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(334000, "nym", "Nyamwezi", null, null, null)
	}),

	/** Nyankole (nyn) */
	NYN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(335000, "nyn", "Nyankole", null, null, null)
	}),

	/** Nyoro (nyo) */
	NYO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(336000, "nyo", "Nyoro", null, null, null)
	}),

	/** Nzima (nzi) */
	NZI(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(337000, "nzi", "Nzima", null, null, null)
	}),

	/** Okzitanisch (oci) */
	OCI(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(338000, "oci", "Okzitanisch", "oc", null, null)
	}),

	/** Khana (ogo) */
	OGO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(339000, "ogo", "Khana", null, null, null)
	}),

	/** Ojibwe (oji) */
	OJI(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(340000, "oji", "Ojibwe", "oj", null, null)
	}),

	/** Ombo (oml) - ISO 639-3 */
	OML(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(341000, "oml", "Ombo", null, null, null)
	}),

	/** Oriya (ori) */
	ORI(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(342000, "ori", "Oriya", "or", null, null)
	}),

	/** Oromo (orm) */
	ORM(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(343000, "orm", "Oromo", "om", null, null)
	}),

	/** Ossetisch (oss) */
	OSS(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(344000, "oss", "Ossetisch", "os", null, null)
	}),

	/** Panjabi (pan) */
	PAN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(345000, "pan", "Panjabi", "pa", null, null)
	}),

	/** Palauisch (pau) */
	PAU(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(346000, "pau", "Palauisch", null, null, null)
	}),

	/** Phende (pem) - ISO 639-3 */
	PEM(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(347000, "pem", "Phende", null, null, null)
	}),

	/** Persisch (pes) - ISO 639-3 */
	PES(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(348000, "pes", "Persisch", "fa", null, null)
	}),

	/** Pali (pli) */
	PLI(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(349000, "pli", "Pali", null, null, null)
	}),

	/** Polnisch (pol) */
	POL(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(350000, "pol", "Polnisch", "pl", null, null)
	}),

	/** Portugisisch (por) */
	POR(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(351000, "por", "Portugisisch", "pt", null, null)
	}),

	/** Dari (prs) - ISO 639-3 */
	PRS(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(352000, "prs", "Dari", null, null, null)
	}),

	/** Paschto (pus) */
	PUS(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(353000, "pus", "Paschto", "ps", null, null)
	}),

	/** Quechua (que) */
	QUE(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(354000, "que", "Quechua", "qu", null, null)
	}),

	/** Rajasthani (raj) */
	RAJ(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(355000, "raj", "Rajasthani", null, null, null)
	}),

	/** Ruund (rnd) - ISO 639-3 */
	RND(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(356000, "rnd", "Ruund", null, null, null)
	}),

	/** Rätoromanisch (roh) */
	ROH(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(357000, "roh", "Rätoromanisch", "rm", null, null)
	}),

	/** Romani (rom) */
	ROM(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(358000, "rom", "Romani", null, null, null)
	}),

	/** Rumänisch (ron) */
	RON(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(359000, "ron", "Rumänisch", "ro", null, null)
	}),

	/** Rundi (run) */
	RUN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(360000, "run", "Rundi", null, null, null)
	}),

	/** Russisch (rus) */
	RUS(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(361000, "rus", "Russisch", "ru", null, null)
	}),

	/** Sandawe (sad) */
	SAD(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(362000, "sad", "Sandawe", null, null, null)
	}),

	/** Sango (sag) */
	SAG(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(363000, "sag", "Sango", "sg", null, null)
	}),

	/** Jakutisch (sah) */
	SAH(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(364000, "sah", "Jakutisch", null, null, null)
	}),

	/** Sanskrit (san) */
	SAN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(365000, "san", "Sanskrit", null, null, null)
	}),

	/** Sasak (sas) */
	SAS(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(366000, "sas", "Sasak", null, null, null)
	}),

	/** Santali (sat) */
	SAT(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(367000, "sat", "Santali", null, null, null)
	}),

	/** Sangu (sbp) - ISO 639-3 */
	SBP(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(368000, "sbp", "Sangu", null, null, null)
	}),

	/** Sehwi (sfw) - ISO 639-3 */
	SFW(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(369000, "sfw", "Sehwi", null, null, null)
	}),

	/** Sebat Bet Gurage (sgw) - ISO 639-3 */
	SGW(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(370000, "sgw", "Sebat Bet Gurage", null, null, null)
	}),

	/** Taschelhit (shi) - ISO 639-3 */
	SHI(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(371000, "shi", "Taschelhit", null, null, null)
	}),

	/** Shilluk (shk) - ISO 639-3 */
	SHK(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(372000, "shk", "Shilluk", null, null, null)
	}),

	/** Schan (shn) */
	SHN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(373000, "shn", "Schan", null, null, null)
	}),

	/** Sidamo (sid) */
	SID(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(374000, "sid", "Sidamo", null, null, null)
	}),

	/** Singhalesisch (sin) */
	SIN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(375000, "sin", "Singhalesisch", "si", null, null)
	}),

	/** Sakata (skt) - ISO 639-3 */
	SKT(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(376000, "skt", "Sakata", null, null, null)
	}),

	/** Slowakisch (slk) */
	SLK(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(377000, "slk", "Slowakisch", "sk", null, null)
	}),

	/** Lamaholot (slp) - ISO 639-3 */
	SLP(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(378000, "slp", "Lamaholot", null, null, null)
	}),

	/** Slowenisch (slv) */
	SLV(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(379000, "slv", "Slowenisch", "sl", null, null)
	}),

	/** Nordsamisch (sme) */
	SME(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(380000, "sme", "Nordsamisch", "se", null, null)
	}),

	/** Samoanisch (smo) */
	SMO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(381000, "smo", "Samoanisch", "sm", null, null)
	}),

	/** Shona (sna) */
	SNA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(382000, "sna", "Shona", "sn", null, null)
	}),

	/** Sindhi (snd) */
	SND(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(383000, "snd", "Sindhi", "sd", null, null)
	}),

	/** Soninke (snk) */
	SNK(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(384000, "snk", "Soninke", null, null, null)
	}),

	/** Selee (snw) - ISO 639-3 */
	SNW(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(385000, "snw", "Selee", null, null, null)
	}),

	/** Somali (som) */
	SOM(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(386000, "som", "Somali", "so", null, null)
	}),

	/** Songhai (son) */
	SON(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(387000, "son", "Songhai", null, null, null)
	}),

	/** Songe (sop) - ISO 639-3 */
	SOP(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(388000, "sop", "Songe", null, null, null)
	}),

	/** Sotho-Süd (sot) */
	SOT(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(389000, "sot", "Sotho-Süd", "st", null, null)
	}),

	/** Spanisch (spa) */
	SPA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(390000, "spa", "Spanisch", "es", null, null)
	}),

	/** Albanisch (sqi) */
	SQI(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(391000, "sqi", "Albanisch", "sq", null, null)
	}),

	/** Sardisch (srd) */
	SRD(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(392000, "srd", "Sardisch", "sc", null, null)
	}),

	/** Serbisch (srp) */
	SRP(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(393000, "srp", "Serbisch", "sr", null, null)
	}),

	/** Serer (srr) */
	SRR(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(394000, "srr", "Serer", null, null, null)
	}),

	/** Swati (ssw) */
	SSW(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(395000, "ssw", "Swati", "ss", null, null)
	}),

	/** Saho (ssy) - ISO 639-3 */
	SSY(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(396000, "ssy", "Saho", null, null, null)
	}),

	/** Sukuma (suk) */
	SUK(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(397000, "suk", "Sukuma", null, null, null)
	}),

	/** Sundanesisch (sun) */
	SUN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(398000, "sun", "Sundanesisch", "su", null, null)
	}),

	/** Susu (sus) */
	SUS(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(399000, "sus", "Susu", null, null, null)
	}),

	/** Swahili (swa) */
	SWA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(400000, "swa", "Swahili", "sw", null, null)
	}),

	/** Schwedisch (swe) */
	SWE(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(401000, "swe", "Schwedisch", "sv", null, null)
	}),

	/** Sere (swf) - ISO 639-3 */
	SWF(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(402000, "swf", "Sere", null, null, null)
	}),

	/** Seki (syi) - ISO 639-3 */
	SYI(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(403000, "syi", "Seki", null, null, null)
	}),

	/** Tahitianisch (tah) */
	TAH(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(404000, "tah", "Tahitianisch", "ty", null, null)
	}),

	/** Tamil (tam) */
	TAM(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(405000, "tam", "Tamil", "ta", null, null)
	}),

	/** Tatarisch (tat) */
	TAT(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(406000, "tat", "Tatarisch", "tt", null, null)
	}),

	/** Teke-Tege (teg) - ISO 639-3 */
	TEG(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(407000, "teg", "Teke-Tege", null, null, null)
	}),

	/** Telugu (tel) */
	TEL(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(408000, "tel", "Telugu", "te", null, null)
	}),

	/** Temne (tem) */
	TEM(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(409000, "tem", "Temne", null, null, null)
	}),

	/** Tetum (tet) */
	TET(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(410000, "tet", "Tetum", null, null, null)
	}),

	/** Tadschikisch (tgk) */
	TGK(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(411000, "tgk", "Tadschikisch", "tg", null, null)
	}),

	/** Tagalog (tgl) */
	TGL(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(412000, "tgl", "Tagalog", "tl", null, null)
	}),

	/** Thailändisch (tha) */
	THA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(413000, "tha", "Thailändisch", "th", null, null)
	}),

	/** Thuri (thu) - ISO 639-3 */
	THU(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(414000, "thu", "Thuri", null, null, null)
	}),

	/** Tigre (tig) */
	TIG(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(415000, "tig", "Tigre", null, null, null)
	}),

	/** Tigrinya (tir) */
	TIR(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(416000, "tir", "Tigrinya", "ti", null, null)
	}),

	/** Tiv (tiv) */
	TIV(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(417000, "tiv", "Tiv", null, null, null)
	}),

	/** Tetela (tll) - ISO 639-3 */
	TLL(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(418000, "tll", "Tetela", null, null, null)
	}),

	/** Tamascheq (tmh) */
	TMH(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(419000, "tmh", "Tamascheq", null, null, null)
	}),

	/** Tonga (tog) */
	TOG(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(420000, "tog", "Tonga", "to", null, null)
	}),

	/** Tonga (toi) - ISO 639-3 */
	TOI(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(421000, "toi", "Tonga", "to", null, null)
	}),

	/** Tongaisch (ton) */
	TON(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(422000, "ton", "Tongaisch", "to", null, null)
	}),

	/** Tok Pisin (tpi) */
	TPI(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(423000, "tpi", "Tok Pisin", null, null, null)
	}),

	/** Tswana (tsn) */
	TSN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(424000, "tsn", "Tswana", "tn", null, null)
	}),

	/** Tsonga (tso) */
	TSO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(425000, "tso", "Tsonga", "ts", null, null)
	}),

	/** Turkmenisch (tuk) */
	TUK(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(426000, "tuk", "Turkmenisch", "tk", null, null)
	}),

	/** ChiTumbuka (tum) */
	TUM(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(427000, "tum", "ChiTumbuka", null, null, null)
	}),

	/** Türkisch (tur) */
	TUR(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(428000, "tur", "Türkisch", "tr", null, null)
	}),

	/** Altaisch (tut) */
	TUT(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(429000, "tut", "Altaisch", null, null, null)
	}),

	/** Tuvaluisch (tvl) */
	TVL(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(430000, "tvl", "Tuvaluisch", null, null, null)
	}),

	/** Twi (twi) */
	TWI(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(431000, "twi", "Twi", "tw", null, null)
	}),

	/** Tuwinisch (tyv) */
	TYV(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(432000, "tyv", "Tuwinisch", null, null, null)
	}),

	/** Udisch (udi) - ISO 639-3 */
	UDI(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(433000, "udi", "Udisch", null, null, null)
	}),

	/** Uigurisch (uig) */
	UIG(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(434000, "uig", "Uigurisch", "ug", null, null)
	}),

	/** Ukrainisch (ukr) */
	UKR(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(435000, "ukr", "Ukrainisch", "uk", null, null)
	}),

	/** Unbekannt (und) */
	UND(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(436000, "und", "Unbekannt", "xx", null, null)
	}),

	/** Urdu (urd) */
	URD(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(437000, "urd", "Urdu", "ur", null, null)
	}),

	/** Urhobo (urh) - ISO 639-3 */
	URH(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(438000, "urh", "Urhobo", null, null, null)
	}),

	/** Usbekisch (uzb) */
	UZB(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(439000, "uzb", "Usbekisch", "uz", null, null)
	}),

	/** Vai (vai) */
	VAI(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(440000, "vai", "Vai", null, null, null)
	}),

	/** Venda (ven) */
	VEN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(441000, "ven", "Venda", "ve", null, null)
	}),

	/** Vietnamesisch (vie) */
	VIE(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(442000, "vie", "Vietnamesisch", "vi", null, null)
	}),

	/** Makhuwa (vmw) - ISO 639-3 */
	VMW(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(443000, "vmw", "Makhuwa", null, null, null)
	}),

	/** Wotisch (vot) */
	VOT(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(444000, "vot", "Wotisch", null, null, null)
	}),

	/** Wolaytta (wal) */
	WAL(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(445000, "wal", "Wolaytta", null, null, null)
	}),

	/** Gbe, Waci (wci) - ISO 639-3 */
	WCI(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(446000, "wci", "Gbe, Waci", null, null, null)
	}),

	/** Wallonisch (wln) */
	WLN(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(447000, "wln", "Wallonisch", "wa", null, null)
	}),

	/** Wolof (wol) */
	WOL(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(448000, "wol", "Wolof", "wo", null, null)
	}),

	/** Wu-Chinesisch (wuu) - ISO 639-3 */
	WUU(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(449000, "wuu", "Wu-Chinesisch", null, null, null)
	}),

	/** Kalmückisch (xal) */
	XAL(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(450000, "xal", "Kalmückisch", null, null, null)
	}),

	/** Xhosa (xho) */
	XHO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(451000, "xho", "Xhosa", "xh", null, null)
	}),

	/** Soga (xog) - ISO 639-3 */
	XOG(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(452000, "xog", "Soga", null, null, null)
	}),

	/** Konkomba (xon) - ISO 639-3 */
	XON(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(453000, "xon", "Konkomba", null, null, null)
	}),

	/** Jalonke (yal) - ISO 639-3 */
	YAL(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(454000, "yal", "Jalonke", null, null, null)
	}),

	/** Yao (yao) */
	YAO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(455000, "yao", "Yao", null, null, null)
	}),

	/** Jiddisch (yid) */
	YID(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(456000, "yid", "Jiddisch", "yi", null, null)
	}),

	/** Yansi (yns) - ISO 639-3 */
	YNS(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(457000, "yns", "Yansi", null, null, null)
	}),

	/** Yombe (yom) - ISO 639-3 */
	YOM(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(458000, "yom", "Yombe", null, null, null)
	}),

	/** Yoruba (yor) */
	YOR(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(459000, "yor", "Yoruba", "yo", null, null)
	}),

	/** Kantonesisch (yue) - ISO 639-3 */
	YUE(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(460000, "yue", "Kantonesisch", null, null, null)
	}),

	/** Zhuang (zha) */
	ZHA(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(461000, "zha", "Zhuang", "za", null, null)
	}),

	/** Chinesisch (zho) */
	ZHO(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(462000, "zho", "Chinesisch", "zh", null, null)
	}),

	/** Zande (zne) - ISO 639-3 */
	ZNE(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(463000, "zne", "Zande", null, null, null)
	}),

	/** Zulu (zul) */
	ZUL(new VerkehrsspracheKatalogEintrag[] {
		new VerkehrsspracheKatalogEintrag(464000, "zul", "Zulu", "zu", null, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	
	
	/** Der aktuellen Daten der Verkehrssprache, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull VerkehrsspracheKatalogEintrag daten;
	
	/** Die Historie mit den Einträgen der Verkehrssprache */
	public final @NotNull VerkehrsspracheKatalogEintrag@NotNull[] historie;	

	/** Eine Hashmap mit allen definierten Verkehrssprachen, zugeordnet zu ihren Kürzeln */ 
	private static final @NotNull HashMap<@NotNull String, @NotNull Verkehrssprache> _sprachen = new HashMap<>();

	/** Eine Hashmap mit allen definierten Verkehrssprachen, zugeordnet zu ihren zweistelligen ISO 639-1-Codes */ 
	private static final @NotNull HashMap<@NotNull String, @NotNull Verkehrssprache> _kuerzel2 = new HashMap<>();
	

	/**
	 * Erzeugt eine neue Verkehrssprache in der Aufzählung.
	 * 
	 * @param historie   die Historie der Verkehrssprache, welches ein Array von {@link VerkehrsspracheKatalogEintrag} ist  
	 */
	private Verkehrssprache(@NotNull VerkehrsspracheKatalogEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist 
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt eine Map von den Kürzeln der Verkehrssprachen auf die zugehörigen Verkehrssprachen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln der Verkehrssprache auf die zugehörigen Verkehrssprache
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull Verkehrssprache> getMapSpracheByKuerzel() {
		if (_sprachen.size() == 0) {
			for (Verkehrssprache s : Verkehrssprache.values()) {
				if (s.daten != null)
					_sprachen.put(s.daten.kuerzel, s);
			}
		}
		return _sprachen;
	}


	/**
	 * Gibt eine Map von den zweistelligen Kürzeln der Verkehrssprachen auf die zugehörigen Verkehrssprachen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den zweistelligen Kürzeln der Verkehrssprache auf die zugehörigen Verkehrssprache
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull Verkehrssprache> getMapSpracheByKuerzel2() {
		if (_kuerzel2.size() == 0) {
			for (Verkehrssprache s : Verkehrssprache.values()) {
				if ((s.daten != null) && (s.daten.iso2 != null))
					_kuerzel2.put(s.daten.iso2, s);
			}
		}
		return _kuerzel2;
	}


	/**
	 * Gibt die Verkehrssprache für das angegebene Kürzel zurück.
	 * 
	 * @param kuerzel   das Kürzel der Verkehrssprache
	 * 
	 * @return die Verkehrssprache oder null, falls das Kürzel unbekannt ist
	 */
	public static Verkehrssprache getByKuerzel(String kuerzel) {
		return getMapSpracheByKuerzel().get(kuerzel);
	}


	/**
	 * Gibt die Verkehrssprache für das angegebene zweistellige ISO 639-1-Kürzel zurück.
	 * 
	 * @param kuerzel   das zweistellige Kürzel der Verkehrssprache
	 * 
	 * @return die Verkehrssprache oder null, falls das Kürzel unbekannt ist
	 */
	public static Verkehrssprache getByKuerzelISO2(String kuerzel) {
		return getMapSpracheByKuerzel2().get(kuerzel);
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
	public static Verkehrssprache getByKuerzelAuto(String kuerzel) {
		if (kuerzel == null)
			return null;
		if (kuerzel.length() == 2)
			return getMapSpracheByKuerzel2().get(kuerzel);
		return getMapSpracheByKuerzel().get(kuerzel);
	}

}
