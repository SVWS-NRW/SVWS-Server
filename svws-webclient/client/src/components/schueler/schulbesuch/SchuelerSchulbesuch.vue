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
				<svws-ui-checkbox title="Berufsabschluss vorhanden" v-if="schuleIstBKoderWBK"
					v-model="model.proxy.berufsabschlussVorhanden"
					:readonly>
					Berufsabschluss vorhanden
				</svws-ui-checkbox>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<div />
		<!-- Im Schuljahr vor der Aufname !-->
		<svws-ui-content-card title="Im Schuljahr vor der Aufname">
			<div class="pb-4 flex flex-row gap-6 items-center">
				<svws-ui-radio-option label="Schulbesuch in NRW"
					:model-value="currentMode"
					@update:model-value="setMode(Schulauswahl.INTERNAL)"
					:value="Schulauswahl.INTERNAL" />
				<svws-ui-radio-option label="Außerhalb von NRW"
					:model-value="currentMode"
					@update:model-value="setMode(Schulauswahl.EXTERNAL)"
					:value="Schulauswahl.EXTERNAL" />
				<svws-ui-radio-option label="Kein Schulbesuch"
					:model-value="currentMode"
					@update:model-value="setMode(Schulauswahl.NONE)"
					:value="Schulauswahl.NONE" />
			</div>
			<svws-ui-input-wrapper :grid="2">
				<ui-select label="Schule"
					:class="{ 'invisible pointer-events-none': currentMode === Schulauswahl.NONE }"
					:manager="vorherigeSchuleManager"
					v-model="model.vorherigeSchule.value"
					:readonly />
				<div class="flex" :class="{ 'invisible pointer-events-none': currentMode === Schulauswahl.NONE }">
					<svws-ui-text-input placeholder="Statistik-Schulnummer"
						:model-value="model.schulnummerStatistik.value"
						statistics readonly />
					<svws-ui-button type="transparent" class="min-w-fit"
						@click="goToSchule(manager().daten.idVorherigeSchule ?? -1)"
						:disabled="manager().daten.idVorherigeSchule === null">
						<span class="icon i-ri-link" />Zur Schule
					</svws-ui-button>
				</div>

				<svws-ui-text-input placeholder="Schulform" v-if="currentMode === Schulauswahl.INTERNAL"
					:model-value="model.schulformVorherigeSchuleIntern.value"
					statistics readonly />
				<ui-select label="Schulform" v-if="currentMode === Schulauswahl.EXTERNAL"
					:manager="schulformVorherigExternManager"
					v-model="model.schulformVorherigeSchuleExtern.value"
					:disabled="model.vorherigeSchule.value === null"
					:readonly required :removable="false" />
				<ui-select label="Schulform" v-if="currentMode === Schulauswahl.NONE"
					:manager="schulformVorherigKeinAbschlussManager"
					v-model="model.schulformVorherigeSchuleKeinAbschluss.value"
					:readonly required :removable="false" />
				<svws-ui-text-input placeholder="Entlassen am" type="date"
					v-model="model.proxy.entlassdatumVorherigeSchule"
					statistics :readonly />
				<ui-select label="Entlassjahrgang"
					:manager="vorherigeEntlassjahrgaengeManager"
					v-model="model.kuerzelEntlassjahrgangVorherigeSchule.value"
					statistics
					:disabled="model.vorherigeSchule.value === undefined" :readonly />
				<svws-ui-text-input placeholder="Bemerkung" span="full"
					v-model="model.proxy.bemerkungVorherigeSchule"
					:validation="() => model.getFehler('bemerkungVorherigeSchule')"
					@commit="model.patch"
					:max-len="255" :readonly />
				<ui-select label="Entlassgrund"
					:manager="vorherigerEntlassgrundManager"
					v-model="model.idEntlassgrundVorherigeSchule.value"
					:readonly />
				<ui-select label="Abschlussart Allgemeinbildend"
					:manager="abschlussartAllgemeinbildendVorherigeSchuleManager"
					v-model="model.abschlussartAllgemeinbildendVorherigeSchule.value"
					:readonly />
				<ui-select label="Abschlussart Berufsbildend" v-if="abschlussartBerufsbildendSelectable"
					:manager="abschlussartBerufsbildendVorherigeSchuleManager"
					v-model="model.abschlussartBerufsbildendVorherigeSchule.value"
					:readonly />
				<!-- TODO: durch Ui-Select ersetzen: siehe Issue#3495-->
				<svws-ui-text-input placeholder="Versetzung" span="full"
					:model-value="model.idHerkunftsartVersetzungVorherigeSchule.value?.text ?? ''"
					readonly statistics />
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
				<svws-ui-checkbox title="Wechsel bevorstehend" v-if="serverState.hasDev"
					v-model="wechselBevorstehend"
					:indeterminate="manager().daten.wechselBestaetigtAufnehmendeSchule === null"
					disabled :readonly>
					<!-- Disabled, solange keine Backend-Funktionalität für den Schulwechsel implementiert ist. -->
					Wechsel bevorstehend
				</svws-ui-checkbox>
				<svws-ui-checkbox title="Aufnahme bestätigt"
					v-model="model.proxy.wechselBestaetigtAufnehmendeSchule"
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
				<svws-ui-checkbox title="Verpflichtung f. Sprachförderkurs"
					v-model="model.proxy.verpflichtungSprachfoerderkurs"
					:readonly>
					Verpflichtung für Sprachförderkurs
				</svws-ui-checkbox>
				<svws-ui-checkbox title="Teilnahme an Sprachförderkurs"
					v-model="model.proxy.teilnahmeSprachfoerderkurs"
					:readonly>
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
					@commit="model.patch"
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
					@commit="model.patch"
					:min="1900" :max="2100"
					statistics :readonly />
				<ui-select label="Erste Schulform Sek I"
					:manager="sekIErsteSchulformManager"
					v-model="model.kuerzelErsteSchulformSek1.value"
					:readonly />
				<svws-ui-input-number placeholder="Jahr Wechsel Sek II"
					v-model="model.proxy.wechseljahrSekII"
					:validation="() => model.getFehler('wechseljahrSekII')"
					@commit="model.patch"
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
	import { ArrayList, BenutzerKompetenz, Einschulungsart, Jahrgaenge, Kindergartenbesuch, PrimarstufeSchuleingangsphaseBesuchsjahre,
		SchulabschlussAllgemeinbildend, Schulform, Uebergangsempfehlung, SchulabschlussBerufsbildend, HerkunftSchulform, HerkunftSonstige } from "@core";
	import type { SchuelerSchulbesuchProps } from './SchuelerSchulbesuchProps';
	import { CoreTypeSelectManager, SelectManager, useSchuleState, useServerState } from "@ui";
	import { computed, ref, watch } from "vue";
	import { SchuelerSchulbesuchModelProxy } from "~/components/schueler/schulbesuch/modelProxy/SchuelerSchulbesuchModelProxy";

	const props = defineProps<SchuelerSchulbesuchProps>();
	const schuleState = useSchuleState();
	const serverState = useServerState();

	const updateKompetenz = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));
	const readonly = computed(() => !updateKompetenz.value);
	const model = new SchuelerSchulbesuchModelProxy(
		() => props.manager().daten,
		() => props.manager(),
		(data) => props.patch(props.manager().daten.id, data)
	);
	const schuljahr = computed(() => props.manager().schuljahr);
	const schuleHatPrimarstufe = computed(
		() => [Schulform.G, Schulform.FW, Schulform.WF, Schulform.GM, Schulform.KS, Schulform.S, Schulform.GE, Schulform.V].includes(schuleState.schulform));
	const schuleIstBKoderWBK = computed(
		() => [Schulform.SB, Schulform.BK, Schulform.WB].includes(schuleState.schulform));
	const wechselBevorstehend = ref<boolean>(false);

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
			[Schulform.SB, Schulform.BK].includes(model.vorherigeSchulform.value);
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
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const schulformVorherigExternManager = new CoreTypeSelectManager({
		clazz: HerkunftSchulform.class,
		schuljahr: schuljahr,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const schulformVorherigKeinAbschlussManager = new CoreTypeSelectManager({
		clazz: HerkunftSonstige.class,
		schuljahr: schuljahr,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const vorherigerEntlassgrundManager = new SelectManager<KatalogEntlassgrund>({
		options: computed(() => props.manager().entlassgruendeById.values()),
		optionDisplayText: s => s.bezeichnung,
		selectionDisplayText: s => s.bezeichnung,
	});

	const abschlussartAllgemeinbildendVorherigeSchuleManager = new CoreTypeSelectManager({
		clazz: SchulabschlussAllgemeinbildend.class,
		schuljahr: schuljahr,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const abschlussartBerufsbildendVorherigeSchuleManager = new CoreTypeSelectManager({
		clazz: SchulabschlussBerufsbildend.class,
		schuljahr: schuljahr,
		optionDisplayText: "text",
		selectionDisplayText: "text",
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

	const schulwechselGrundSelectManager = new SelectManager({
		options: computed(() => props.manager().entlassgruendeById.values()),
		optionDisplayText: (option) => option.bezeichnung,
		selectionDisplayText: (option) => option.bezeichnung,
	});

	function bezeichnungSchule(s: SchulEintrag) {
		return `${s.schulnummerStatistik}: ${s.name}`;
	}

</script>
