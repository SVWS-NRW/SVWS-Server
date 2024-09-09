package de.svws_nrw.asd.utils;


import de.svws_nrw.asd.data.NoteKatalogEintrag;
import de.svws_nrw.asd.data.fach.BilingualeSpracheKatalogEintrag;
import de.svws_nrw.asd.data.fach.FachKatalogEintrag;
import de.svws_nrw.asd.data.fach.FachgruppeKatalogEintrag;
import de.svws_nrw.asd.data.fach.SprachreferenzniveauKatalogEintrag;
import de.svws_nrw.asd.data.jahrgang.JahrgaengeKatalogEintrag;
import de.svws_nrw.asd.data.jahrgang.PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag;
import de.svws_nrw.asd.data.kaoa.KAOAAnschlussoptionenKatalogEintrag;
import de.svws_nrw.asd.data.kaoa.KAOABerufsfeldKatalogEintrag;
import de.svws_nrw.asd.data.kaoa.KAOAEbene4KatalogEintrag;
import de.svws_nrw.asd.data.kaoa.KAOAKategorieKatalogEintrag;
import de.svws_nrw.asd.data.kaoa.KAOAMerkmalKatalogEintrag;
import de.svws_nrw.asd.data.kaoa.KAOAMerkmaleOptionsartenKatalogEintrag;
import de.svws_nrw.asd.data.kaoa.KAOAZusatzmerkmalKatalogEintrag;
import de.svws_nrw.asd.data.kaoa.KAOAZusatzmerkmaleOptionsartenKatalogEintrag;
import de.svws_nrw.asd.data.klassen.KlassenartKatalogEintrag;
import de.svws_nrw.asd.data.kurse.ZulaessigeKursartKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerAbgangsgrundKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerAnrechnungsgrundKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerBeschaeftigungsartKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerEinsatzstatusKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungAnerkennungKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehramtAnerkennungKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehramtKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungAnerkennungKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLeitungsfunktionKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerMehrleistungsartKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerMinderleistungsartKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerRechtsverhaeltnisKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerZugangsgrundKatalogEintrag;
import de.svws_nrw.asd.data.schueler.HerkunftBildungsgangKatalogEintrag;
import de.svws_nrw.asd.data.schueler.HerkunftBildungsgangTypKatalogEintrag;
import de.svws_nrw.asd.data.schueler.SchuelerStatusKatalogEintrag;
import de.svws_nrw.asd.data.schueler.UebergangsempfehlungKatalogEintrag;
import de.svws_nrw.asd.data.schule.BerufskollegAnlageKatalogEintrag;
import de.svws_nrw.asd.data.schule.BildungsgangTypKatalogEintrag;
import de.svws_nrw.asd.data.schule.FoerderschwerpunktKatalogEintrag;
import de.svws_nrw.asd.data.schule.KindergartenbesuchKatalogEintrag;
import de.svws_nrw.asd.data.schule.OrganisationsformKatalogEintrag;
import de.svws_nrw.asd.data.schule.ReligionKatalogEintrag;
import de.svws_nrw.asd.data.schule.SchulabschlussAllgemeinbildendKatalogEintrag;
import de.svws_nrw.asd.data.schule.SchulabschlussBerufsbildendKatalogEintrag;
import de.svws_nrw.asd.data.schule.SchulformKatalogEintrag;
import de.svws_nrw.asd.data.schule.SchulgliederungKatalogEintrag;
import de.svws_nrw.asd.types.CoreTypeSimple;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.asd.types.fach.BilingualeSprache;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.asd.types.fach.Fachgruppe;
import de.svws_nrw.asd.types.fach.Sprachreferenzniveau;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.jahrgang.PrimarstufeSchuleingangsphaseBesuchsjahre;
import de.svws_nrw.asd.types.kaoa.KAOAAnschlussoptionen;
import de.svws_nrw.asd.types.kaoa.KAOABerufsfeld;
import de.svws_nrw.asd.types.kaoa.KAOAEbene4;
import de.svws_nrw.asd.types.kaoa.KAOAKategorie;
import de.svws_nrw.asd.types.kaoa.KAOAMerkmal;
import de.svws_nrw.asd.types.kaoa.KAOAMerkmaleOptionsarten;
import de.svws_nrw.asd.types.kaoa.KAOAZusatzmerkmal;
import de.svws_nrw.asd.types.kaoa.KAOAZusatzmerkmaleOptionsarten;
import de.svws_nrw.asd.types.klassen.Klassenart;
import de.svws_nrw.asd.types.kurse.ZulaessigeKursart;
import de.svws_nrw.asd.types.lehrer.LehrerAbgangsgrund;
import de.svws_nrw.asd.types.lehrer.LehrerAnrechnungsgrund;
import de.svws_nrw.asd.types.lehrer.LehrerBeschaeftigungsart;
import de.svws_nrw.asd.types.lehrer.LehrerEinsatzstatus;
import de.svws_nrw.asd.types.lehrer.LehrerFachrichtung;
import de.svws_nrw.asd.types.lehrer.LehrerFachrichtungAnerkennung;
import de.svws_nrw.asd.types.lehrer.LehrerLehramt;
import de.svws_nrw.asd.types.lehrer.LehrerLehramtAnerkennung;
import de.svws_nrw.asd.types.lehrer.LehrerLehrbefaehigung;
import de.svws_nrw.asd.types.lehrer.LehrerLehrbefaehigungAnerkennung;
import de.svws_nrw.asd.types.lehrer.LehrerLeitungsfunktion;
import de.svws_nrw.asd.types.lehrer.LehrerMehrleistungsarten;
import de.svws_nrw.asd.types.lehrer.LehrerMinderleistungsarten;
import de.svws_nrw.asd.types.lehrer.LehrerRechtsverhaeltnis;
import de.svws_nrw.asd.types.lehrer.LehrerZugangsgrund;
import de.svws_nrw.asd.types.schueler.HerkunftBildungsgang;
import de.svws_nrw.asd.types.schueler.HerkunftBildungsgangTyp;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.asd.types.schueler.Uebergangsempfehlung;
import de.svws_nrw.asd.types.schule.AllgemeinbildendOrganisationsformen;
import de.svws_nrw.asd.types.schule.BerufskollegAnlage;
import de.svws_nrw.asd.types.schule.BerufskollegBildungsgangTyp;
import de.svws_nrw.asd.types.schule.BerufskollegOrganisationsformen;
import de.svws_nrw.asd.types.schule.Foerderschwerpunkt;
import de.svws_nrw.asd.types.schule.Kindergartenbesuch;
import de.svws_nrw.asd.types.schule.Religion;
import de.svws_nrw.asd.types.schule.SchulabschlussAllgemeinbildend;
import de.svws_nrw.asd.types.schule.SchulabschlussBerufsbildend;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.types.schule.WeiterbildungskollegBildungsgangTyp;
import de.svws_nrw.asd.types.schule.WeiterbildungskollegOrganisationsformen;
import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.validate.ValidatorManager;


