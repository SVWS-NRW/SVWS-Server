package de.svws_nrw.asd.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.asd.data.CoreTypeException;
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
import de.svws_nrw.asd.data.schule.NationalitaetenKatalogEintrag;
import de.svws_nrw.asd.data.schule.OrganisationsformKatalogEintrag;
import de.svws_nrw.asd.data.schule.ReligionKatalogEintrag;
import de.svws_nrw.asd.data.schule.SchulabschlussAllgemeinbildendKatalogEintrag;
import de.svws_nrw.asd.data.schule.SchulabschlussBerufsbildendKatalogEintrag;
import de.svws_nrw.asd.data.schule.SchulformKatalogEintrag;
import de.svws_nrw.asd.data.schule.SchulgliederungKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
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
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.types.schule.Religion;
import de.svws_nrw.asd.types.schule.SchulabschlussAllgemeinbildend;
import de.svws_nrw.asd.types.schule.SchulabschlussBerufsbildend;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.types.schule.WeiterbildungskollegBildungsgangTyp;
import de.svws_nrw.asd.types.schule.WeiterbildungskollegOrganisationsformen;
import de.svws_nrw.asd.utils.json.JsonCoreTypeData;
import de.svws_nrw.asd.utils.json.JsonReader;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse enthält Informationen, um den Core-Types den Ort der Ressource zuzuweisen
 * und den Bezug zu dem Datentyp der Katalog-Einträge herzustellen.
 *
 * @param <T> der Typ des Katalog-Einträge
 * @param <U> der Typ des Core-Types
 */
public final class CoreTypeRessource<T extends CoreTypeData, U extends CoreType<T, U>> {

	/** eine Liste mit allen definierten Core-Type-Ressourcen */
	private static final List<CoreTypeRessource<?, ?>> listResources = new ArrayList<>();

	/** eine Map mit allen definierten Core-Type-Ressourcen zu geordnet zu der Core-Type-Klasse */
	private static final Map<Class<?>, CoreTypeRessource<?, ?>> mapResources = new HashMap<>();


	/** Die Klasse des Core-Types */
	public final Class<U> typeClass;

	/** Die Klasse der Katalog-Einträge des Core-Types */
	public final Class<T> dataClass;

	/** Der Pfad zu der Ressources des Core-Types */
	public final String path;

	/** Die Daten des Core-Types, welche aus der Ressource geladen wurden */
	private JsonCoreTypeData<T> data;

	/** Die möglichen Werte des Core-Types - bei Simple-Core-Types werden diese Lazy initialisiert. */
	private U[] values = null;

	/** Der Manager für die Core-Type-Daten */
	private CoreTypeDataManager<T, U> dataManager;



	/**
	 * Initialisiert die Ressourcen-Informationen
	 *
	 * @param typeClass   die Klasse des Core-Types
	 * @param dataClass   die Klasse der Katalog-Einträge des Core-Types
	 * @param path        der Pfad zu der Ressource
	 * @param values      die möglichen Werte des Core-Types - null bei Simple-Core-Types für eine späte Initialisierung
	 */
	private CoreTypeRessource(final Class<U> typeClass, final Class<T> dataClass, final String path, final U[] values) {
		this.typeClass = typeClass;
		this.dataClass = dataClass;
		this.path = path;
		this.values = values;
	}


	/**
	 * Gibt die Daten des Core-Types zurück.
	 *
	 * @return die Daten
	 */
	public JsonCoreTypeData<T> getData() {
		return data;
	}


	/**
	 * Gibt den Daten-Manager des Core-Types zurück.
	 *
	 * @return der Daten-Manager
	 */
	public CoreTypeDataManager<T, U> getDataManager() {
		return dataManager;
	}


	/**
	 * Ermittelt die Informationen Daten zu der Core-Type-Klasse und lädt diese von der Ressource, sofern dies nicht schon
	 * geschehen ist.
	 *
	 * @param <T> der Typ der Core-Typ-Katalog-Einträge
	 * @param <U> der Typ des Core-Types
	 * @param typeClass   die Klasse des Core-Types
	 *
	 * @return die Informationen zu der Ressource
	 */
	public static <T extends CoreTypeData, U extends CoreType<T, U>> @NotNull JsonCoreTypeData<T> getData(final Class<U> typeClass) {
		final var res = get(typeClass);
		if (res == null)
			throw new RuntimeException("Der Core Type %s wurde noch nicht initialisiert.".formatted(typeClass.getName()));
		return res.getData();
	}


