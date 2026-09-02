<template>
	<!-- Im Schuljahr vor der Aufname !-->
	<svws-ui-content-card title="Im Schuljahr vor der Aufname" class="[&_.content-card--content]:min-h-105">
		<!-- Toggle !-->
		<div class="pb-4 flex flex-row gap-6 items-center">
			<svws-ui-radio-option label="Öffentliche oder Ersatzschule in NRW"
				:model-value="currentMode"
				@update:model-value="setMode(Schulauswahl.SCHULE_IN_NRW)"
				:value="Schulauswahl.SCHULE_IN_NRW"
				:disabled />
			<svws-ui-radio-option label="Sonstige Schule"
				:model-value="currentMode"
				@update:model-value="setMode(Schulauswahl.SONSTIGE_SCHULE)"
				:value="Schulauswahl.SONSTIGE_SCHULE"
				:disabled />
			<svws-ui-radio-option label="Kein Schulbesuch"
				:model-value="currentMode"
				@update:model-value="setMode(Schulauswahl.KEIN_SCHULBESUCH)"
				:value="Schulauswahl.KEIN_SCHULBESUCH"
				:disabled />
		</div>
		<!-- Schule !-->
		<div class="flex" v-if="!keinSchulbesuchSelected">
			<ui-select :label="labelVorherigeSchuleAuswahl"
				:manager="vorherigeSchuleManager"
				v-model="model.vorherigeSchule.value"
				:readonly />
			<svws-ui-button type="transparent" class="min-w-fit"
				@click="goToSchule(manager().daten.idVorherigeSchule ?? -1)"
				:disabled="manager().daten.idVorherigeSchule === null">
				<span class="icon i-ri-link" />Zur Schule
			</svws-ui-button>
		</div>
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Schulform" v-if="schuleInNRWSelected"
				:model-value="model.bezeichnungSchulformVorherigeSchule.value"
				statistics readonly />
			<svws-ui-text-input placeholder="Schulform" v-if="sonstigeSchuleSelected"
				:model-value="model.bezeichnungHerkunftSchulformVorherigeSchule.value"
				statistics readonly />
			<ui-select label="vorherige Tätigkeit / Herkunft" v-if="currentMode === Schulauswahl.KEIN_SCHULBESUCH"
				:manager="schulformVorherigKeinAbschlussManager"
				v-model="model.schulformVorherigeSchuleKeinSchulbesuch.value"
				:readonly required :removable="false" />
			<svws-ui-text-input placeholder="Statistik-Schulnummer" v-if="!keinSchulbesuchSelected"
				:model-value="model.schulnummerStatistik.value"
				statistics readonly />
			<ui-select label="Schulgliederung" v-if="vorherigeSchuleIstBKOderWBK"
				:manager="schulgliederungManager"
				v-model="model.schulgliederungVorherigeSchule.value"
				:readonly statistics />
			<ui-select label="Fachklasse" v-if="vorherigeSchuleIstBK"
				:manager="fachklasseManager"
				v-model="model.fachklasseVorherigeSchule.value"
				:readonly statistics />
			<svws-ui-text-input :placeholder="labelEntlassdatum" type="date"
				v-model="model.proxy.entlassdatumVorherigeSchule"
				statistics :readonly />
			<ui-select label="Entlassjahrgang" v-if="currentMode !== Schulauswahl.KEIN_SCHULBESUCH"
				:manager="vorherigeEntlassjahrgaengeManager"
				v-model="model.kuerzelEntlassjahrgangVorherigeSchule.value"
				statistics
				:disabled="model.vorherigeSchule.value === undefined" :readonly />
			<ui-select label="Entlassgrund" v-if="currentMode !== Schulauswahl.KEIN_SCHULBESUCH"
				:manager="vorherigerEntlassgrundManager"
				v-model="model.idEntlassgrundVorherigeSchule.value"
				:readonly />
			<div v-if="currentMode === Schulauswahl.SCHULE_IN_NRW" />
			<ui-select label="Hochschulabschluss" v-if="sonstigeSchuleSelected && eigeneSchuleIstBKorSBorWB"
				:manager="hochschulabschlussManager"
				v-model="model.idHochschulabschluss.value"
				:readonly statistics />
			<ui-select label="Höchster allgemeinbildender Abschluss" v-if="abschlussartAllgemeinbildendSelectable"
				:manager="abschlussartAllgemeinbildendVorherigeSchuleManager"
				v-model="model.abschlussartAllgemeinbildendVorherigeSchule.value"
				:readonly statistics />
			<ui-select label="Höchster berufsbildender Abschluss" v-if="abschlussartBerufsbildendSelectable"
				:manager="abschlussartBerufsbildendVorherigeSchuleManager"
				v-model="model.abschlussartBerufsbildendVorherigeSchule.value"
				:readonly statistics />
			<ui-select label="Versetzung" class="col-span-full" v-if="currentMode !== Schulauswahl.KEIN_SCHULBESUCH"
				:manager="herkunftsartenManager"
				v-model="model.idHerkunftsartVersetzungVorherigeSchule.value"
				statistics :readonly />
			<svws-ui-text-input placeholder="Bemerkung" span="full"
				v-model="model.proxy.bemerkungVorherigeSchule"
				:validation="() => model.getFehler('bemerkungVorherigeSchule')"
				@change="model.patch"
				:max-len="255" :readonly />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">


	import { computed, ref, watch } from "vue";
	import { ArrayList, BenutzerKompetenz, Fachklasse, HerkunftBildungsgang, Herkunftsarten, HerkunftSonstige,
		Hochschulabschluss, Jahrgaenge, SchulabschlussAllgemeinbildend,
		SchulabschlussBerufsbildend, Schulform } from "@core";
	import type { List, SchulEintrag, KatalogEntlassgrund } from "@core";
	import type { SchuelerSchulbesuchManager } from "@ui";
	import { CoreTypeSelectManager, SelectManager, useBenutzerState, useSchuleState } from "@ui";
	import type { SchuelerSchulbesuchModelProxy } from "~/components/schueler/schulbesuch/modelProxy/SchuelerSchulbesuchModelProxy";

	const props = defineProps<{
		manager: () => SchuelerSchulbesuchManager;
		goToSchule: (idSchule: number) => Promise<void>;
		model: SchuelerSchulbesuchModelProxy;
		readonly: boolean;
	}>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();
	const updateKompetenz = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));
	const disabled = computed(() => !updateKompetenz.value);
	const schuljahr = computed(() => props.manager().schuljahr);
	const PRAEFIX_SCHULNUMMER_NRW = "1";
	const PRAEFIX_SCHULNUMMER_SONSTIGE = "9";

	// Toggle
	enum Schulauswahl { SCHULE_IN_NRW, SONSTIGE_SCHULE, KEIN_SCHULBESUCH }

	const schulauswahlMode = ref<Schulauswahl | null>(null);
	const currentMode = computed<Schulauswahl>(() => {
		if (schulauswahlMode.value !== null) {
			return schulauswahlMode.value;
		}
		const firstDigit = props.model.vorherigeSchule.value?.schulnummerStatistik?.charAt(0);
		if (firstDigit === PRAEFIX_SCHULNUMMER_NRW) {
			return Schulauswahl.SCHULE_IN_NRW;
		}
		if (firstDigit === PRAEFIX_SCHULNUMMER_SONSTIGE) {
			return Schulauswahl.SONSTIGE_SCHULE;
		}
		return Schulauswahl.KEIN_SCHULBESUCH;
	});

	function setMode(newMode: Schulauswahl) {
		if (newMode === currentMode.value) {
			return;
		}
		props.model.vorherigeSchule.value = null;
		schulauswahlMode.value = newMode;
		vorherigeSchuleManager.updateFilteredOptions();
	}

	const schuleInNRWSelected = computed(() => currentMode.value === Schulauswahl.SCHULE_IN_NRW);
	const sonstigeSchuleSelected = computed(() => currentMode.value === Schulauswahl.SONSTIGE_SCHULE);
	const keinSchulbesuchSelected = computed(() => currentMode.value === Schulauswahl.KEIN_SCHULBESUCH);
	const eigeneSchuleIstGrundschule = computed(() => schuleState.schulform === Schulform.G);
	const eigeneSchuleIstBKorSBorWB = computed(() => (schuleState.schulform === Schulform.BK)
		|| (schuleState.schulform === Schulform.SB)
		|| (schuleState.schulform === Schulform.WB)
	);

	watch(() => props.manager().daten.id, () => {
		schulauswahlMode.value = null;
	});

	const abschlussartBerufsbildendSelectable = computed(() => {
		if (eigeneSchuleIstGrundschule.value) {
			return false;
		}
		if (currentMode.value === Schulauswahl.KEIN_SCHULBESUCH) {
			return true;
		}
		return props.model.schulformVorherigeSchule.value !== null &&
			[Schulform.SB, Schulform.BK, Schulform.WB].includes(props.model.schulformVorherigeSchule.value);
	});

	const vorherigeSchuleIstGrundschule = computed(() => ((props.model.schulformVorherigeSchule.value !== null)
		&& (props.model.schulformVorherigeSchule.value === Schulform.G)));

	const abschlussartAllgemeinbildendSelectable = computed(
		() => !vorherigeSchuleIstGrundschule.value
			&& !eigeneSchuleIstGrundschule.value
	);

	const vorherigeSchuleIstBK = computed(() => {
		return props.model.schulformVorherigeSchule.value !== null &&
			[Schulform.BK, Schulform.SB].includes(props.model.schulformVorherigeSchule.value);
	});
	const vorherigeSchuleIstBKOderWBK = computed(() => {
		return props.model.schulformVorherigeSchule.value !== null &&
			[Schulform.BK, Schulform.SB, Schulform.WB].includes(props.model.schulformVorherigeSchule.value);
	});

	const vorherigeSchuleFilter = {
		key: "schulauswahlModus",
		apply: (options: List<SchulEintrag>) => {
			const filtered = new ArrayList<SchulEintrag>();
			let praefix: string | null;
			if (currentMode.value === Schulauswahl.SCHULE_IN_NRW) {
				praefix = PRAEFIX_SCHULNUMMER_NRW;
			} else if (currentMode.value === Schulauswahl.SONSTIGE_SCHULE) {
				praefix = PRAEFIX_SCHULNUMMER_SONSTIGE;
			} else {
				return filtered;
			}
			for (const option of options) {
				if (option.schulnummerStatistik?.charAt(0) === praefix) {
					filtered.add(option);
				}
			}
			return filtered;
		},
	};

	const labelVorherigeSchuleAuswahl = computed(() => {
		if (currentMode.value === Schulauswahl.SCHULE_IN_NRW) {
			return 'Öffentliche oder Ersatz-Schule in NRW';
		}
		if (currentMode.value === Schulauswahl.SONSTIGE_SCHULE) {
			return 'Schule außerhalb von NRW oder sonstige Schule in NRW';
		}
		return '';
	});

	const labelEntlassdatum = computed(() => {
		if (currentMode.value === Schulauswahl.KEIN_SCHULBESUCH) {
			return 'Datum';
		}
		return 'Entlassen am';
	});

	const vorherigeSchuleManager = new SelectManager<SchulEintrag>({
		filters: [vorherigeSchuleFilter],
		options: computed(() => props.manager().schulenById.values()),
		optionDisplayText: bezeichnungSchule,
		selectionDisplayText: bezeichnungSchule,
	});

	function bezeichnungSchule(s: SchulEintrag) {
		return `${s.schulnummerStatistik}: ${s.name}`;
	}

	const vorherigeEntlassjahrgaengeManager = new CoreTypeSelectManager({
		clazz: Jahrgaenge.class,
		schuljahr: schuljahr,
		schulformen: props.model.schulformVorherigeSchule,
		optionDisplayText: "kuerzelText",
		selectionDisplayText: "kuerzelText",
	});

	const schulformVorherigKeinAbschlussManager = new CoreTypeSelectManager({
		clazz: HerkunftSonstige.class,
		schuljahr: schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "kuerzelText",
		selectionDisplayText: "kuerzelText",
	});

	const hochschulabschlussManager = new CoreTypeSelectManager({
		clazz: Hochschulabschluss.class,
		schuljahr: schuljahr,
		optionDisplayText: "kuerzelText",
		selectionDisplayText: "kuerzelText",
	});

	const vorherigerEntlassgrundManager = new SelectManager<KatalogEntlassgrund>({
		options: computed(() => props.manager().entlassgruendeById.values()),
		optionDisplayText: s => s.bezeichnung,
		selectionDisplayText: s => s.bezeichnung,
	});

	const abschlussartAllgemeinbildendVorherigeSchuleManager = new CoreTypeSelectManager({
		clazz: SchulabschlussAllgemeinbildend.class,
		schuljahr: schuljahr,
		optionDisplayText: "kuerzelText",
		selectionDisplayText: "kuerzelText",
	});

	const abschlussartBerufsbildendVorherigeSchuleManager = new CoreTypeSelectManager({
		clazz: SchulabschlussBerufsbildend.class,
		schuljahr: schuljahr,
		optionDisplayText: "kuerzelText",
		selectionDisplayText: "kuerzelText",
	});

	const schulgliederungManager = new CoreTypeSelectManager({
		clazz: HerkunftBildungsgang.class,
		schuljahr: schuljahr,
		schulformen: props.model.schulformVorherigeSchule,
		optionDisplayText: "kuerzelText",
		selectionDisplayText: "kuerzelText",
	});

	const fachklasseManager = new CoreTypeSelectManager({
		clazz: Fachklasse.class,
		schuljahr: schuljahr,
		optionDisplayText: "kuerzelText",
		selectionDisplayText: "kuerzelText",
	});

	const herkunftsartenManager = new CoreTypeSelectManager({
		clazz: Herkunftsarten.class,
		schuljahr: schuljahr,
		schulformen: props.model.schulformVorherigeSchule,
		optionDisplayText: "kuerzelText",
		selectionDisplayText: "kuerzelText",
	});


</script>