/**
 * Diese Klasse stellt allgemeine Hilfsmethoden für die ASD-Core-Types zur Verfügung.
 */
public final class ASDCoreTypeUtils {

	private ASDCoreTypeUtils() {
		throw new IllegalStateException("Instantiation not allowed");
	}

	/**
	 * Initialisiert alle Core-Types für die amtlichen Schuldaten.
	 */
	public static void initAll() {
		// Core-Type Schulform
		final var dataSchulform = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/schule/Schulform.json", SchulformKatalogEintrag.class);
		Schulform.init(new CoreTypeDataManager<>(dataSchulform.getVersion(), Schulform.class, Schulform.values(), dataSchulform.getData(), dataSchulform.getHistorienIDs()));
		// Core-Type BerufskollegAnlage
		final var dataBerufskollegAnlage = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/schule/BerufskollegAnlage.json", BerufskollegAnlageKatalogEintrag.class);
		BerufskollegAnlage.init(new CoreTypeDataManager<>(dataBerufskollegAnlage.getVersion(), BerufskollegAnlage.class, BerufskollegAnlage.values(), dataBerufskollegAnlage.getData(), dataBerufskollegAnlage.getHistorienIDs()));
		// Core-Type AllgemeinbildendOrganisationsform
		final var dataAllgemeinbildendOrganisationsform = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/schule/AllgemeinbildendOrganisationsformen.json", OrganisationsformKatalogEintrag.class);
		AllgemeinbildendOrganisationsformen.init(new CoreTypeDataManager<>(dataAllgemeinbildendOrganisationsform.getVersion(), AllgemeinbildendOrganisationsformen.class, AllgemeinbildendOrganisationsformen.values(), dataAllgemeinbildendOrganisationsform.getData(), dataAllgemeinbildendOrganisationsform.getHistorienIDs()));
		// Core-Type BerufskollegOrganisationsform
		final var dataBerufskollegOrganisationsform = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/schule/BerufskollegOrganisationsformen.json", OrganisationsformKatalogEintrag.class);
		BerufskollegOrganisationsformen.init(new CoreTypeDataManager<>(dataBerufskollegOrganisationsform.getVersion(), BerufskollegOrganisationsformen.class, BerufskollegOrganisationsformen.values(), dataBerufskollegOrganisationsform.getData(), dataBerufskollegOrganisationsform.getHistorienIDs()));
		// Core-Type WeiterbildungskollegOrganisationsform
		final var dataWeiterbildungskollegOrganisationsform = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/schule/WeiterbildungskollegOrganisationsformen.json", OrganisationsformKatalogEintrag.class);
		WeiterbildungskollegOrganisationsformen.init(new CoreTypeDataManager<>(dataWeiterbildungskollegOrganisationsform.getVersion(), WeiterbildungskollegOrganisationsformen.class, WeiterbildungskollegOrganisationsformen.values(), dataWeiterbildungskollegOrganisationsform.getData(), dataWeiterbildungskollegOrganisationsform.getHistorienIDs()));
		// Core-Type SchulabschlussAllgemeinbildend
		final var dataSchulabschlussAllgemeinbildend = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/schule/SchulabschlussAllgemeinbildend.json", SchulabschlussAllgemeinbildendKatalogEintrag.class);
		SchulabschlussAllgemeinbildend.init(new CoreTypeDataManager<>(dataSchulabschlussAllgemeinbildend.getVersion(), SchulabschlussAllgemeinbildend.class, SchulabschlussAllgemeinbildend.values(), dataSchulabschlussAllgemeinbildend.getData(), dataSchulabschlussAllgemeinbildend.getHistorienIDs()));
		// Core-Type SchulabschlussBerufsbildend
		final var dataSchulabschlussBerufsbildend = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/schule/SchulabschlussBerufsbildend.json", SchulabschlussBerufsbildendKatalogEintrag.class);
		SchulabschlussBerufsbildend.init(new CoreTypeDataManager<>(dataSchulabschlussBerufsbildend.getVersion(), SchulabschlussBerufsbildend.class, SchulabschlussBerufsbildend.values(), dataSchulabschlussBerufsbildend.getData(), dataSchulabschlussBerufsbildend.getHistorienIDs()));

		// Core-Type HerkunftBildungsgang
		final var dataHerkunftBildungsgang = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/schueler/HerkunftBildungsgang.json", HerkunftBildungsgangKatalogEintrag.class);
		HerkunftBildungsgang.init(new CoreTypeDataManager<>(dataHerkunftBildungsgang.getVersion(), HerkunftBildungsgang.class, HerkunftBildungsgang.values(), dataHerkunftBildungsgang.getData(), dataHerkunftBildungsgang.getHistorienIDs()));
		// Core-Type HerkunftBildungsgangTyp
		final var dataHerkunftBildungsgangTyp = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/schueler/HerkunftBildungsgangTyp.json", HerkunftBildungsgangTypKatalogEintrag.class);
		HerkunftBildungsgangTyp.init(new CoreTypeDataManager<>(dataHerkunftBildungsgangTyp.getVersion(), HerkunftBildungsgangTyp.class, HerkunftBildungsgangTyp.values(), dataHerkunftBildungsgangTyp.getData(), dataHerkunftBildungsgangTyp.getHistorienIDs()));

		// Core-Type Jahrgang
		final var dataJahrgang = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/jahrgang/Jahrgaenge.json", JahrgaengeKatalogEintrag.class);
		Jahrgaenge.init(new CoreTypeDataManager<>(dataJahrgang.getVersion(), Jahrgaenge.class, Jahrgaenge.values(), dataJahrgang.getData(), dataJahrgang.getHistorienIDs()));
		// Core-Type PrimarstufeSchuleingangsphaseBesuchsjahr
		final var dataPrimarstufeSchuleingangsphaseBesuchsjahr = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/jahrgang/PrimarstufeSchuleingangsphaseBesuchsjahre.json", PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag.class);
		PrimarstufeSchuleingangsphaseBesuchsjahre.init(new CoreTypeDataManager<>(dataPrimarstufeSchuleingangsphaseBesuchsjahr.getVersion(), PrimarstufeSchuleingangsphaseBesuchsjahre.class, PrimarstufeSchuleingangsphaseBesuchsjahre.values(), dataPrimarstufeSchuleingangsphaseBesuchsjahr.getData(), dataPrimarstufeSchuleingangsphaseBesuchsjahr.getHistorienIDs()));
		// Core-Type Religion
		final var dataReligion = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/schule/Religion.json", ReligionKatalogEintrag.class);
		Religion.init(new CoreTypeDataManager<>(dataReligion.getVersion(), Religion.class, Religion.values(), dataReligion.getData(), dataReligion.getHistorienIDs()));
		// Core-Type Kindergartenbesuch
		final var dataKindergartenbesuch = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/schule/Kindergartenbesuch.json", KindergartenbesuchKatalogEintrag.class);
		Kindergartenbesuch.init(new CoreTypeDataManager<>(dataKindergartenbesuch.getVersion(), Kindergartenbesuch.class, Kindergartenbesuch.values(), dataKindergartenbesuch.getData(), dataKindergartenbesuch.getHistorienIDs()));
		// Core-Type SchuelerStatus
		final var dataSchuelerStatus = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/schueler/SchuelerStatus.json", SchuelerStatusKatalogEintrag.class);
		SchuelerStatus.init(new CoreTypeDataManager<>(dataSchuelerStatus.getVersion(), SchuelerStatus.class, SchuelerStatus.values(), dataSchuelerStatus.getData(), dataSchuelerStatus.getHistorienIDs()));
		// Core-Type Note
		final var dataNote = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/Note.json", NoteKatalogEintrag.class);
		Note.init(new CoreTypeDataManager<>(dataNote.getVersion(), Note.class, Note.values(), dataNote.getData(), dataNote.getHistorienIDs()));
		// Core-Type Sprachreferenzniveau
		final var dataSprachreferenzniveau = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/fach/Sprachreferenzniveau.json", SprachreferenzniveauKatalogEintrag.class);
		Sprachreferenzniveau.init(new CoreTypeDataManager<>(dataSprachreferenzniveau.getVersion(), Sprachreferenzniveau.class, Sprachreferenzniveau.values(), dataSprachreferenzniveau.getData(), dataSprachreferenzniveau.getHistorienIDs()));
		// Core-Type BerufskollegBildungsgangTyp
		final var dataBerufskollegBildungsgangTyp = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/schule/BerufskollegBildungsgangTyp.json", BildungsgangTypKatalogEintrag.class);
		BerufskollegBildungsgangTyp.init(new CoreTypeDataManager<>(dataBerufskollegBildungsgangTyp.getVersion(), BerufskollegBildungsgangTyp.class, BerufskollegBildungsgangTyp.values(), dataBerufskollegBildungsgangTyp.getData(), dataBerufskollegBildungsgangTyp.getHistorienIDs()));
		// Core-Type WeiterbildungskollegBildungsgangTyp
		final var dataWeiterbildungskollegBildungsgangTyp = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/schule/WeiterbildungskollegBildungsgangTyp.json", BildungsgangTypKatalogEintrag.class);
		WeiterbildungskollegBildungsgangTyp.init(new CoreTypeDataManager<>(dataWeiterbildungskollegBildungsgangTyp.getVersion(), WeiterbildungskollegBildungsgangTyp.class, WeiterbildungskollegBildungsgangTyp.values(), dataWeiterbildungskollegBildungsgangTyp.getData(), dataWeiterbildungskollegBildungsgangTyp.getHistorienIDs()));
		// CoreType Schulgliederung
		final var dataSchulgliederung = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/schule/Schulgliederung.json", SchulgliederungKatalogEintrag.class);
		Schulgliederung.init(new CoreTypeDataManager<>(dataSchulgliederung.getVersion(), Schulgliederung.class, Schulgliederung.values(), dataSchulgliederung.getData(), dataSchulgliederung.getHistorienIDs()));
		// CoreType Fachgruppe
		final var dataFachgruppe = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/fach/Fachgruppe.json", FachgruppeKatalogEintrag.class);
		Fachgruppe.init(new CoreTypeDataManager<>(dataFachgruppe.getVersion(), Fachgruppe.class, Fachgruppe.values(), dataFachgruppe.getData(), dataFachgruppe.getHistorienIDs()));
		// CoreType Fach
		final var dataFach = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/fach/Fach.json", FachKatalogEintrag.class);
		Fach.init(new CoreTypeDataManager<>(dataFach.getVersion(), Fach.class, Fach.values(), dataFach.getData(), dataFach.getHistorienIDs()));

		// Core-Type LehrerAbgangsgrund
		final var dataLehrerAbgangsgrund = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/lehrer/LehrerAbgangsgrund.json", LehrerAbgangsgrundKatalogEintrag.class);
		LehrerAbgangsgrund.init(new CoreTypeDataManager<>(dataLehrerAbgangsgrund.getVersion(), LehrerAbgangsgrund.class, LehrerAbgangsgrund.values(), dataLehrerAbgangsgrund.getData(), dataLehrerAbgangsgrund.getHistorienIDs()));
		// Core-Type LehrerBeschaeftigungsart
		final var dataLehrerBeschaeftigungsart = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/lehrer/LehrerBeschaeftigungsart.json", LehrerBeschaeftigungsartKatalogEintrag.class);
		LehrerBeschaeftigungsart.init(new CoreTypeDataManager<>(dataLehrerBeschaeftigungsart.getVersion(), LehrerBeschaeftigungsart.class, LehrerBeschaeftigungsart.values(), dataLehrerBeschaeftigungsart.getData(), dataLehrerBeschaeftigungsart.getHistorienIDs()));
		// Core-Type LehrerEinsatzstatus
		final var dataLehrerEinsatzstatus = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/lehrer/LehrerEinsatzstatus.json", LehrerEinsatzstatusKatalogEintrag.class);
		LehrerEinsatzstatus.init(new CoreTypeDataManager<>(dataLehrerEinsatzstatus.getVersion(), LehrerEinsatzstatus.class, LehrerEinsatzstatus.values(), dataLehrerEinsatzstatus.getData(), dataLehrerEinsatzstatus.getHistorienIDs()));
		// Core-Type LehrerFachrichtung
		final var dataLehrerFachrichtung = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/lehrer/LehrerFachrichtung.json", LehrerFachrichtungKatalogEintrag.class);
		LehrerFachrichtung.init(new CoreTypeDataManager<>(dataLehrerFachrichtung.getVersion(), LehrerFachrichtung.class, LehrerFachrichtung.values(), dataLehrerFachrichtung.getData(), dataLehrerFachrichtung.getHistorienIDs()));
		// Core-Type LehrerLehrbefaehigung
		final var dataLehrerLehrbefaehigung = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/lehrer/LehrerLehrbefaehigung.json", LehrerLehrbefaehigungKatalogEintrag.class);
		LehrerLehrbefaehigung.init(new CoreTypeDataManager<>(dataLehrerLehrbefaehigung.getVersion(), LehrerLehrbefaehigung.class, LehrerLehrbefaehigung.values(), dataLehrerLehrbefaehigung.getData(), dataLehrerLehrbefaehigung.getHistorienIDs()));
		// Core-Type LehrerFachrichtungAnerkennung
		final var dataLehrerFachrichtungAnerkennung = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/lehrer/LehrerFachrichtungAnerkennung.json", LehrerFachrichtungAnerkennungKatalogEintrag.class);
		LehrerFachrichtungAnerkennung.init(new CoreTypeDataManager<>(dataLehrerFachrichtungAnerkennung.getVersion(), LehrerFachrichtungAnerkennung.class, LehrerFachrichtungAnerkennung.values(), dataLehrerFachrichtungAnerkennung.getData(), dataLehrerFachrichtungAnerkennung.getHistorienIDs()));
		// Core-Type LehrerLehramtAnerkennung
		final var dataLehrerLehramt = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/lehrer/LehrerLehramt.json", LehrerLehramtKatalogEintrag.class);
		LehrerLehramt.init(new CoreTypeDataManager<>(dataLehrerLehramt.getVersion(), LehrerLehramt.class, LehrerLehramt.values(), dataLehrerLehramt.getData(), dataLehrerLehramt.getHistorienIDs()));
		// Core-Type LehrerLehramtAnerkennung
		final var dataLehrerLehramtAnerkennung = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/lehrer/LehrerLehramtAnerkennung.json", LehrerLehramtAnerkennungKatalogEintrag.class);
		LehrerLehramtAnerkennung.init(new CoreTypeDataManager<>(dataLehrerLehramtAnerkennung.getVersion(), LehrerLehramtAnerkennung.class, LehrerLehramtAnerkennung.values(), dataLehrerLehramtAnerkennung.getData(), dataLehrerLehramtAnerkennung.getHistorienIDs()));
        // Core-Type LehrerLehrbefaehigungAnerkennung
		final var dataLehrerLehrbefaehigungAnerkennung = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/lehrer/LehrerLehrbefaehigungAnerkennung.json", LehrerLehrbefaehigungAnerkennungKatalogEintrag.class);
		LehrerLehrbefaehigungAnerkennung.init(new CoreTypeDataManager<>(dataLehrerLehrbefaehigungAnerkennung.getVersion(), LehrerLehrbefaehigungAnerkennung.class, LehrerLehrbefaehigungAnerkennung.values(), dataLehrerLehrbefaehigungAnerkennung.getData(), dataLehrerLehrbefaehigungAnerkennung.getHistorienIDs()));
        // Core-Type LehrerLeitungsfunktion
		final var dataLehrerLeitungsfunktion = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/lehrer/LehrerLeitungsfunktion.json", LehrerLeitungsfunktionKatalogEintrag.class);
		LehrerLeitungsfunktion.init(new CoreTypeDataManager<>(dataLehrerLeitungsfunktion.getVersion(), LehrerLeitungsfunktion.class, LehrerLeitungsfunktion.values(), dataLehrerLeitungsfunktion.getData(), dataLehrerLeitungsfunktion.getHistorienIDs()));
        // Core-Type LehrerRechtsverhaeltnis
		final var dataLehrerRechtsverhaeltnis = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/lehrer/LehrerRechtsverhaeltnis.json", LehrerRechtsverhaeltnisKatalogEintrag.class);
		LehrerRechtsverhaeltnis.init(new CoreTypeDataManager<>(dataLehrerRechtsverhaeltnis.getVersion(), LehrerRechtsverhaeltnis.class, LehrerRechtsverhaeltnis.values(), dataLehrerRechtsverhaeltnis.getData(), dataLehrerRechtsverhaeltnis.getHistorienIDs()));
		// Core-Type LehrerZugangsgrund
		final var dataLehrerZugangsgrund = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/lehrer/LehrerZugangsgrund.json", LehrerZugangsgrundKatalogEintrag.class);
		LehrerZugangsgrund.init(new CoreTypeDataManager<>(dataLehrerZugangsgrund.getVersion(), LehrerZugangsgrund.class, LehrerZugangsgrund.values(), dataLehrerZugangsgrund.getData(), dataLehrerZugangsgrund.getHistorienIDs()));
		// Core-Type BilingualeSprache
		final var dataBilingualeSprache = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/fach/BilingualeSprache.json", BilingualeSpracheKatalogEintrag.class);
		BilingualeSprache.init(new CoreTypeDataManager<>(dataBilingualeSprache.getVersion(), BilingualeSprache.class, BilingualeSprache.values(), dataBilingualeSprache.getData(), dataBilingualeSprache.getHistorienIDs()));

		// Core-Type KAOABerufsfeld
		final var dataKAOABerufsfeld = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/kaoa/KAOABerufsfeld.json", KAOABerufsfeldKatalogEintrag.class);
		KAOABerufsfeld.init(new CoreTypeDataManager<>(dataKAOABerufsfeld.getVersion(), KAOABerufsfeld.class, KAOABerufsfeld.values(), dataKAOABerufsfeld.getData(), dataKAOABerufsfeld.getHistorienIDs()));
		// Core-Type KAOAMerkmaleOptionsart
		final var dataKAOAMerkmaleOptionsart = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/kaoa/KAOAMerkmaleOptionsarten.json", KAOAMerkmaleOptionsartenKatalogEintrag.class);
		KAOAMerkmaleOptionsarten.init(new CoreTypeDataManager<>(dataKAOAMerkmaleOptionsart.getVersion(), KAOAMerkmaleOptionsarten.class, KAOAMerkmaleOptionsarten.values(), dataKAOAMerkmaleOptionsart.getData(), dataKAOAMerkmaleOptionsart.getHistorienIDs()));
		// Core-Type KAOAZusatzmerkmaleOptionsart
		final var dataKAOAZusatzmerkmaleOptionsart = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/kaoa/KAOAZusatzmerkmaleOptionsarten.json", KAOAZusatzmerkmaleOptionsartenKatalogEintrag.class);
		KAOAZusatzmerkmaleOptionsarten.init(new CoreTypeDataManager<>(dataKAOAZusatzmerkmaleOptionsart.getVersion(), KAOAZusatzmerkmaleOptionsarten.class, KAOAZusatzmerkmaleOptionsarten.values(), dataKAOAZusatzmerkmaleOptionsart.getData(), dataKAOAZusatzmerkmaleOptionsart.getHistorienIDs()));
		// Core-Type KAOAEbene4
		final var dataKAOAEbene4 = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/kaoa/KAOAEbene4.json", KAOAEbene4KatalogEintrag.class);
		KAOAEbene4.init(new CoreTypeDataManager<>(dataKAOAEbene4.getVersion(), KAOAEbene4.class, KAOAEbene4.values(), dataKAOAEbene4.getData(), dataKAOAEbene4.getHistorienIDs()));
		// Core-Type KAOAZusatzmerkmal
		final var dataKAOAZusatzmerkmal = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/kaoa/KAOAZusatzmerkmal.json", KAOAZusatzmerkmalKatalogEintrag.class);
		KAOAZusatzmerkmal.init(new CoreTypeDataManager<>(dataKAOAZusatzmerkmal.getVersion(), KAOAZusatzmerkmal.class, KAOAZusatzmerkmal.values(), dataKAOAZusatzmerkmal.getData(), dataKAOAZusatzmerkmal.getHistorienIDs()));
        // Core-Type KAOAAnschlussoption
		final var dataKAOAAnschlussoption = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/kaoa/KAOAAnschlussoptionen.json", KAOAAnschlussoptionenKatalogEintrag.class);
		KAOAAnschlussoptionen.init(new CoreTypeDataManager<>(dataKAOAAnschlussoption.getVersion(), KAOAAnschlussoptionen.class, KAOAAnschlussoptionen.values(), dataKAOAAnschlussoption.getData(), dataKAOAAnschlussoption.getHistorienIDs()));
		// Core-Type KAOAKategorie
		final var dataKAOAKategorie = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/kaoa/KAOAKategorie.json", KAOAKategorieKatalogEintrag.class);
		KAOAKategorie.init(new CoreTypeDataManager<>(dataKAOAKategorie.getVersion(), KAOAKategorie.class, KAOAKategorie.values(), dataKAOAKategorie.getData(), dataKAOAKategorie.getHistorienIDs()));
		// Core-Type KAOAMerkmal
		final var dataKAOAMerkmal = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/kaoa/KAOAMerkmal.json", KAOAMerkmalKatalogEintrag.class);
		KAOAMerkmal.init(new CoreTypeDataManager<>(dataKAOAMerkmal.getVersion(), KAOAMerkmal.class, KAOAMerkmal.values(), dataKAOAMerkmal.getData(), dataKAOAMerkmal.getHistorienIDs()));

		// Core-Type Klassenart
		final var dataKlassenart = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/klassen/Klassenart.json", KlassenartKatalogEintrag.class);
		Klassenart.init(new CoreTypeDataManager<>(dataKlassenart.getVersion(), Klassenart.class, Klassenart.values(), dataKlassenart.getData(), dataKlassenart.getHistorienIDs()));
		// Core-Type Uebergangsempfehlung
		final var dataUebergangsempfehlung = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/schueler/Uebergangsempfehlung.json", UebergangsempfehlungKatalogEintrag.class);
		Uebergangsempfehlung.init(new CoreTypeDataManager<>(dataUebergangsempfehlung.getVersion(), Uebergangsempfehlung.class, Uebergangsempfehlung.values(), dataUebergangsempfehlung.getData(), dataUebergangsempfehlung.getHistorienIDs()));
		// Core-Type ZulaessigeKursart
		final var dataZulaessigeKursart = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/kurse/ZulaessigeKursart.json", ZulaessigeKursartKatalogEintrag.class);
		ZulaessigeKursart.init(new CoreTypeDataManager<>(dataZulaessigeKursart.getVersion(), ZulaessigeKursart.class, ZulaessigeKursart.values(), dataZulaessigeKursart.getData(), dataZulaessigeKursart.getHistorienIDs()));
		// Core-Type Foerderschwerpunkt
		final var dataFoerderschwerpunkt = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/schule/Foerderschwerpunkt.json", FoerderschwerpunktKatalogEintrag.class);
		Foerderschwerpunkt.init(new CoreTypeDataManager<>(dataFoerderschwerpunkt.getVersion(), Foerderschwerpunkt.class, Foerderschwerpunkt.values(), dataFoerderschwerpunkt.getData(), dataFoerderschwerpunkt.getHistorienIDs()));

		// SimpleCoreTypes
		// Core-Type LehrerAnrechnungsgrund
		final var dataLehrerAnrechnungsgrund = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/lehrer/LehrerAnrechnungsgrund.json", LehrerAnrechnungsgrundKatalogEintrag.class);
		CoreTypeSimple.initValues(new LehrerAnrechnungsgrund(), LehrerAnrechnungsgrund.class, dataLehrerAnrechnungsgrund.getData());
		LehrerAnrechnungsgrund.init(new CoreTypeDataManager<>(dataLehrerAnrechnungsgrund.getVersion(), LehrerAnrechnungsgrund.class, LehrerAnrechnungsgrund.values(), dataLehrerAnrechnungsgrund.getData(), dataLehrerAnrechnungsgrund.getHistorienIDs()));
		// Core-Type LehrerMehrleistungsart
		final var dataLehrerMehrleistungsart = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/lehrer/LehrerMehrleistungsarten.json", LehrerMehrleistungsartKatalogEintrag.class);
		CoreTypeSimple.initValues(new LehrerMehrleistungsarten(), LehrerMehrleistungsarten.class, dataLehrerMehrleistungsart.getData());
		LehrerMehrleistungsarten.init(new CoreTypeDataManager<>(dataLehrerMehrleistungsart.getVersion(), LehrerMehrleistungsarten.class, LehrerMehrleistungsarten.values(), dataLehrerMehrleistungsart.getData(), dataLehrerMehrleistungsart.getHistorienIDs()));
		// Core-Type LehrerMinderleistungsart
		final var dataLehrerMinderleistungsart = JsonReader.fromResourceGetCoreTypeData("de/svws_nrw/asd/types/lehrer/LehrerMinderleistungsarten.json", LehrerMinderleistungsartKatalogEintrag.class);
		CoreTypeSimple.initValues(new LehrerMinderleistungsarten(), LehrerMinderleistungsarten.class, dataLehrerMinderleistungsart.getData());
		LehrerMinderleistungsarten.init(new CoreTypeDataManager<>(dataLehrerMinderleistungsart.getVersion(), LehrerMinderleistungsarten.class, LehrerMinderleistungsarten.values(), dataLehrerMinderleistungsart.getData(), dataLehrerMinderleistungsart.getHistorienIDs()));

		// TODO ...

		// ValidatorFehlerarten
		final var dataValidatorenFehlerartKontext = JsonReader.fromResourceGetValidatorData("de/svws_nrw/asd/validate/ValidatorenFehlerartKontext.json");
		ValidatorManager.init(dataValidatorenFehlerartKontext.getVersion(), dataValidatorenFehlerartKontext.getData());
	}

}
