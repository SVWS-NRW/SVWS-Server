<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<ui-select label="Konfession ASD-Kürzel"
						v-model="model.selectedKonfession.value"
						:manager="konfessionKuerzelSelectManager"
						:validation="() => model.getFehler('idReligion')"
						searchable statistics :disabled="!hatKompetenzUpdate" required :removable="false" />
					<ui-select label="Konfession ASD-Text"
						v-model="model.selectedKonfession.value"
						:manager="konfessionTextSelectManager"
						:validation="() => model.getFehler('idReligion')"
						searchable statistics :disabled="!hatKompetenzUpdate" required :removable="false" />
					<svws-ui-text-input placeholder="Interne Bezeichnung"
						v-model="model.proxy.bezeichnung"
						:validation="() => model.getFehler('bezeichnung')"
						:max-len="30" :disabled="!hatKompetenzUpdate" required />
					<svws-ui-text-input placeholder="Zeugnisbezeichnung"
						v-model="model.proxy.bezeichnungZeugnis"
						:validation="() => model.getFehler('bezeichnungZeugnis')"
						:max-len="50" :disabled="!hatKompetenzUpdate" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="model.proxy.sortierung"
						:validation="() => model.getFehler('sortierung')"
						:min="0" :max="32000" :disabled="!hatKompetenzUpdate"
						:removable="false" required />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="model.proxy.istSichtbar" :disabled="!hatKompetenzUpdate">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">
					Abbrechen
				</svws-ui-button>
				<svws-ui-button @click="addKonfession" :disabled="!hatKompetenzUpdate || !formIsValid">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">
	import type { KonfessionenNeuProps } from "./KonfessionenNeuProps";
	import { computed, ref, watch } from "vue";
	import { BenutzerKompetenz, Religion, ReligionEintrag } from "@core";
	import { CoreTypeSelectManager, useBenutzerState, useSchuleState } from "@ui";
	import { KonfessionModelProxy } from "~/components/schule/kataloge/konfessionen/modelproxy/KonfessionModelProxy";

	const props = defineProps<KonfessionenNeuProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();

	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const isLoading = ref<boolean>(false);
	const initialData = ref<ReligionEintrag>(Object.assign(new ReligionEintrag(), { istSichtbar: true, sortierung: 32000 }));
	const model = new KonfessionModelProxy(() => initialData.value, () => props.manager().liste.list());

	const formIsValid = computed(() => model.getAlleFehler().isEmpty());

	const konfessionKuerzelSelectManager = new CoreTypeSelectManager({
		clazz: Religion.class,
		schuljahr: schuleState.abschnitt.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "kuerzel",
		selectionDisplayText: "kuerzel",
	});

	const konfessionTextSelectManager = new CoreTypeSelectManager({
		clazz: Religion.class,
		schuljahr: schuleState.abschnitt.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	// --- util ---

	async function cancel() {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	async function addKonfession() {
		if (isLoading.value) {
			return;
		}
		isLoading.value = true;
		props.checkpoint.active = false;
		const { id, referenziertInAnderenTabellen, ...partialData } = model.proxy;
		await props.add(partialData);
		isLoading.value = false;
	}

	watch(() => model.proxy, async () => {
		if (isLoading.value) {
			return;
		}
		props.checkpoint.active = true;
	}, { immediate: false, deep: true });
</script>