	/**
	 * Ermittelt die Informationen zu der Ressource anhand der Core-Type-Klasse und lädt die Daten von der Ressource
	 *
	 * @param <T> der Typ der Core-Typ-Katalog-Einträge
	 * @param <U> der Typ des Core-Types
	 * @param typeClass   die Klasse des Core-Types
	 *
	 * @return die Informationen zu der Ressource
	 */
	@SuppressWarnings("unchecked")
	public static <T extends CoreTypeData, U extends CoreType<T, U>> CoreTypeRessource<T, U> get(final Class<U> typeClass) {
		if (listResources.isEmpty())
			initAll();
		return (CoreTypeRessource<T, U>) mapResources.get(typeClass);
	}


	/**
	 * Initialisiert den Core-Type. Die Daten müssen zuvor geladen sein (siehe initAll)
	 */
	private void init() {
		dataManager = new CoreTypeDataManager<>(data.getVersion(), typeClass, values, data.getData(), data.getStatistikIDs());
		try {
			final Method method = typeClass.getMethod("init", CoreTypeDataManager.class);
			method.invoke(null, dataManager);
		} catch (final NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Fehler beim Aufruf der Init-Methode des CoreTypes %s".formatted(typeClass.getName()), e);
		}
	}


	/**
	 * Initialisiert alle Core-Type mit den Daten aus den angegeben ressourcen
	 */
	public static void initAll() {
		// Lade die Daten
		addAll();
		// Initialisieren die Core-Types
		for (final var res : listResources)
			res.init();
	}


	/**
	 * Reinitialisieren der Values eines CoreTypeSimple.
	 *
	 * @param <S>
	 * @param jsonCoreTypeData
	 */
	@SuppressWarnings("unchecked")
	private <S extends CoreTypeSimple<T, S>> void reinitSimple(final JsonCoreTypeData<T> jsonCoreTypeData) {
		final Class<S> typeClassSimple = (Class<S>) typeClass;
		try {
			CoreTypeSimple.initValues(typeClassSimple.getConstructor().newInstance(), typeClassSimple, jsonCoreTypeData.getData());
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			throw new RuntimeException("Fehler beim Initialisieren des CoreTypeSimple %s".formatted(typeClass.getName()), e);
		}
		values = (U[]) CoreTypeSimple.valuesByClass(typeClassSimple);
	}


	/**
	 * Reinitialisieren eines CoreTypes und der zugehörigen Ressource mit neuen Daten.
	 *
	 * @param jsonCoreTypeData
	 */
	public void reinit(final JsonCoreTypeData<T> jsonCoreTypeData) {
		data = jsonCoreTypeData;
		// Ermitteln, ob vom Typ CoreTypeSimple
		if (!typeClass.isEnum()) {
			reinitSimple(jsonCoreTypeData);
		}
		init();
	}


	/**
	 * Führt eine Reinitialisierung aller Kataloge mit den übergebenen Daten aus.
	 *
	 * @param <V>
	 * @param <U>
	 * @param map
	 */
	@SuppressWarnings("unchecked")
	public static <V extends CoreTypeData, U extends CoreType<V, U>> void reinitAll(@NotNull final Map<Class<U>, JsonCoreTypeData<V>> map) {
		if ((map == null) || map.isEmpty())
			throw new CoreTypeException("Liste darf nicht leer sein.");
		// Prüfen auf unbekannte Kataloge
		for (final Class<U> key : map.keySet())
			if (!mapResources.containsKey(key))
				throw new CoreTypeException("Liste enthält Katalog der nicht in den Ressourcen vorhanden ist.");
		// Reinit für alle Kataloge ausführen
		for (final Map.Entry<Class<U>, JsonCoreTypeData<V>> entry : map.entrySet())
			((CoreTypeRessource<V, ?>) mapResources.get(entry.getKey())).reinit(entry.getValue());
	}


