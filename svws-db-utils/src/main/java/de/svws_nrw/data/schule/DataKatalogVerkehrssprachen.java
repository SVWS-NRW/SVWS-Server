package de.svws_nrw.data.schule;

import java.io.InputStream;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import de.svws_nrw.asd.data.schule.VerkehrsspracheKatalogEintrag;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link VerkehrsspracheKatalogEintrag}.
 */
public final class DataKatalogVerkehrssprachen extends DataManager<Long> {

	private static String daten = """
			[
			  {
			    "id": 1000,
			    "kuerzel": "deu",
			    "bezeichnung": "Deutsch",
			    "iso2": "de",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 2000,
			    "kuerzel": "aar",
			    "bezeichnung": "Afar",
			    "iso2": "aa",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 3000,
			    "kuerzel": "abk",
			    "bezeichnung": "Abchasisch",
			    "iso2": "ab",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 4000,
			    "kuerzel": "afr",
			    "bezeichnung": "Afrikaans",
			    "iso2": "af",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 5000,
			    "kuerzel": "aka",
			    "bezeichnung": "Akan",
			    "iso2": "ak",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 6000,
			    "kuerzel": "amh",
			    "bezeichnung": "Amharisch",
			    "iso2": "am",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 7000,
			    "kuerzel": "ara",
			    "bezeichnung": "Arabisch",
			    "iso2": "ar",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 8000,
			    "kuerzel": "arg",
			    "bezeichnung": "Aragonesisch",
			    "iso2": "an",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 9000,
			    "kuerzel": "asm",
			    "bezeichnung": "Assamesisch",
			    "iso2": "as",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 10000,
			    "kuerzel": "ava",
			    "bezeichnung": "Awarisch",
			    "iso2": "av",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 11000,
			    "kuerzel": "aym",
			    "bezeichnung": "Aymara",
			    "iso2": "ay",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 12000,
			    "kuerzel": "aze",
			    "bezeichnung": "Aserbaidschanisch",
			    "iso2": "az",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 13000,
			    "kuerzel": "bak",
			    "bezeichnung": "Baschkirisch",
			    "iso2": "ba",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 14000,
			    "kuerzel": "bam",
			    "bezeichnung": "Bambara",
			    "iso2": "bm",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 15000,
			    "kuerzel": "bel",
			    "bezeichnung": "Weißrussisch",
			    "iso2": "be",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 16000,
			    "kuerzel": "ben",
			    "bezeichnung": "Bengali",
			    "iso2": "bn",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 17000,
			    "kuerzel": "bih",
			    "bezeichnung": "Bihari",
			    "iso2": "bh",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 18000,
			    "kuerzel": "bis",
			    "bezeichnung": "Bislama",
			    "iso2": "bi",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 19000,
			    "kuerzel": "bod",
			    "bezeichnung": "Tibetisch",
			    "iso2": "bo",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 20000,
			    "kuerzel": "bos",
			    "bezeichnung": "Bosnisch",
			    "iso2": "bs",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 21000,
			    "kuerzel": "bre",
			    "bezeichnung": "Bretonisch",
			    "iso2": "br",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 22000,
			    "kuerzel": "bul",
			    "bezeichnung": "Bulgarisch",
			    "iso2": "bg",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 23000,
			    "kuerzel": "cat",
			    "bezeichnung": "Katalanisch",
			    "iso2": "ca",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 24000,
			    "kuerzel": "ces",
			    "bezeichnung": "Tschechisch",
			    "iso2": "cs",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 25000,
			    "kuerzel": "che",
			    "bezeichnung": "Tschetschenisch",
			    "iso2": "ce",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 26000,
			    "kuerzel": "chv",
			    "bezeichnung": "Tschuwaschisch",
			    "iso2": "cv",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 27000,
			    "kuerzel": "cor",
			    "bezeichnung": "Kornisch",
			    "iso2": "kw",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 28000,
			    "kuerzel": "cos",
			    "bezeichnung": "Korsisch",
			    "iso2": "co",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 29000,
			    "kuerzel": "cre",
			    "bezeichnung": "Cree",
			    "iso2": "cr",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 30000,
			    "kuerzel": "cym",
			    "bezeichnung": "Walisisch",
			    "iso2": "cy",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 31000,
			    "kuerzel": "dan",
			    "bezeichnung": "Dänisch",
			    "iso2": "da",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 32000,
			    "kuerzel": "div",
			    "bezeichnung": "Maledivisch",
			    "iso2": "dv",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 33000,
			    "kuerzel": "dzo",
			    "bezeichnung": "Dzongkha",
			    "iso2": "dz",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 34000,
			    "kuerzel": "ell",
			    "bezeichnung": "Griechisch",
			    "iso2": "el",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 35000,
			    "kuerzel": "eng",
			    "bezeichnung": "Englisch",
			    "iso2": "en",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 36000,
			    "kuerzel": "est",
			    "bezeichnung": "Estnisch",
			    "iso2": "et",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 37000,
			    "kuerzel": "eus",
			    "bezeichnung": "Baskisch",
			    "iso2": "eu",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 38000,
			    "kuerzel": "ewe",
			    "bezeichnung": "Ewe",
			    "iso2": "ee",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 39000,
			    "kuerzel": "fao",
			    "bezeichnung": "Färöisch",
			    "iso2": "fo",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 40000,
			    "kuerzel": "fij",
			    "bezeichnung": "Fidschi",
			    "iso2": "fj",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 41000,
			    "kuerzel": "fin",
			    "bezeichnung": "Finnisch",
			    "iso2": "fi",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 42000,
			    "kuerzel": "fra",
			    "bezeichnung": "Französisch",
			    "iso2": "fr",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 43000,
			    "kuerzel": "fry",
			    "bezeichnung": "Westfriesisch",
			    "iso2": "fy",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 44000,
			    "kuerzel": "ful",
			    "bezeichnung": "Fulfulde",
			    "iso2": "ff",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 45000,
			    "kuerzel": "gla",
			    "bezeichnung": "Schottisch-gälisch",
			    "iso2": "gd",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 46000,
			    "kuerzel": "gle",
			    "bezeichnung": "Irisch",
			    "iso2": "ga",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 47000,
			    "kuerzel": "glg",
			    "bezeichnung": "Galicisch",
			    "iso2": "gl",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 48000,
			    "kuerzel": "grn",
			    "bezeichnung": "Guarani",
			    "iso2": "gn",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 49000,
			    "kuerzel": "guj",
			    "bezeichnung": "Gujarati",
			    "iso2": "gu",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 50000,
			    "kuerzel": "hat",
			    "bezeichnung": "Haitianisch",
			    "iso2": "ht",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 51000,
			    "kuerzel": "hau",
			    "bezeichnung": "Hausa",
			    "iso2": "ha",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 52000,
			    "kuerzel": "heb",
			    "bezeichnung": "Hebräisch",
			    "iso2": "he",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 53000,
			    "kuerzel": "her",
			    "bezeichnung": "Herero",
			    "iso2": "hz",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 54000,
			    "kuerzel": "hin",
			    "bezeichnung": "Hindi",
			    "iso2": "hi",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 55000,
			    "kuerzel": "hmo",
			    "bezeichnung": "Hiri Motu",
			    "iso2": "ho",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 56000,
			    "kuerzel": "hrv",
			    "bezeichnung": "Kroatisch",
			    "iso2": "hr",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 57000,
			    "kuerzel": "hun",
			    "bezeichnung": "Ungarisch",
			    "iso2": "hu",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 58000,
			    "kuerzel": "hye",
			    "bezeichnung": "Armenisch",
			    "iso2": "hy",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 59000,
			    "kuerzel": "iii",
			    "bezeichnung": "Sichuan-Yi",
			    "iso2": "ii",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 60000,
			    "kuerzel": "iku",
			    "bezeichnung": "Inuktitut",
			    "iso2": "iu",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 61000,
			    "kuerzel": "ind",
			    "bezeichnung": "Indonesisch",
			    "iso2": "id",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 62000,
			    "kuerzel": "ipk",
			    "bezeichnung": "Inupiaq",
			    "iso2": "ik",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 63000,
			    "kuerzel": "isl",
			    "bezeichnung": "Isländisch",
			    "iso2": "is",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 64000,
			    "kuerzel": "ita",
			    "bezeichnung": "Italienisch",
			    "iso2": "it",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 65000,
			    "kuerzel": "jav",
			    "bezeichnung": "Javanisch",
			    "iso2": "jv",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 66000,
			    "kuerzel": "jpn",
			    "bezeichnung": "Japanisch",
			    "iso2": "ja",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 67000,
			    "kuerzel": "kal",
			    "bezeichnung": "Grönländisch",
			    "iso2": "kl",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 68000,
			    "kuerzel": "kan",
			    "bezeichnung": "Kannada",
			    "iso2": "kn",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 69000,
			    "kuerzel": "kas",
			    "bezeichnung": "Kashmiri",
			    "iso2": "ks",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 70000,
			    "kuerzel": "kat",
			    "bezeichnung": "Georgisch",
			    "iso2": "ka",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 71000,
			    "kuerzel": "kau",
			    "bezeichnung": "Kanuri",
			    "iso2": "kr",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 72000,
			    "kuerzel": "kaz",
			    "bezeichnung": "Kasachisch",
			    "iso2": "kk",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 73000,
			    "kuerzel": "khm",
			    "bezeichnung": "Khmer",
			    "iso2": "km",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 74000,
			    "kuerzel": "kik",
			    "bezeichnung": "Kikuyu",
			    "iso2": "ki",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 75000,
			    "kuerzel": "kin",
			    "bezeichnung": "Kinyarwanda",
			    "iso2": "rw",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 76000,
			    "kuerzel": "kir",
			    "bezeichnung": "Kirgisisch",
			    "iso2": "ky",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 77000,
			    "kuerzel": "kom",
			    "bezeichnung": "Komi",
			    "iso2": "kv",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 78000,
			    "kuerzel": "kon",
			    "bezeichnung": "Kongo",
			    "iso2": "kg",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 79000,
			    "kuerzel": "kor",
			    "bezeichnung": "Koreanisch",
			    "iso2": "ko",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 80000,
			    "kuerzel": "kua",
			    "bezeichnung": "Kuanyama",
			    "iso2": "kj",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 81000,
			    "kuerzel": "kur",
			    "bezeichnung": "Kurdisch",
			    "iso2": "ku",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 82000,
			    "kuerzel": "lao",
			    "bezeichnung": "Laotisch",
			    "iso2": "lo",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 83000,
			    "kuerzel": "lav",
			    "bezeichnung": "Lettisch",
			    "iso2": "lv",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 84000,
			    "kuerzel": "lim",
			    "bezeichnung": "Limburgisch",
			    "iso2": "li",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 85000,
			    "kuerzel": "lin",
			    "bezeichnung": "Lingala",
			    "iso2": "ln",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 86000,
			    "kuerzel": "lit",
			    "bezeichnung": "Litauisch",
			    "iso2": "lt",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 87000,
			    "kuerzel": "ltz",
			    "bezeichnung": "Luxemburgisch",
			    "iso2": "lb",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 88000,
			    "kuerzel": "lub",
			    "bezeichnung": "Luba-Katanga",
			    "iso2": "lu",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 89000,
			    "kuerzel": "lug",
			    "bezeichnung": "Luganda",
			    "iso2": "lg",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 90000,
			    "kuerzel": "mah",
			    "bezeichnung": "Marshallesisch",
			    "iso2": "mh",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 91000,
			    "kuerzel": "mal",
			    "bezeichnung": "Malayalam",
			    "iso2": "ml",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 92000,
			    "kuerzel": "mar",
			    "bezeichnung": "Marathi",
			    "iso2": "mr",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 93000,
			    "kuerzel": "mkd",
			    "bezeichnung": "Mazedonisch",
			    "iso2": "mk",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 94000,
			    "kuerzel": "mlg",
			    "bezeichnung": "Malagasy",
			    "iso2": "mg",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 95000,
			    "kuerzel": "mlt",
			    "bezeichnung": "Maltesisch",
			    "iso2": "mt",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 96000,
			    "kuerzel": "mol",
			    "bezeichnung": "Moldawisch",
			    "iso2": "mo",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 97000,
			    "kuerzel": "mon",
			    "bezeichnung": "Mongolisch",
			    "iso2": "mn",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 98000,
			    "kuerzel": "mri",
			    "bezeichnung": "Maori",
			    "iso2": "mi",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 99000,
			    "kuerzel": "msa",
			    "bezeichnung": "Malaiisch",
			    "iso2": "ms",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 100000,
			    "kuerzel": "mya",
			    "bezeichnung": "Burmesisch",
			    "iso2": "my",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 101000,
			    "kuerzel": "nau",
			    "bezeichnung": "Nauruanisch",
			    "iso2": "na",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 102000,
			    "kuerzel": "nav",
			    "bezeichnung": "Navajo",
			    "iso2": "nv",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 103000,
			    "kuerzel": "nbl",
			    "bezeichnung": "Ndebele-Süd",
			    "iso2": "nr",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 104000,
			    "kuerzel": "nde",
			    "bezeichnung": "Ndebele-Nord",
			    "iso2": "nd",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 105000,
			    "kuerzel": "ndo",
			    "bezeichnung": "Ndonga",
			    "iso2": "ng",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 106000,
			    "kuerzel": "nep",
			    "bezeichnung": "Nepali",
			    "iso2": "ne",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 107000,
			    "kuerzel": "nld",
			    "bezeichnung": "Niederländisch",
			    "iso2": "nl",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 108000,
			    "kuerzel": "nor",
			    "bezeichnung": "Norwegisch",
			    "iso2": "no",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 109000,
			    "kuerzel": "nya",
			    "bezeichnung": "Nyanja",
			    "iso2": "ny",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 110000,
			    "kuerzel": "oci",
			    "bezeichnung": "Okzitanisch",
			    "iso2": "oc",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 111000,
			    "kuerzel": "oji",
			    "bezeichnung": "Ojibwe",
			    "iso2": "oj",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 112000,
			    "kuerzel": "ori",
			    "bezeichnung": "Oriya",
			    "iso2": "or",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 113000,
			    "kuerzel": "orm",
			    "bezeichnung": "Oromo",
			    "iso2": "om",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 114000,
			    "kuerzel": "oss",
			    "bezeichnung": "Ossetisch",
			    "iso2": "os",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 115000,
			    "kuerzel": "pan",
			    "bezeichnung": "Panjabi",
			    "iso2": "pa",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 116000,
			    "kuerzel": "pes",
			    "bezeichnung": "Persisch",
			    "iso2": "fa",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 117000,
			    "kuerzel": "pol",
			    "bezeichnung": "Polnisch",
			    "iso2": "pl",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 118000,
			    "kuerzel": "por",
			    "bezeichnung": "Portugisisch",
			    "iso2": "pt",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 119000,
			    "kuerzel": "pus",
			    "bezeichnung": "Paschto",
			    "iso2": "ps",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 120000,
			    "kuerzel": "que",
			    "bezeichnung": "Quechua",
			    "iso2": "qu",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 121000,
			    "kuerzel": "roh",
			    "bezeichnung": "Rätoromanisch",
			    "iso2": "rm",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 122000,
			    "kuerzel": "ron",
			    "bezeichnung": "Rumänisch",
			    "iso2": "ro",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 123000,
			    "kuerzel": "rus",
			    "bezeichnung": "Russisch",
			    "iso2": "ru",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 124000,
			    "kuerzel": "sag",
			    "bezeichnung": "Sango",
			    "iso2": "sg",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 125000,
			    "kuerzel": "sin",
			    "bezeichnung": "Singhalesisch",
			    "iso2": "si",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 126000,
			    "kuerzel": "slk",
			    "bezeichnung": "Slowakisch",
			    "iso2": "sk",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 127000,
			    "kuerzel": "slv",
			    "bezeichnung": "Slowenisch",
			    "iso2": "sl",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 128000,
			    "kuerzel": "sme",
			    "bezeichnung": "Nordsamisch",
			    "iso2": "se",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 129000,
			    "kuerzel": "smo",
			    "bezeichnung": "Samoanisch",
			    "iso2": "sm",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 130000,
			    "kuerzel": "sna",
			    "bezeichnung": "Shona",
			    "iso2": "sn",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 131000,
			    "kuerzel": "snd",
			    "bezeichnung": "Sindhi",
			    "iso2": "sd",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 132000,
			    "kuerzel": "som",
			    "bezeichnung": "Somali",
			    "iso2": "so",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 133000,
			    "kuerzel": "sot",
			    "bezeichnung": "Sotho-Süd",
			    "iso2": "st",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 134000,
			    "kuerzel": "spa",
			    "bezeichnung": "Spanisch",
			    "iso2": "es",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 135000,
			    "kuerzel": "sqi",
			    "bezeichnung": "Albanisch",
			    "iso2": "sq",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 136000,
			    "kuerzel": "srd",
			    "bezeichnung": "Sardisch",
			    "iso2": "sc",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 137000,
			    "kuerzel": "srp",
			    "bezeichnung": "Serbisch",
			    "iso2": "sr",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 138000,
			    "kuerzel": "ssw",
			    "bezeichnung": "Swati",
			    "iso2": "ss",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 139000,
			    "kuerzel": "sun",
			    "bezeichnung": "Sundanesisch",
			    "iso2": "su",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 140000,
			    "kuerzel": "swa",
			    "bezeichnung": "Swahili",
			    "iso2": "sw",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 141000,
			    "kuerzel": "swe",
			    "bezeichnung": "Schwedisch",
			    "iso2": "sv",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 142000,
			    "kuerzel": "tah",
			    "bezeichnung": "Tahitianisch",
			    "iso2": "ty",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 143000,
			    "kuerzel": "tam",
			    "bezeichnung": "Tamil",
			    "iso2": "ta",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 144000,
			    "kuerzel": "tat",
			    "bezeichnung": "Tatarisch",
			    "iso2": "tt",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 145000,
			    "kuerzel": "tel",
			    "bezeichnung": "Telugu",
			    "iso2": "te",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 146000,
			    "kuerzel": "tgk",
			    "bezeichnung": "Tadschikisch",
			    "iso2": "tg",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 147000,
			    "kuerzel": "tgl",
			    "bezeichnung": "Tagalog",
			    "iso2": "tl",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 148000,
			    "kuerzel": "tha",
			    "bezeichnung": "Thailändisch",
			    "iso2": "th",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 149000,
			    "kuerzel": "tir",
			    "bezeichnung": "Tigrinya",
			    "iso2": "ti",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 150000,
			    "kuerzel": "tog",
			    "bezeichnung": "Tonga",
			    "iso2": "to",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 151000,
			    "kuerzel": "toi",
			    "bezeichnung": "Tonga",
			    "iso2": "to",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 152000,
			    "kuerzel": "ton",
			    "bezeichnung": "Tongaisch",
			    "iso2": "to",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 153000,
			    "kuerzel": "tsn",
			    "bezeichnung": "Tswana",
			    "iso2": "tn",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 154000,
			    "kuerzel": "tso",
			    "bezeichnung": "Tsonga",
			    "iso2": "ts",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 155000,
			    "kuerzel": "tuk",
			    "bezeichnung": "Turkmenisch",
			    "iso2": "tk",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 156000,
			    "kuerzel": "tur",
			    "bezeichnung": "Türkisch",
			    "iso2": "tr",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 157000,
			    "kuerzel": "twi",
			    "bezeichnung": "Twi",
			    "iso2": "tw",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 158000,
			    "kuerzel": "uig",
			    "bezeichnung": "Uigurisch",
			    "iso2": "ug",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 159000,
			    "kuerzel": "ukr",
			    "bezeichnung": "Ukrainisch",
			    "iso2": "uk",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 160000,
			    "kuerzel": "urd",
			    "bezeichnung": "Urdu",
			    "iso2": "ur",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 161000,
			    "kuerzel": "uzb",
			    "bezeichnung": "Usbekisch",
			    "iso2": "uz",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 162000,
			    "kuerzel": "ven",
			    "bezeichnung": "Venda",
			    "iso2": "ve",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 163000,
			    "kuerzel": "vie",
			    "bezeichnung": "Vietnamesisch",
			    "iso2": "vi",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 164000,
			    "kuerzel": "wln",
			    "bezeichnung": "Wallonisch",
			    "iso2": "wa",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 165000,
			    "kuerzel": "wol",
			    "bezeichnung": "Wolof",
			    "iso2": "wo",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 166000,
			    "kuerzel": "xho",
			    "bezeichnung": "Xhosa",
			    "iso2": "xh",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 167000,
			    "kuerzel": "yid",
			    "bezeichnung": "Jiddisch",
			    "iso2": "yi",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 168000,
			    "kuerzel": "yor",
			    "bezeichnung": "Yoruba",
			    "iso2": "yo",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 169000,
			    "kuerzel": "zha",
			    "bezeichnung": "Zhuang",
			    "iso2": "za",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 170000,
			    "kuerzel": "zho",
			    "bezeichnung": "Chinesisch",
			    "iso2": "zh",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 171000,
			    "kuerzel": "zul",
			    "bezeichnung": "Zulu",
			    "iso2": "zu",
			    "gueltigVon": null,
			    "gueltigBis": null
			  },
			  {
			    "id": 0,
			    "kuerzel": "und",
			    "bezeichnung": "Unbekannt",
			    "iso2": "xx",
			    "gueltigVon": null,
			    "gueltigBis": null
			  }
			]
			""";

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link VerkehrsspracheKatalogEintrag}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKatalogVerkehrssprachen(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() {
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() {
		return this.getAll();
	}

	@Override
	public Response get(final Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
