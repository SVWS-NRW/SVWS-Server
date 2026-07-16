<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-modal-hilfe>
			<hilfe-schueler-schulbesuch />
		</svws-ui-modal-hilfe>
	</Teleport>
	<div class="page page-grid-cards">
		<!-- Vorhandene Abschlüsse !-->
		<svws-ui-content-card title="Vorhandene Abschlüsse">
			<svws-ui-input-wrapper :grid="2">
				<ui-select label="Höchster allgemeinbildender Abschluss"
					:manager="hoechsterAbschlussManager"
					v-model="model.hoechsterSchulabschluss.value"
					:readonly />
				<svws-ui-checkbox v-if="eigeneSchuleIstBKOderWBK"
					v-model="model.proxy.berufsabschlussVorhanden"
					:readonly>
					Berufsabschluss vorhanden
				</svws-ui-checkbox>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Schulbesuchsjahre" v-if="showSchulbesuchsjahre">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-input-number placeholder="Schulbesuchsjahre"
					:model-value="schulbesuchsjahre"
					readonly />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<div v-else />
		<!-- Im Schuljahr vor der Aufname !-->
		<svws-ui-content-card title="Im Schuljahr vor der Aufname" class="[&_.content-card--content]:min-h-105">
			<div class="pb-4 flex flex-row gap-6 items-center">
				<svws-ui-radio-option label="Öffentliche oder Ersatzschule in NRW"
					:model-value="currentMode"
					@update:model-value="setMode(Schulauswahl.INTERNAL)"
					:value="Schulauswahl.INTERNAL"
					:disabled="readonly && (currentMode !== Schulauswahl.INTERNAL)" />
				<svws-ui-radio-option label="Sonstige Schule"
					:model-value="currentMode"
					@update:model-value="setMode(Schulauswahl.EXTERNAL)"
					:value="Schulauswahl.EXTERNAL"
					:disabled="readonly && (currentMode !== Schulauswahl.EXTERNAL)" />
				<svws-ui-radio-option label="Kein Schulbesuch"
					:model-value="currentMode"
					@update:model-value="setMode(Schulauswahl.NONE)"
					:value="Schulauswahl.NONE"
					:disabled="readonly && (currentMode !== Schulauswahl.NONE)" />
			</div>
			<div class="flex" v-if="!keinSchulbesuch">
				<ui-select :label="labelVorherigeSchuleAuswahl" v-if="!keinSchulbesuch"
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
				<svws-ui-text-input placeholder="Schulform" v-if="!keinSchulbesuch"
					:model-value="model.bezeichnungSchulformVorherigeSchule.value"
					statistics readonly />
				<ui-select label="vorherige Tätigkeit / Herkunft" v-if="currentMode === Schulauswahl.NONE"
					:manager="schulformVorherigKeinAbschlussManager"
					v-model="model.schulformVorherigeSchuleKeinAbschluss.value"
					:readonly required :removable="false" />
				<svws-ui-text-input placeholder="Statistik-Schulnummer" v-if="!keinSchulbesuch"
					:model-value="model.schulnummerStatistik.value"
					statistics readonly />
				<svws-ui-text-input :placeholder="labelEntlassdatum" type="date"
					v-model="model.proxy.entlassdatumVorherigeSchule"
					statistics :readonly />
				<svws-ui-text-input placeholder="Bemerkung" span="full"
					v-model="model.proxy.bemerkungVorherigeSchule"
					:validation="() => model.getFehler('bemerkungVorherigeSchule')"
					@change="model.patch"
					:max-len="255" :readonly />
				<ui-select label="Höchster allgemeinbildender Abschluss" v-if="abschlussartAllgemeinbildendSelectable"
					:manager="abschlussartAllgemeinbildendVorherigeSchuleManager"
					v-model="model.abschlussartAllgemeinbildendVorherigeSchule.value"
					:readonly />
				<ui-select label="Höchster berufsbildender Abschluss" v-if="abschlussartBerufsbildendSelectable"
					:manager="abschlussartBerufsbildendVorherigeSchuleManager"
					v-model="model.abschlussartBerufsbildendVorherigeSchule.value"
					:readonly />
				<ui-select label="Versetzung" class="col-span-full"
					:manager="herkunftsartenManager"
					v-model="model.idHerkunftsartVersetzungVorherigeSchule.value"
					statistics :readonly />
				<ui-select label="Entlassjahrgang"
					:class="{ 'invisible pointer-events-none': currentMode === Schulauswahl.NONE }"
					:manager="vorherigeEntlassjahrgaengeManager"
					v-model="model.kuerzelEntlassjahrgangVorherigeSchule.value"
					statistics
					:disabled="model.vorherigeSchule.value === undefined" :readonly />
				<ui-select label="Entlassgrund"
					:class="{ 'invisible pointer-events-none': currentMode === Schulauswahl.NONE }"
					:manager="vorherigerEntlassgrundManager"
					v-model="model.idEntlassgrundVorherigeSchule.value"
					:readonly />
				<ui-select label="Schulgliederung" v-if="vorherigeSchuleIstBKOderWBK"
					:manager="schulgliederungManager"
					v-model="model.schulgliederungVorherigeSchule.value"
					:readonly />
				<ui-select label="Fachklasse" v-if="vorherigeSchuleIstBK"
					:manager="fachklasseManager"
					v-model="model.fachklasseVorherigeSchule.value"
					:readonly />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>

		<!-- Entlassung von eigener Schule !-->
		<svws-ui-content-card title="Entlassung von eigener Schule">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Entlassen am" type="date" class="contentFocusField"
					v-model="model.proxy.entlassdatumDieseSchule"
					statistics :readonly />
				<ui-select label="Entlassjahrgang"
					:manager="jahrgaengeManager"
					v-model="model.idEntlassjahrgangDieseSchule.value"
					:readonly />
				<ui-select label="Entlassgrund"
					:manager="entlassgrundManager"
					v-model="model.idEntlassgrundDieseSchule.value"
					:readonly />
				<svws-ui-text-input placeholder="Art des Abschlusses" span="full"
					v-model="model.proxy.idAbschlussartDieseSchule"
					disabled statistics :readonly />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>

		<!-- Wechsel zu aufnehmender Schule !-->
		<svws-ui-content-card title="Wechsel zu aufnehmender Schule">
			<template #actions>
				<svws-ui-checkbox v-if="serverState.hasDev"
					v-model="wechselBevorstehend"
					:indeterminate="manager().daten.wechselBestaetigtAufnehmendeSchule === null"
					disabled :readonly>
					<!-- Disabled, solange keine Backend-Funktionalität für den Schulwechsel implementiert ist. -->
					Wechsel bevorstehend
				</svws-ui-checkbox>
				<svws-ui-checkbox v-model="model.proxy.wechselBestaetigtAufnehmendeSchule"
					:indeterminate="manager().daten.wechselBestaetigtAufnehmendeSchule === null"
					focus-class-content :readonly>
					Aufnahme bestätigt
				</svws-ui-checkbox>
			</template>
			<svws-ui-input-wrapper :grid="2">
				<ui-select label="Schule"
					:manager="aufnehmendeSchuleManager"
					v-model="model.idAufnehmendeSchule.value"
					:readonly />
				<svws-ui-button type="transparent"
					@click="goToSchule(manager().daten.idAufnehmendeSchule ?? -1)"
					:disabled="manager().daten.idAufnehmendeSchule === null"
					:readonly>
					<span class="icon i-ri-link" />Zur Schule
				</svws-ui-button>
				<svws-ui-text-input placeholder="Wechseldatum" type="date"
					:model-value="manager().daten.wechseldatumAufnehmendeSchule"
					readonly />
				<ui-select label="Wechselgrund"
					v-if="serverState.hasDev"
					:manager="schulwechselGrundSelectManager"
					:disabled="!wechselBevorstehend" :readonly />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>

		<!-- Kindergartenbesuch !-->
		<svws-ui-content-card title="Kindergartenbesuch" v-if="schuleHatPrimarstufe">
			<svws-ui-input-wrapper :grid="2">
				<ui-select label="Name des Kindergartens"
					:manager="kindergartenManager"
					v-model="model.idKindergarten.value"
					:readonly />
				<ui-select label="Dauer des Kindergartenbesuchs"
					:manager="dauerKindergartenbesuchManager"
					v-model="model.idDauerKindergartenbesuch.value"
					:readonly />
				<svws-ui-checkbox v-model="model.proxy.verpflichtungSprachfoerderkurs" :readonly>
					Verpflichtung für Sprachförderkurs
				</svws-ui-checkbox>
				<svws-ui-checkbox v-model="model.proxy.teilnahmeSprachfoerderkurs" :readonly>
					Teilnahme an Sprachförderkurs
				</svws-ui-checkbox>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<div v-else />

		<!-- Grundschulbesuch !-->
		<svws-ui-content-card title="Grundschulbesuch">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-input-number placeholder="Einschulung" class="contentFocusField"
					v-model="model.proxy.einschulungsjahrGrundschule"
					:validation="() => model.getFehler('einschulungsjahrGrundschule')"
					@change="model.patch"
					:min="1900" :max="2100"
					statistics :readonly />
				<ui-select label="Einschulungsart"
					:manager="einschulungsartenManager"
					v-model="model.idEinschulungsartGrundschule.value"
					:readonly />
				<ui-select label="EP-Jahre"
					:manager="grundschuleJahreEingangsphaseManager"
					v-model="model.idEingangsphaseGrundschule.value"
					:readonly />
				<ui-select label="Übergangsempfehlung Jg. 5"
					:manager="grundschuleUebergangsempfehlungManager"
					v-model="model.idUebergangsempfehlungGrundschule.value"
					:readonly />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>

		<!-- Sekundarstufe I !-->
		<svws-ui-content-card title="Sekundarstufe I">
			<svws-ui-input-wrapper>
				<svws-ui-input-number placeholder="Jahr Wechsel Sek I" class="contentFocusField"
					v-model="model.proxy.wechseljahrSekI"
					:validation="() => model.getFehler('wechseljahrSekI')"
					@change="model.patch"
					:min="1900" :max="2100"
					statistics :readonly />
				<ui-select label="Erste Schulform Sek I"
					:manager="sekIErsteSchulformManager"
					v-model="model.kuerzelErsteSchulformSek1.value"
					:readonly />
				<svws-ui-input-number placeholder="Jahr Wechsel Sek II"
					v-model="model.proxy.wechseljahrSekII"
					:validation="() => model.getFehler('wechseljahrSekII')"
					@change="model.patch"
					:min="1900" :max="2100"
					statistics :readonly />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<schueler-schulbesuch-merkmale :manager
			:get-merkmale="() => props.manager().daten.merkmale"
			:add-merkmal
			:patch-merkmal
			:delete-merkmale
			:update-kompetenz />
		<schueler-schulbesuch-bisherige-schulen :manager
			:get-bisherige-schulen="() => props.manager().daten.bisherBesuchteSchulen"
			:add-bisherige-schule
			:patch-bisherige-schule
			:delete-bisherige-schulen
			:update-kompetenz />
	</div>