	/**
	 * Fügt eine neue Ressource hinzu und lädt die Daten.
	 *
	 * @param <T>         der Typ der Core-Type-Daten
	 * @param <U>         der Typ des Core-Types
	 * @param typeClass   die Klasse des Core-Types
	 * @param dataClass   die Klasse der Katalog-Einträge des Core-Types
	 * @param values      die möglichen Werte des Core-Types - null bei Simple-Core-Types für eine späte Initialisierung
	 * @param path        der Pfad zu der Ressource
	 *
	 * @return die neue Ressource
	 */
	private static <T extends CoreTypeData, U extends CoreType<T, U>> CoreTypeRessource<T, U> add(final Class<U> typeClass, final Class<T> dataClass,
			final U[] values, final String path) {
		final CoreTypeRessource<T, U> res = new CoreTypeRessource<>(typeClass, dataClass, path, values);
		res.data = JsonReader.fromResourceGetCoreTypeData(path, dataClass);
		listResources.add(res);
		mapResources.put(typeClass, res);
		return res;
	}

	/**
	 * Fügt eine neue Ressource für einen CoreTypeSimple hinzu und lädt die Daten.
	 *
	 * @param <T>         der Typ der Core-Type-Daten
	 * @param <U>         der Typ des Core-Types
	 * @param typeClass   die Klasse des Core-Types
	 * @param dataClass   die Klasse der Katalog-Einträge des Core-Types
	 * @param path        der Pfad zu der Ressource
	 *
	 * @return die neue Ressource
	 */
	private static <T extends CoreTypeData, U extends CoreTypeSimple<T, U>> CoreTypeRessource<T, U> addSimple(final Class<U> typeClass,
			final Class<T> dataClass, final String path) {
		final CoreTypeRessource<T, U> res = add(typeClass, dataClass, null, path);
		// Lazy Loading bei SimpleCoreType
		try {
			CoreTypeSimple.initValues(typeClass.getConstructor().newInstance(), typeClass, res.data.getData());
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			throw new RuntimeException("Fehler beim Initialisieren des CoreTypeSimple %s".formatted(typeClass.getName()), e);
		}
		res.values = CoreTypeSimple.valuesByClass(typeClass);
		return res;
	}


