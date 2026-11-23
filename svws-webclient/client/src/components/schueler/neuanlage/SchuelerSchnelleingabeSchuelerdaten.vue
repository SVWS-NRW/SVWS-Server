<template>
	<svws-ui-content-card title="Persönliche Daten" class="col-span-full">
		<svws-ui-input-wrapper :grid="4">
			<svws-ui-text-input placeholder="Nachname"
				v-model="stammdatenModel.proxy.nachname"
				@change="stammdatenModel.patch"
				:validation="() => stammdatenModel.getFehler('nachname')"
				:max-len="120" :readonly required />
			<svws-ui-text-input placeholder="Rufname"
				v-model="stammdatenModel.proxy.vorname"
				@change="stammdatenModel.patch"
				:validation="() => stammdatenModel.getFehler('vorname')"
				:max-len="80" :readonly required />
			<svws-ui-text-input placeholder="Alle Vornamen"
				v-model="stammdatenModel.proxy.alleVornamen"
				@change="stammdatenModel.patch"
				:validation="() => stammdatenModel.getFehler('alleVornamen')"
				:max-len="255" :readonly />
			<ui-select label="Geschlecht"
				v-model="stammdatenModel.geschlecht.value"
				:validation="() => stammdatenModel.getFehler('geschlecht')"
				:manager="geschlechtManager"
				:removable="false" required :readonly />
			<svws-ui-spacing />
			<svws-ui-text-input placeholder="Straße"
				v-model="stammdatenModel.strasse.value"
				@change="stammdatenModel.patch"
				:validation="() => stammdatenModel.getFehler('strassenname')"
				:max-len="55" :readonly />
			<ui-select label="Wohnort"
				v-model="stammdatenModel.wohnort.value"
				:manager="wohnortManager"
				:readonly />
			<ui-select label="Ortsteil"
				v-model="stammdatenModel.ortsteil.value"
				:manager="ortsteilManager"
				:disabled="istOrtsteilDisabled"
				:readonly />
			<svws-ui-spacing />
			<svws-ui-text-input placeholder="Geburtsdatum" type="date"
				v-model="stammdatenModel.proxy.geburtsdatum"
				@change="stammdatenModel.patch"
				:validation="() => stammdatenModel.getFehler('geburtsdatum')"
				required :readonly />
			<svws-ui-text-input placeholder="Geburtsort"
				v-model="stammdatenModel.proxy.geburtsort"
				@change="stammdatenModel.patch"
				:validation="() => stammdatenModel.getFehler('geburtsort')"
				:max-len="100" :readonly />
			<svws-ui-spacing />
			<svws-ui-text-input placeholder="Telefon" type="tel"
				v-model="stammdatenModel.proxy.telefon"
				@change="stammdatenModel.patch"
				:validation="() => stammdatenModel.getFehler('telefon')"
				:max-len="20" :readonly />
			<svws-ui-text-input placeholder="Mobil/Fax" type="tel"
				v-model="stammdatenModel.proxy.telefonMobil"
				@change="stammdatenModel.patch"
				:validation="() => stammdatenModel.getFehler('telefonMobil')"
				:max-len="20" :readonly />
			<svws-ui-text-input placeholder="E-Mail" type="email"
				v-model="stammdatenModel.proxy.emailPrivat"
				@change="stammdatenModel.patch"
				:validation="() => stammdatenModel.getFehler('emailPrivat')"
				:max-len="100" :readonly />
			<svws-ui-spacing />
			<ui-select label="1. Staatsangehörigkeit"
				v-model="stammdatenModel.staatsangehoerigkeit1.value"
				:validation="() => stammdatenModel.getFehler('idStaatsangehoerigkeit')"
				:manager="staatsangehoerigkeitenManager"
				:readonly
				required :removable="false" />
			<ui-select label="2. Staatsangehörigkeit"
				v-model="stammdatenModel.staatsangehoerigkeit2.value"
				:manager="staatsangehoerigkeitenManager"
				:readonly />
			<ui-select label="Konfession"
				v-model="stammdatenModel.religion.value"
				:manager="religionManager"
				:validation="() => stammdatenModel.getFehler('religionID')"
				:removable="false" required
				:readonly />
			<svws-ui-text-input placeholder="Abmeldung vom Religionsunterricht"
				v-model="stammdatenModel.proxy.religionabmeldung"
				@change="stammdatenModel.patch"
				type="date" :readonly />
			<svws-ui-checkbox v-model="stammdatenModel.proxy.hatMigrationshintergrund" :readonly>
				Migrationshintergrund vorhanden
			</svws-ui-checkbox>
			<svws-ui-input-number placeholder="Zuzugsjahr"
				v-model="stammdatenModel.proxy.zuzugsjahr"
				@change="stammdatenModel.patch"
				:validation="() => stammdatenModel.getFehler('zuzugsjahr')"
				:disabled="(!stammdatenModel.proxy.hatMigrationshintergrund || readonly)"
				:steps="false" :min :max />
			<ui-select label="Geburtsland"
				v-model="stammdatenModel.geburtsland.value"
				:manager="geburtslandManager"
				:disabled="(!stammdatenModel.proxy.hatMigrationshintergrund || readonly)" />
			<svws-ui-spacing />
			<ui-select label="Geburtsland Mutter"
				v-model="stammdatenModel.geburtslandMutter.value"
				:manager="geburtslandManager"
				:disabled="(!stammdatenModel.proxy.hatMigrationshintergrund || readonly)" />
			<ui-select label="Geburtsland Vater"
				v-model="stammdatenModel.geburtslandVater.value"
				:manager="geburtslandManager"
				:disabled="(!stammdatenModel.proxy.hatMigrationshintergrund || readonly)" />
			<ui-select label="Verkehrssprache"
				v-model="stammdatenModel.verkehrspracheFamilie.value"
				:manager="verkehrsspracheManager"
				:disabled="(!stammdatenModel.proxy.hatMigrationshintergrund || readonly)" />
			<svws-ui-spacing />
			<ui-select label="Fahrschüler"
				v-model="stammdatenModel.fahrschuelerArt.value"
				:manager="fahrschuelerartManager"
				:readonly />
			<ui-select label="Haltestelle"
				v-model="stammdatenModel.haltestelle.value"
				:manager="haltestellenManager"
				:readonly />
			<svws-ui-spacing />
			<ui-select label="Ext. ID-Nr."
				v-model="stammdatenModel.externeSchulNr.value"
				:manager="schulenManager"
				:readonly />
			<svws-ui-text-input placeholder="Schülerausweis-Nummer"
				v-model="stammdatenModel.proxy.idSchuelerausweis"
				@change="stammdatenModel.patch"
				:validation="() => stammdatenModel.getFehler('idSchuelerausweis')"
				:max-len="30" :readonly />
			<svws-ui-checkbox v-model="lernabschnittModel.proxy.hatSchwerbehinderungsNachweis" :readonly>
				Schwerstbehinderung
			</svws-ui-checkbox>
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed } from "vue";

	import type { NationalitaetenKatalogEintrag } from "@core/asd/data/schule/NationalitaetenKatalogEintrag";
	import type { VerkehrsspracheKatalogEintrag } from "@core/asd/data/schule/VerkehrsspracheKatalogEintrag";
	import { Geschlecht } from "@core/asd/types/Geschlecht";
	import { Nationalitaeten } from "@core/asd/types/schule/Nationalitaeten";
	import { Verkehrssprache } from "@core/asd/types/schule/Verkehrssprache";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useFahrschuelerartenState } from "@ui/states/kataloge/FahrschuelerartenState";
	import { useHaltestellenState } from "@ui/states/kataloge/HaltestellenState";
	import { useHerkunftschulenState } from "@ui/states/kataloge/HerkunftschulenState";
	import { useOrteState } from "@ui/states/kataloge/OrteState";
	import { useReligionenState } from "@ui/states/kataloge/ReligionenState";
	import { CoreTypeSelectManager } from "@ui/ui/controls/select/manager/CoreTypeSelectManager";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";

	import type { SchuelerIndividualdatenModelProxy } from "../individualdaten/modelproxy/SchuelerIndividualdatenModelProxy";
	import type { SchuelerLernabschnittAllgemeinModelProxy } from "../lernabschnitte/allgemein/modelproxy/SchuelerLernabschnittAllgemeinModelProxy";
	import { orte_sort, ortsteilSort } from "~/utils/helfer";

	const props = defineProps<{
		stammdatenModel: SchuelerIndividualdatenModelProxy;
		lernabschnittModel: SchuelerLernabschnittAllgemeinModelProxy;
		readonly: boolean;
	}>();

	// --- States ---

	const abschnittState = useAbschnittState();
	const orteState = useOrteState();
	const fahrschuelerartenState = useFahrschuelerartenState();
	const religionenState = useReligionenState();
	const haltestellenState = useHaltestellenState();
	const herkunftschulenState = useHerkunftschulenState();

	// --- Zuzugsjahr ---

	const max = new Date().getFullYear() + 1;
	const min = max - 100;

	const istOrtsteilDisabled = computed(() => (props.stammdatenModel.wohnort.value === null) && (props.stammdatenModel.ortsteil.value === null));

	// --- manager ---

	const geschlechtManager = new SelectManager({
		options: Geschlecht.values(),
		optionDisplayText: i => i.text,
		selectionDisplayText: i => i.text,
	});

	const wohnortManager = new SelectManager({
		options: computed(() => orteState.orte.list),
		sort: orte_sort,
		optionDisplayText: i => `${i.plz ?? ''} ${i.ortsname ?? ''}`.trim(),
		selectionDisplayText: i => `${i.plz ?? ''} ${i.ortsname ?? ''}`.trim(),
	});

	const ortsteilManager = new SelectManager({
		options: computed(() => orteState.ortsteile.listByOrtId(props.stammdatenModel.proxy.wohnortID)),
		sort: ortsteilSort,
		optionDisplayText: i => i.ortsteil ?? '',
		selectionDisplayText: i => i.ortsteil ?? '',
	});

	const staatsangehoerigkeitenManager = new CoreTypeSelectManager<NationalitaetenKatalogEintrag, Nationalitaeten>({
		clazz: Nationalitaeten.class,
		schuljahr: abschnittState.auswahl.schuljahr,
		sort: (a, b) => {
			if (a.staatsangehoerigkeit.length > 0 && b.staatsangehoerigkeit.length > 0) {
				return a.staatsangehoerigkeit.localeCompare(b.staatsangehoerigkeit);
			} else if (a.staatsangehoerigkeit.length > 0) {
				return -1;
			} else if (b.staatsangehoerigkeit.length > 0) {
				return 1;
			}
			return 0;
		},
		optionDisplayText: i => i.staatsangehoerigkeit,
		selectionDisplayText: i => i.staatsangehoerigkeit,
	});

	const religionManager = new SelectManager({
		options: computed(() => religionenState.religionen.list),
		optionDisplayText: i => i.bezeichnung,
		selectionDisplayText: i => i.bezeichnung,
	});

	const geburtslandManager = new CoreTypeSelectManager<NationalitaetenKatalogEintrag, Nationalitaeten>({
		clazz: Nationalitaeten.class,
		schuljahr: abschnittState.auswahl.schuljahr,
		sort: (a, b) => a.bezeichnung.localeCompare(b.bezeichnung),
		optionDisplayText: i => `${i.bezeichnung} (${i.iso3})`,
		selectionDisplayText: i => `${i.bezeichnung} (${i.iso3})`,
	});

	const verkehrsspracheManager = new CoreTypeSelectManager<VerkehrsspracheKatalogEintrag, Verkehrssprache>({
		clazz: Verkehrssprache.class,
		schuljahr: abschnittState.auswahl.schuljahr,
		sort: (a, b) => a.text.localeCompare(b.text),
		optionDisplayText: i => `${i.text} (${i.iso3})`,
		selectionDisplayText: i => `${i.text} (${i.iso3})`,
	});

	const fahrschuelerartManager = new SelectManager({
		options: computed(() => fahrschuelerartenState.fahrschuelerarten.list),
		optionDisplayText: i => i.bezeichnung ?? '',
		selectionDisplayText: i => i.bezeichnung ?? '',
	});

	const haltestellenManager = new SelectManager({
		options: computed(() => haltestellenState.haltestellen.list),
		optionDisplayText: i => i.bezeichnung ?? '',
		selectionDisplayText: i => i.bezeichnung ?? '',
	});

	const schulenManager = new SelectManager({
		options: computed(() => herkunftschulenState.herkunftschulen.list),
		optionDisplayText: i => i.kuerzel ?? i.schulnummerStatistik ?? i.kurzbezeichnung ?? i.name,
		selectionDisplayText: i => i.kuerzel ?? i.schulnummerStatistik ?? i.kurzbezeichnung ?? i.name,
	});

</script>