</template>

<script setup lang="ts">

	import type { JahrgangsDaten, KatalogEntlassgrund, Kindergarten, List, SchulEintrag } from "@core";
	import { ArrayList, BenutzerKompetenz, Einschulungsart, Jahrgaenge, Kindergartenbesuch, PrimarstufeSchuleingangsphaseBesuchsjahre, Herkunftsarten,
		SchulabschlussAllgemeinbildend, Schulform, Uebergangsempfehlung, SchulabschlussBerufsbildend, HerkunftSonstige, Schulgliederung, Fachklasse } from "@core";
	import type { SchuelerSchulbesuchProps } from './SchuelerSchulbesuchProps';
	import { CoreTypeSelectManager, SelectManager, useBenutzerState, useSchuleState, useServerState } from "@ui";
	import { computed, ref, watch } from "vue";
	import { SchuelerSchulbesuchModelProxy } from "~/components/schueler/schulbesuch/modelProxy/SchuelerSchulbesuchModelProxy";

	const props = defineProps<SchuelerSchulbesuchProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();
	const serverState = useServerState();

	const updateKompetenz = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));
	const readonly = computed(() => !updateKompetenz.value);
	const model = new SchuelerSchulbesuchModelProxy(
		() => props.manager().daten,
		() => props.manager(),
		(data) => props.patch(props.manager().daten.id, data)
	);
	const schuljahr = computed(() => props.manager().schuljahr);
	const vorherigeSchuleIstBK = computed(() => {
		return model.vorherigeSchulform.value !== null &&
			[Schulform.BK, Schulform.SB].includes(model.vorherigeSchulform.value);
	});
	const vorherigeSchuleIstBKOderWBK = computed(() => {
		return model.vorherigeSchulform.value !== null &&
			[Schulform.BK, Schulform.SB, Schulform.WB].includes(model.vorherigeSchulform.value);
	});
	const schuleHatPrimarstufe = computed(
		() => [Schulform.G, Schulform.FW, Schulform.WF, Schulform.GM, Schulform.KS, Schulform.S, Schulform.GE, Schulform.V].includes(schuleState.schulform));
	const eigeneSchuleIstBKOderWBK = computed(
		() => [Schulform.SB, Schulform.BK, Schulform.WB].includes(schuleState.schulform));

	const wechselBevorstehend = ref<boolean>(false);
	const keinSchulbesuch = computed(() => currentMode.value === Schulauswahl.NONE);

	// --- Schulbesuchsjahre ---
	const showSchulbesuchsjahre = computed(
		() => [Schulform.FW, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.R, Schulform.S, Schulform.KS, Schulform.SG, Schulform.SK,
			Schulform.SR, Schulform.V, Schulform.WF, Schulform.HI].includes(schuleState.schulform));
	const schulbesuchsjahre = computed<number | null>(() => {
		if ((model.proxy.einschulungsjahrGrundschule === null) || (model.proxy.idEingangsphaseGrundschule === null)) {
			return null;
		}
		const selectedEingangsPhase = PrimarstufeSchuleingangsphaseBesuchsjahre.data().getWertByIDOrNull(model.proxy.idEingangsphaseGrundschule);
		if (selectedEingangsPhase === null) {
			return null;
		}
		let result = schuleState.abschnitt.schuljahr - model.proxy.einschulungsjahrGrundschule;
		// Das dritte EP-Jahr wird nicht zum Schulbesuch dazugerechnet -> als würde man ein Jahr später eingeschult werden
		if (PrimarstufeSchuleingangsphaseBesuchsjahre.E3.statistikId() === selectedEingangsPhase.statistikId()) {
			result = result - 1;
		}
		return Math.max(result, 0);
	});


	// --- Toggle Schulauswahl ---
	const schulauswahlMode = ref<Schulauswahl | null>(null);

	watch(() => props.manager().daten.id, () => {
		schulauswahlMode.value = null;
	});

	const currentMode = computed<Schulauswahl>(() => {
		if (schulauswahlMode.value !== null) {
			return schulauswahlMode.value;
		}
		const firstDigit = model.vorherigeSchule.value?.schulnummerStatistik?.charAt(0);
		if (firstDigit === "1") {
			return Schulauswahl.INTERNAL;
		}
		if (firstDigit === "9") {
			return Schulauswahl.EXTERNAL;
		}
		return Schulauswahl.NONE;
	});

	function setMode(newMode: Schulauswahl) {
		if (newMode === currentMode.value) {
			return;
		}
		model.vorherigeSchule.value = null;
		schulauswahlMode.value = newMode;
		vorherigeSchuleManager.updateFilteredOptions();
	}

	enum Schulauswahl { INTERNAL, EXTERNAL, NONE }

	const abschlussartBerufsbildendSelectable = computed(() => {
		if (currentMode.value === Schulauswahl.NONE) {
			return true;
		}
		return model.vorherigeSchulform.value !== null &&
			[Schulform.SB, Schulform.BK, Schulform.WB].includes(model.vorherigeSchulform.value);
	});

	const abschlussartAllgemeinbildendSelectable = computed(() => {
		return !(model.vorherigeSchulform.value !== null && [Schulform.G].includes(model.vorherigeSchulform.value));
	});


	const vorherigeSchuleFilter = {
		key: "schulauswahlModus",
		apply: (options: List<SchulEintrag>) => {
			const filtered = new ArrayList<SchulEintrag>();
			let praefix: string | null;
			if (currentMode.value === Schulauswahl.INTERNAL) {
				praefix = "1";
			} else if (currentMode.value === Schulauswahl.EXTERNAL) {
				praefix = "9";
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
		if (currentMode.value === Schulauswahl.INTERNAL) {
			return 'Öffentliche oder Ersatz-Schule in NRW';
		}
		if (currentMode.value === Schulauswahl.EXTERNAL) {
			return 'Schule außerhalb von NRW oder sonstige Schule in NRW';
		}
		return '';
	});

	const labelEntlassdatum = computed(() => {
		if (currentMode.value === Schulauswahl.NONE) {
			return 'Datum';
		}
		return 'Entlassen am';
	});

	const hoechsterAbschlussManager = new CoreTypeSelectManager({
		clazz: SchulabschlussAllgemeinbildend.class,
		schuljahr: schuljahr,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const vorherigeSchuleManager = new SelectManager<SchulEintrag>({
		filters: [vorherigeSchuleFilter],
		options: computed(() => props.manager().schulenById.values()),
		optionDisplayText: bezeichnungSchule,
		selectionDisplayText: bezeichnungSchule,
	});

	const vorherigeEntlassjahrgaengeManager = new CoreTypeSelectManager({
		clazz: Jahrgaenge.class,
		schuljahr: schuljahr,
		schulformen: model.vorherigeSchulform,
		optionDisplayText: "kuerzelText",
		selectionDisplayText: "kuerzelText",
	});

	const schulformVorherigKeinAbschlussManager = new CoreTypeSelectManager({
		clazz: HerkunftSonstige.class,
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

	const jahrgaengeManager = new SelectManager<JahrgangsDaten>({
		options: computed(() => props.manager().jahrgaengeById.values()),
		optionDisplayText: j => j.bezeichnung ?? '-',
		selectionDisplayText: j => j.bezeichnung ?? '-',
	});

	const entlassgrundManager = new SelectManager<KatalogEntlassgrund>({
		options: computed(() => props.manager().entlassgruendeById.values()),
		optionDisplayText: s => s.bezeichnung,
		selectionDisplayText: s => s.bezeichnung,
	});

	const kindergartenManager = new SelectManager<Kindergarten>({
		options: computed(() => props.manager().kindergaertenById.values()),
		optionDisplayText: s => s.bezeichnung,
		selectionDisplayText: s => s.bezeichnung,
	});

	const dauerKindergartenbesuchManager = new CoreTypeSelectManager({
		clazz: Kindergartenbesuch.class,
		schuljahr: schuljahr,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const aufnehmendeSchuleManager = new SelectManager<SchulEintrag>({
		options: computed(() => props.manager().schulenById.values()),
		optionDisplayText: bezeichnungSchule,
		selectionDisplayText: bezeichnungSchule,
	});

	const einschulungsartenManager = new CoreTypeSelectManager({
		clazz: Einschulungsart.class,
		schuljahr: schuljahr,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const grundschuleJahreEingangsphaseManager = new CoreTypeSelectManager({
		clazz: PrimarstufeSchuleingangsphaseBesuchsjahre.class,
		schuljahr: schuljahr,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const grundschuleUebergangsempfehlungManager = new CoreTypeSelectManager({
		clazz: Uebergangsempfehlung.class,
		schuljahr: schuljahr,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const sekIErsteSchulformManager = new CoreTypeSelectManager({
		clazz: Schulform.class,
		schuljahr: schuljahr,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const schulgliederungManager = new CoreTypeSelectManager({
		clazz: Schulgliederung.class,
		schuljahr: schuljahr,
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
		schulformen: model.vorherigeSchulform,
		optionDisplayText: "kuerzelText",
		selectionDisplayText: "kuerzelText",
	});

	const schulwechselGrundSelectManager = new SelectManager({
		options: computed(() => props.manager().entlassgruendeById.values()),
		optionDisplayText: (option) => option.bezeichnung,
		selectionDisplayText: (option) => option.bezeichnung,
	});

	function bezeichnungSchule(s: SchulEintrag) {
		return `${s.schulnummerStatistik}: ${s.name}`;
	}

</script>