	/**
	 * Fügt die Informationen zu allen Ressourcen hinzu.
	 */
	private static void addAll() {
		add(Schulform.class, SchulformKatalogEintrag.class, Schulform.values(),
				"de/svws_nrw/asd/types/schule/Schulform.json");
		add(BerufskollegAnlage.class, BerufskollegAnlageKatalogEintrag.class, BerufskollegAnlage.values(),
				"de/svws_nrw/asd/types/schule/BerufskollegAnlage.json");
		add(AllgemeinbildendOrganisationsformen.class, OrganisationsformKatalogEintrag.class, AllgemeinbildendOrganisationsformen.values(),
				"de/svws_nrw/asd/types/schule/AllgemeinbildendOrganisationsformen.json");
		add(BerufskollegOrganisationsformen.class, OrganisationsformKatalogEintrag.class, BerufskollegOrganisationsformen.values(),
				"de/svws_nrw/asd/types/schule/BerufskollegOrganisationsformen.json");
		add(WeiterbildungskollegOrganisationsformen.class, OrganisationsformKatalogEintrag.class, WeiterbildungskollegOrganisationsformen.values(),
				"de/svws_nrw/asd/types/schule/WeiterbildungskollegOrganisationsformen.json");
		add(SchulabschlussAllgemeinbildend.class, SchulabschlussAllgemeinbildendKatalogEintrag.class, SchulabschlussAllgemeinbildend.values(),
				"de/svws_nrw/asd/types/schule/SchulabschlussAllgemeinbildend.json");
		add(SchulabschlussBerufsbildend.class, SchulabschlussBerufsbildendKatalogEintrag.class, SchulabschlussBerufsbildend.values(),
				"de/svws_nrw/asd/types/schule/SchulabschlussBerufsbildend.json");
		add(HerkunftBildungsgang.class, HerkunftBildungsgangKatalogEintrag.class, HerkunftBildungsgang.values(),
				"de/svws_nrw/asd/types/schueler/HerkunftBildungsgang.json");
		add(HerkunftBildungsgangTyp.class, HerkunftBildungsgangTypKatalogEintrag.class, HerkunftBildungsgangTyp.values(),
				"de/svws_nrw/asd/types/schueler/HerkunftBildungsgangTyp.json");
		add(Jahrgaenge.class, JahrgaengeKatalogEintrag.class, Jahrgaenge.values(),
				"de/svws_nrw/asd/types/jahrgang/Jahrgaenge.json");
		add(PrimarstufeSchuleingangsphaseBesuchsjahre.class, PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag.class,
				PrimarstufeSchuleingangsphaseBesuchsjahre.values(),
				"de/svws_nrw/asd/types/jahrgang/PrimarstufeSchuleingangsphaseBesuchsjahre.json");
		add(Religion.class, ReligionKatalogEintrag.class, Religion.values(),
				"de/svws_nrw/asd/types/schule/Religion.json");
		add(Kindergartenbesuch.class, KindergartenbesuchKatalogEintrag.class, Kindergartenbesuch.values(),
				"de/svws_nrw/asd/types/schule/Kindergartenbesuch.json");
		add(SchuelerStatus.class, SchuelerStatusKatalogEintrag.class, SchuelerStatus.values(),
				"de/svws_nrw/asd/types/schueler/SchuelerStatus.json");
		add(Note.class, NoteKatalogEintrag.class, Note.values(),
				"de/svws_nrw/asd/types/Note.json");
		add(Sprachreferenzniveau.class, SprachreferenzniveauKatalogEintrag.class, Sprachreferenzniveau.values(),
				"de/svws_nrw/asd/types/fach/Sprachreferenzniveau.json");
		add(BerufskollegBildungsgangTyp.class, BildungsgangTypKatalogEintrag.class, BerufskollegBildungsgangTyp.values(),
				"de/svws_nrw/asd/types/schule/BerufskollegBildungsgangTyp.json");
		add(WeiterbildungskollegBildungsgangTyp.class, BildungsgangTypKatalogEintrag.class, WeiterbildungskollegBildungsgangTyp.values(),
				"de/svws_nrw/asd/types/schule/WeiterbildungskollegBildungsgangTyp.json");
		add(Schulgliederung.class, SchulgliederungKatalogEintrag.class, Schulgliederung.values(),
				"de/svws_nrw/asd/types/schule/Schulgliederung.json");
		add(Fachgruppe.class, FachgruppeKatalogEintrag.class, Fachgruppe.values(),
				"de/svws_nrw/asd/types/fach/Fachgruppe.json");
		add(Fach.class, FachKatalogEintrag.class, Fach.values(),
				"de/svws_nrw/asd/types/fach/Fach.json");
		add(LehrerAbgangsgrund.class, LehrerAbgangsgrundKatalogEintrag.class, LehrerAbgangsgrund.values(),
				"de/svws_nrw/asd/types/lehrer/LehrerAbgangsgrund.json");
		add(LehrerBeschaeftigungsart.class, LehrerBeschaeftigungsartKatalogEintrag.class, LehrerBeschaeftigungsart.values(),
				"de/svws_nrw/asd/types/lehrer/LehrerBeschaeftigungsart.json");
		add(LehrerEinsatzstatus.class, LehrerEinsatzstatusKatalogEintrag.class, LehrerEinsatzstatus.values(),
				"de/svws_nrw/asd/types/lehrer/LehrerEinsatzstatus.json");
		add(LehrerFachrichtung.class, LehrerFachrichtungKatalogEintrag.class, LehrerFachrichtung.values(),
				"de/svws_nrw/asd/types/lehrer/LehrerFachrichtung.json");
		add(LehrerLehrbefaehigung.class, LehrerLehrbefaehigungKatalogEintrag.class, LehrerLehrbefaehigung.values(),
				"de/svws_nrw/asd/types/lehrer/LehrerLehrbefaehigung.json");
		add(LehrerFachrichtungAnerkennung.class, LehrerFachrichtungAnerkennungKatalogEintrag.class, LehrerFachrichtungAnerkennung.values(),
				"de/svws_nrw/asd/types/lehrer/LehrerFachrichtungAnerkennung.json");
		add(LehrerLehramt.class, LehrerLehramtKatalogEintrag.class, LehrerLehramt.values(),
				"de/svws_nrw/asd/types/lehrer/LehrerLehramt.json");
		add(LehrerLehramtAnerkennung.class, LehrerLehramtAnerkennungKatalogEintrag.class, LehrerLehramtAnerkennung.values(),
				"de/svws_nrw/asd/types/lehrer/LehrerLehramtAnerkennung.json");
		add(LehrerLehrbefaehigungAnerkennung.class, LehrerLehrbefaehigungAnerkennungKatalogEintrag.class, LehrerLehrbefaehigungAnerkennung.values(),
				"de/svws_nrw/asd/types/lehrer/LehrerLehrbefaehigungAnerkennung.json");
		add(LehrerLeitungsfunktion.class, LehrerLeitungsfunktionKatalogEintrag.class, LehrerLeitungsfunktion.values(),
				"de/svws_nrw/asd/types/lehrer/LehrerLeitungsfunktion.json");
		add(LehrerRechtsverhaeltnis.class, LehrerRechtsverhaeltnisKatalogEintrag.class, LehrerRechtsverhaeltnis.values(),
				"de/svws_nrw/asd/types/lehrer/LehrerRechtsverhaeltnis.json");
		add(LehrerZugangsgrund.class, LehrerZugangsgrundKatalogEintrag.class, LehrerZugangsgrund.values(),
				"de/svws_nrw/asd/types/lehrer/LehrerZugangsgrund.json");
		add(BilingualeSprache.class, BilingualeSpracheKatalogEintrag.class, BilingualeSprache.values(),
				"de/svws_nrw/asd/types/fach/BilingualeSprache.json");
		add(KAOABerufsfeld.class, KAOABerufsfeldKatalogEintrag.class, KAOABerufsfeld.values(),
				"de/svws_nrw/asd/types/kaoa/KAOABerufsfeld.json");
		add(KAOAMerkmaleOptionsarten.class, KAOAMerkmaleOptionsartenKatalogEintrag.class, KAOAMerkmaleOptionsarten.values(),
				"de/svws_nrw/asd/types/kaoa/KAOAMerkmaleOptionsarten.json");
		add(KAOAZusatzmerkmaleOptionsarten.class, KAOAZusatzmerkmaleOptionsartenKatalogEintrag.class, KAOAZusatzmerkmaleOptionsarten.values(),
				"de/svws_nrw/asd/types/kaoa/KAOAZusatzmerkmaleOptionsarten.json");
		add(KAOAEbene4.class, KAOAEbene4KatalogEintrag.class, KAOAEbene4.values(),
				"de/svws_nrw/asd/types/kaoa/KAOAEbene4.json");
		add(KAOAZusatzmerkmal.class, KAOAZusatzmerkmalKatalogEintrag.class, KAOAZusatzmerkmal.values(),
				"de/svws_nrw/asd/types/kaoa/KAOAZusatzmerkmal.json");
		add(KAOAAnschlussoptionen.class, KAOAAnschlussoptionenKatalogEintrag.class, KAOAAnschlussoptionen.values(),
				"de/svws_nrw/asd/types/kaoa/KAOAAnschlussoptionen.json");
		add(KAOAKategorie.class, KAOAKategorieKatalogEintrag.class, KAOAKategorie.values(),
				"de/svws_nrw/asd/types/kaoa/KAOAKategorie.json");
		add(KAOAMerkmal.class, KAOAMerkmalKatalogEintrag.class, KAOAMerkmal.values(),
				"de/svws_nrw/asd/types/kaoa/KAOAMerkmal.json");
		add(Klassenart.class, KlassenartKatalogEintrag.class, Klassenart.values(),
				"de/svws_nrw/asd/types/klassen/Klassenart.json");
		add(Uebergangsempfehlung.class, UebergangsempfehlungKatalogEintrag.class, Uebergangsempfehlung.values(),
				"de/svws_nrw/asd/types/schueler/Uebergangsempfehlung.json");
		add(ZulaessigeKursart.class, ZulaessigeKursartKatalogEintrag.class, ZulaessigeKursart.values(),
				"de/svws_nrw/asd/types/kurse/ZulaessigeKursart.json");
		add(Foerderschwerpunkt.class, FoerderschwerpunktKatalogEintrag.class, Foerderschwerpunkt.values(),
				"de/svws_nrw/asd/types/schule/Foerderschwerpunkt.json");
		addSimple(LehrerAnrechnungsgrund.class, LehrerAnrechnungsgrundKatalogEintrag.class,
				"de/svws_nrw/asd/types/lehrer/LehrerAnrechnungsgrund.json");
		addSimple(LehrerMehrleistungsarten.class, LehrerMehrleistungsartKatalogEintrag.class,
				"de/svws_nrw/asd/types/lehrer/LehrerMehrleistungsarten.json");
		addSimple(LehrerMinderleistungsarten.class, LehrerMinderleistungsartKatalogEintrag.class,
				"de/svws_nrw/asd/types/lehrer/LehrerMinderleistungsarten.json");
		addSimple(Nationalitaeten.class, NationalitaetenKatalogEintrag.class,
				"de/svws_nrw/asd/types/schule/Nationalitaeten.json");
	}

}
