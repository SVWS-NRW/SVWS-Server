<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-input-wrapper :grid="1">
				<div class="pb-4 flex flex-row gap-6 items-center">
					<svws-ui-radio-option label=" Öffentliche oder Ersatz-Schule in NRW "
						v-model="isInternal"
						:value="true" :disabled="!hatKompetenzAdd" />
					<svws-ui-radio-option label=" Sonstige Schule "
						v-model="isInternal"
						:value="false" :disabled="!hatKompetenzAdd" />
				</div>
				<svws-ui-tooltip v-if="!isInternal" color="primary" :show-arrow="false" :indicator="false">
					<template #content>
						Schule außerhalb NRW und sonstige Herkünfte z.B. auch nicht staatl. anerkannte Schule.
					</template>
					<ui-select label="Schule außerhalb von NRW oder sonstige Schule in NRW" class="pb-4 w-full"
						:manager="externeSchulenSelectManager"
						v-model="selectedExterneSchulen"
						:validation="() => model.getFehler('schulnummerStatistik')"
						:disabled="!hatKompetenzAdd" required :removable="false" />
				</svws-ui-tooltip>
				<ui-select v-if="isInternal"
					label="Öffentliche oder Ersatz-Schule in NRW" class="pb-4 w-full"
					:manager="schulenNRWSelectManager"
					v-model="selectedSchule"
					:validation="() => model.getFehler('schulnummerStatistik')"
					:disabled="isLoading || !hatKompetenzAdd" required :removable="false" />
				<div v-if="!schuleAlreadyCreated">
					<svws-ui-content-card title="Schulangaben" />
					<svws-ui-input-wrapper :grid="2">
						<svws-ui-text-input placeholder="Schulform" v-if="isInternal"
							:model-value="model.schulformInternal.value"
							readonly />
						<ui-select label="Schulform" v-else-if="!isInternal"
							:manager="schulformenExternSelectManager"
							v-model="model.selectedSchulformSonstigeSchule.value"
							:disabled="!hatKompetenzAdd" />
						<svws-ui-text-input placeholder="Statistik-Schulnummer"
							:model-value="model.proxy.schulnummerStatistik"
							:disabled="!hatKompetenzAdd" readonly />
						<svws-ui-text-input placeholder="Kürzel"
							v-model="model.proxy.kuerzel"
							:validation="() => model.getFehler('kuerzel')"
							:max-len="10" :disabled="!hatKompetenzAdd" />
						<svws-ui-text-input placeholder="Schulname"
							v-model="model.proxy.name"
							:validation="() => model.getFehler('name')"
							:max-len="120" :disabled="!hatKompetenzAdd" required />
						<svws-ui-text-input placeholder="Kurzbezeichnung"
							v-model="model.proxy.kurzbezeichnung"
							:validation="() => model.getFehler('kurzbezeichnung')"
							:max-len="40" :disabled="!hatKompetenzAdd" required />
						<svws-ui-text-input placeholder="Schulleitung"
							v-model="model.proxy.schulleiter"
							:validation="() => model.getFehler('schulleiter')"
							:max-len="40" :disabled="!hatKompetenzAdd" />
						<svws-ui-text-input placeholder="Straße"
							v-model="model.adresse.value"
							:validation="() => model.getFehler('strassenname')"
							:max-len="55" :disabled="!hatKompetenzAdd" />
						<svws-ui-text-input placeholder="PLZ"
							v-model="model.proxy.plz"
							:validation="() => model.getFehler('plz')"
							:max-len="10" :disabled="!hatKompetenzAdd" />
						<svws-ui-text-input placeholder="Ort"
							v-model="model.proxy.ort"
							:validation="() => model.getFehler('ort')"
							:max-len="50" :disabled="!hatKompetenzAdd" />
						<svws-ui-text-input placeholder="Telefon" type="tel"
							v-model="model.proxy.telefon"
							:validation="() => model.getFehler('telefon')"
							:max-len="20" :disabled="!hatKompetenzAdd" />
						<svws-ui-text-input placeholder="Fax" type="tel"
							v-model="model.proxy.fax"
							:validation="() => model.getFehler('fax')"
							:max-len="20" :disabled="!hatKompetenzAdd" />
						<svws-ui-text-input placeholder="E-Mail-Adresse" type="email"
							v-model="model.proxy.email"
							:validation="() => model.getFehler('email')"
							:max-len="40" :disabled="!hatKompetenzAdd" />
					</svws-ui-input-wrapper>
					<svws-ui-spacing :size="2" />
					<svws-ui-content-card title="Ansicht & Sortierung">
						<svws-ui-input-wrapper :grid="2">
							<svws-ui-input-number placeholder="Sortierung"
								v-model="model.proxy.sortierung"
								:validation="() => model.getFehler('sortierung')"
								:min="0" :max="32000" :disabled="schuleAlreadyCreated || !hatKompetenzAdd" :removable="false" required />
							<svws-ui-spacing />
							<svws-ui-checkbox v-model="model.proxy.istSichtbar" :disabled="schuleAlreadyCreated || !hatKompetenzAdd">
								Sichtbar
							</svws-ui-checkbox>
						</svws-ui-input-wrapper>
					</svws-ui-content-card>
				</div>
				<div v-else-if="schuleAlreadyCreated">
					<p class="pb-4">Diese Schule wurde bereits angelegt:</p>
					<svws-ui-button @click="navigateToSelectedSchule"> Zur Schule </svws-ui-button>
				</div>
			</svws-ui-input-wrapper>
			<div v-if="!schuleAlreadyCreated" class="mt-7 flex flex-row gap-4 justify-end w-full">
				<svws-ui-button type="secondary" @click="cancel">
					Abbrechen
				</svws-ui-button>
				<svws-ui-button @click="addSchule" :disabled="!isValid || !hatKompetenzAdd">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">
	import { computed, ref, watch } from "vue";
	import { JavaObject, SchulEintrag, Schulform, Herkunftsschulnummer, BenutzerKompetenz, HerkunftSchulform } from "@core";
	import type { HerkunftsschulnummerKatalogEintrag, SchulenKatalogEintrag } from "@core";
	import type { SchulenNeuProps } from "./SchulenNeuProps";
	import { CoreTypeSelectManager, SelectManager, useBenutzerState, useSchuleState } from "@ui";
	import { SchuleModelProxy } from "~/components/schule/kataloge/schulen/modelproxy/SchuleModelProxy";

	const props = defineProps<SchulenNeuProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();

	const initialData = ref<SchulEintrag>(Object.assign(new SchulEintrag(), { sortierung: 32000, istSichtbar: true }));
	const model = new SchuleModelProxy(() => initialData.value, () => props.manager().liste.list());
	const isLoading = ref<boolean>(false);
	const hatKompetenzAdd = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const isInternal = ref<boolean>(true);
	const selectedSchulenKatalogEintrag = ref<SchulenKatalogEintrag>();
	const schuljahr = computed<number>(() => schuleState.schuljahr);

	const selectedExterneSchulen = computed<HerkunftsschulnummerKatalogEintrag | null>({
		get: () => Herkunftsschulnummer.data().getEintragBySchuljahrUndSchluessel(schuljahr.value, model.proxy.schulnummerStatistik ?? ""),
		set: (value) => model.proxy.schulnummerStatistik = value?.schluessel ?? null,
	});

	const externeSchulenSelectManager = new CoreTypeSelectManager({
		clazz: Herkunftsschulnummer.class,
		schuljahr: schuljahr.value,
		schulformen: schuleState.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const schulenNRWSelectManager = new SelectManager<SchulenKatalogEintrag>({
		options: computed(() => props.manager().schulenKatalogEintraege),
		optionDisplayText: (s) => schulenKatalogEintragText(s),
		selectionDisplayText: (s) => schulenKatalogEintragText(s),
	});

	const schulformenExternSelectManager = new CoreTypeSelectManager({
		clazz: HerkunftSchulform.class,
		schuljahr: schuleState.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const selectedSchule = computed<SchulenKatalogEintrag | null>({
		get: () => selectedSchulenKatalogEintrag.value ?? null,
		set: (value) => {
			selectedSchulenKatalogEintrag.value = value ?? undefined;
			updateData(value);
		},
	});

	const isValid = computed<boolean>(() => model.getAlleFehler().isEmpty() && model.proxy.schulnummerStatistik !== null);

	function getSchulname(schule: SchulenKatalogEintrag) {
		const bez1 = (schule.ABez1 ?? '').trim();
		const bez2 = (schule.ABez2 ?? '').trim();
		const bez3 = (schule.ABez3 ?? '').trim();
		return (bez1 + ' ' + bez2 + ' ' + bez3).trim();
	}

	// befüllt das Formular mit den Werten der vorausgewählten Schule
	function updateData(schule: SchulenKatalogEintrag | undefined | null) {
		if (schule === undefined || schule === null) {
			resetForm();
			return;
		}

		model.proxy.kurzbezeichnung = schule.KurzBez;
		model.proxy.schulnummerStatistik = schule.SchulNr;
		model.proxy.name = getSchulname(schule);
		model.proxy.idSchulform = Schulform.data().getEintragBySchuljahrUndSchluessel(schuljahr.value, schule.SF ?? "")?.id ?? null;
		model.adresse.value = schule.Strasse;
		model.proxy.plz = schule.PLZ;
		model.proxy.ort = schule.Ort;
		model.proxy.telefon = schule.Telefon;
		model.proxy.fax = schule.Fax;
		model.proxy.email = schule.Email;
	}

	// ---Bezeichnungen---

	function schulenKatalogEintragText(i: SchulenKatalogEintrag) {
		if (i.KurzBez === null) {
			return `${i.SchulNr}: Schule ohne Name`;
		}
		return `${i.SchulNr}: ${i.KurzBez}`;
	}

	// ---buttons---

	async function addSchule() {
		if (isLoading.value) {
			return;
		}

		isLoading.value = true;
		props.checkpoint.active = false;
		const { id, referenziertInAnderenTabellen, schulnummerIntern, ...partialData } = model.proxy;
		await props.add(partialData);
		isLoading.value = false;
	}

	async function cancel() {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	// ---util---

	const schuleAlreadyCreated = computed(() => findSchuleByPredicate(
		(schuleintrag: SchulEintrag) => JavaObject.equalsTranspiler(schuleintrag.schulnummerStatistik, selectedSchule.value?.SchulNr)) !== null
	);

	function resetForm() {
		initialData.value = Object.assign(new SchulEintrag(), { sortierung: 32000, istSichtbar: true });
		selectedSchulenKatalogEintrag.value = undefined;
	}

	async function navigateToSelectedSchule() {
		props.checkpoint.active = false;
		const schuleintrag = findSchuleByPredicate((schuleintrag: SchulEintrag) =>
			JavaObject.equalsTranspiler(schuleintrag.schulnummerStatistik, selectedSchule.value?.SchulNr));
		if (schuleintrag) {
			await props.gotoDefaultView(schuleintrag.id);
		}
	}

	function findSchuleByPredicate(predicate: (schuleintrag: any) => boolean) {
		for (const schuleintrag of props.manager().liste.list()) {
			if (predicate(schuleintrag)) {
				return schuleintrag;
			}
		}
		return null;
	}

	watch(() => model.proxy, async () => {
		if (isLoading.value) {
			return;
		}
		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

	watch(() => isInternal.value, () => {
		resetForm();
	});

</script>
