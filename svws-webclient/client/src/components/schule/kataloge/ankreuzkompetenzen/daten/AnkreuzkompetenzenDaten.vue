<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-textarea-input placeholder="Kompetenzbeschreibung" class="contentFocusField"
					v-model="model.proxy.floskelText"
					@change="patchFloskeltext"
					:validation="() => model.getFehler('floskelText')"
					:max-len="255" :readonly="!hatKompetenzUpdate" required />
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input v-if="model.istASV.value"
						placeholder="Fach"
						model-value="ASV"
						readonly />
					<ui-select v-else
						label="Fach"
						v-model="model.fach.value"
						:validation="() => model.getFehler('idFach')"
						:manager="faecherSelectManager"
						:removable="false" required />
					<svws-ui-checkbox class="my-auto"
						v-model="model.istASV.value"
						:readonly="!hatKompetenzUpdate">
						ASV
					</svws-ui-checkbox>
					<ui-select label="Schulgliederung"
						v-model="model.schulgliederung.value"
						:manager="schulgliederungSelectManager"
						:readonly="!hatKompetenzUpdate" />
					<ui-select label="Abschnitt"
						v-model="model.abschnitt.value"
						:manager="abschnittSelectManager"
						:readonly="!hatKompetenzUpdate" :removable="false" required />
					<svws-ui-checkbox v-model="model.proxy.istAktiv"
						:validation="() => model.getFehler('istAktiv')"
						@change="model.patch"
						:readonly="!hatKompetenzUpdate">
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
						@change="model.patch"
						:min="0" :max="32000"
						:readonly="!hatKompetenzUpdate"
						:removeable="false" required />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="model.proxy.istSichtbar" :readonly="!hatKompetenzUpdate">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-content-card>
		<svws-ui-content-card title="Zugeordnete Jahrgänge">
			<svws-ui-table :columns
				:items="manager().getJahrgaengeByAuswahl()"
				v-model="jahrgaengeToBeDeleted"
				:selectable="hatKompetenzUpdate" count scroll>
				<template #actions v-if="hatKompetenzUpdate">
					<div class="inline-flex gap-4">
						<svws-ui-button title="Jahrgang löschen" type="trash"
							@click="deleteSelectedJahrgaenge"
							:disabled="jahrgaengeToBeDeleted.length === 0" />
						<svws-ui-button title="Jahrgang hinzufügen" type="icon"
							@click="openModal"
							:disabled="addableJahrgaenge.length === 0">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
					</div>
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
		<svws-ui-modal :show="modalIsOpen"
			@update:show="closeModal"
			:auto-close="false">
			<template #modalTitle>Jahrgänge hinzufügen</template>
			<template #modalContent>
				<svws-ui-table class="max-h-[400px]"
					:items="addableJahrgaenge" :columns
					v-model="jahrgaengeToBeAdded"
					selectable scroll>
					<template #actions v-if="hatKompetenzUpdate">
						<div class="inline-flex gap-4">
							<div class="mt-7 flex flex-row gap-4 justify end">
								<svws-ui-button type="secondary"
									@click="closeModal">
									Abbrechen
								</svws-ui-button>
								<svws-ui-button @click="addJahrgaenge"
									:disabled="jahrgaengeToBeAdded.length === 0">
									Speichern
								</svws-ui-button>
							</div>
						</div>
					</template>
				</svws-ui-table>
			</template>
		</svws-ui-modal>
	</div>
</template>

<script setup lang="ts">

	import type { AnkreuzkompetenzenDatenProps } from "~/components/schule/kataloge/ankreuzkompetenzen/daten/AnkreuzkompetenzenDatenProps";
	import { computed, ref } from "vue";
	import { AnkreuzkompetenzenModelProxy } from "~/components/schule/kataloge/ankreuzkompetenzen/modelproxy/AnkreuzkompetenzenModelProxy";
	import { Schulgliederung } from "@core/asd/types/schule/Schulgliederung";
	import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { Arrays } from "@core/java/util/Arrays";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import type { DataTableColumn } from "@ui/types";
	import { CoreTypeSelectManager } from "@ui/ui/controls/select/manager/CoreTypeSelectManager";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";

	const props = defineProps<AnkreuzkompetenzenDatenProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();

	const manager = () => props.manager();
	const model = new AnkreuzkompetenzenModelProxy(() => manager().daten(), () => manager().liste.list(), () => manager().faecherById, schuleState.abschnitt.schuljahr, props.patch);
	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN));

	const columns: DataTableColumn[] = [
		{ key: "kuerzel", label: "Jahrgang" },
	];

	const faecherAufZeugnis = computed(() => [...manager().faecherById.values()].filter(f => f.aufZeugnis && f.istSichtbar));
	const faecherSelectManager = new SelectManager({
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

	// --- Jahrgangszuordnungen ---
	const jahrgaengeToBeDeleted = ref<JahrgangsDaten[]>([]);
	const jahrgaengeToBeAdded = ref<JahrgangsDaten[]>([]);
	const addableJahrgaenge = computed(() => manager().getAddableJahrgaenge());
	const jahrgaengezuordnungenByIdJahrgaenge = computed(() => manager().getJahrgaengezuordnungenByIdJahrgang());

	async function addJahrgaenge() {
		if (isLoading.value) {
			return;
		}

		isLoading.value = true;
		if (jahrgaengeToBeAdded.value.length === 0) {
			closeModal();
			return;
		}

		const idsJahrgaenge = Arrays.asList(jahrgaengeToBeAdded.value.map(jahrgang => jahrgang.id));
		await props.addJahrgaengezuordnungen(manager().daten().id, idsJahrgaenge);

		isLoading.value = false;
		closeModal();
	}

	async function deleteSelectedJahrgaenge() {
		if (jahrgaengeToBeDeleted.value.length === 0) {
			return;
		}

		const ids = new ArrayList<number>();
		for (const jahrgang of jahrgaengeToBeDeleted.value) {
			const zuordnung = jahrgaengezuordnungenByIdJahrgaenge.value.get(jahrgang.id);
			if (zuordnung !== null) {
				ids.add(zuordnung.id);
			}
		}

		await props.deleteJahrgaengezuordnungen(ids);
		jahrgaengeToBeDeleted.value = [];
	}

	// --- Modal ---
	const isLoading = ref<boolean>(false);
	const modalIsOpen = ref<boolean>(false);

	function closeModal() {
		jahrgaengeToBeAdded.value = [];
		modalIsOpen.value = false;
	}

	function openModal() {
		jahrgaengeToBeAdded.value = [];
		modalIsOpen.value = true;
	}

	async function patchFloskeltext(v: string | null) {
		model.proxy.floskelText = v ?? '';
		await model.patch();
	}

</script>
