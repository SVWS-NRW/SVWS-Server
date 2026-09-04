<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-textarea-input placeholder="Kompetenzbeschreibung" class="contentFocusField"
					v-model="model.proxy.floskelText"
					:validation="() => model.getFehler('floskelText')"
					:max-len="255" :disabled required />
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input v-if="model.istASV.value" placeholder="Fach"
						model-value="ASV"
						readonly />
					<ui-select v-else label="Fach"
						:manager="faecherManager"
						v-model="model.fach.value"
						:validation="() => model.getFehler('idFach')"
						:disabled required />
					<svws-ui-checkbox class="my-auto"
						v-model="model.istASV.value"
						:validation="() => model.getFehler('istASV')"
						:disabled>
						ASV
					</svws-ui-checkbox>
					<ui-select label="Schulgliederung"
						v-model="model.schulgliederung.value"
						:manager="schulgliederungSelectManager"
						:disabled />
					<ui-select label="Abschnitt"
						v-model="model.abschnitt.value"
						:manager="abschnittSelectManager"
						:removable="false" :disabled required />
					<svws-ui-checkbox v-model="model.proxy.istAktiv"
						:disabled>
						Aktiv
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="model.proxy.sortierung"
						:validation="() => model.getFehler('sortierung')"
						:min="0" :max="32000"
						:disabled
						:removeable="false" required />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="model.proxy.istSichtbar"
						:validation="() => model.getFehler('istSichtbar')">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">
					Abbrechen
				</svws-ui-button>
				<svws-ui-button @click="add" :disabled="!isValid">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-content-card title="Jahrgänge zuordnen">
			<svws-ui-table :columns
				:items="manager().jahrgaengeById.values()"
				v-model="jahrgaengeToBeAdded"
				:selectable="hatKompetenzAdd" count scroll />
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import { Schulgliederung } from "@core/asd/types/schule/Schulgliederung";
	import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
	import { Ankreuzkompetenz } from "@core/core/data/schule/Ankreuzkompetenz";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { Arrays } from "@core/java/util/Arrays";
	import type { List } from "@core/java/util/List";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import type { DataTableColumn } from "@ui/types";
	import { CoreTypeSelectManager } from "@ui/ui/controls/select/manager/CoreTypeSelectManager";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";
	import { computed, ref, watch } from "vue";
	import type { AnkreuzkompetenzenNeuProps } from "~/components/schule/kataloge/ankreuzkompetenzen/AnkreuzkompetenzenNeuProps";
	import { AnkreuzkompetenzenModelProxy } from "~/components/schule/kataloge/ankreuzkompetenzen/modelproxy/AnkreuzkompetenzenModelProxy";

	const props = defineProps<AnkreuzkompetenzenNeuProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();

	const data = ref<Ankreuzkompetenz>(Object.assign(new Ankreuzkompetenz(), { istSichtbar: true, sortierung: 32000 }));
	const model = new AnkreuzkompetenzenModelProxy(() => data.value, () => props.manager().liste.list(), () => props.manager().faecherById, schuleState.abschnitt.schuljahr);
	const isLoading = ref<boolean>(false);
	const hatKompetenzAdd = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const disabled = computed<boolean>(() => !hatKompetenzAdd.value);
	const jahrgaengeToBeAdded = ref<JahrgangsDaten[]>([]);
	const jahrgaengeIdsToBeAdded = computed<List<number>>(() => Arrays.asList(jahrgaengeToBeAdded.value.map(jahrgang => jahrgang.id)));

	const columns: DataTableColumn[] = [
		{ key: "kuerzel", label: "Jahrgang" },
	];

	const faecherAufZeugnis = computed(() => [...props.manager().faecherById.values()].filter((f => f.aufZeugnis && f.istSichtbar)));
	const faecherManager = new SelectManager({
		options: faecherAufZeugnis,
		optionDisplayText: f => f.bezeichnung,
		selectionDisplayText: f => f.bezeichnung,
	});

	const schulgliederungSelectManager = new CoreTypeSelectManager({
		clazz: Schulgliederung.class,
		schuljahr: schuleState.abschnitt.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const abschnittSelectManager = new SelectManager({
		options: AnkreuzkompetenzenModelProxy.abschnittOptionen,
		optionDisplayText: a => a.text,
		selectionDisplayText: a => a.text,
	});

	const isValid = computed<boolean>(() => model.getAlleFehler().isEmpty());

	// --- util ---
	async function add() {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = false;
		isLoading.value = true;
		const { id, jahrgaengezuordnung, referenziertInAnderenTabellen, ...partialData } = model.proxy;
		const ankreuzkompetenz = await props.addAnkreuzkompetenz(partialData, jahrgaengeIdsToBeAdded.value);

		await props.gotoDefaultView(ankreuzkompetenz.id);

		isLoading.value = false;
	}

	async function cancel() {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	watch(() => data.value, async () => {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

</script>
