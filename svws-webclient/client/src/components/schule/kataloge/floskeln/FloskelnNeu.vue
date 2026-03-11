<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper>
					<svws-ui-text-input placeholder="Kürzel" class="contentFocusField"
						v-model="data.kuerzel"
						:valid="() => fieldIsValid('kuerzel')"
						:min-len="1" :max-len="10" :disabled required />
					<svws-ui-textarea-input placeholder="Text"
						v-model="data.text"
						@input="value => data.text = value"
						:valid="() => fieldIsValid('text')"
						:disabled required @keydown.enter.prevent />
					<svws-ui-input-wrapper :grid="2">
						<ui-select label="Floskelgruppe"
							v-model="selectedFloskelgruppe"
							:manager="floskelgruppenManager"
							:removable="false" searchable required />
						<ui-select v-if="hatFloskelgruppeArtFach" label="Fach"
							v-model="selectedFach"
							:manager="faecherManager"
							searchable />
						<div v-else />
						<ui-select label="Jahrgang"
							v-model="selectedJahrgang"
							:manager="jahrgaengeManager"
							searchable removable />
						<ui-select label="Niveau"
							v-model="selectedNiveau"
							:manager="niveauManager" />
					</svws-ui-input-wrapper>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<!-- Sortierung -->
			<svws-ui-content-card title="Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="data.sortierung"
						:valid="() => fieldIsValid('sortierung')"
						:min="0" :max="32000" :disabled="!hatKompetenzAdd" :removable="false" />
					<svws-ui-spacing />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
				<svws-ui-button @click="addFloskel" :disabled="!formIsValid">Speichern</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";
	import type { FachDaten, Floskelgruppe, JahrgangsDaten } from "@core";
	import { ArrayList, BenutzerKompetenz, Floskel, Floskelgruppenart } from "@core";
	import { isUniqueInList, mandatoryInputIsValid, numberHasDecimals, numberIsValid } from "~/util/validation/Validation";
	import { SelectManager } from "@ui";
	import type { FloskelnNeuProps } from "./FloskelnNeuProps";

	const props = defineProps<FloskelnNeuProps>();
	const data = ref<Floskel>(Object.assign(new Floskel(), { istSichtbar: true, sortierung: 32000 }));
	const isLoading = ref<boolean>(false);
	const hatKompetenzAdd = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const disabled = computed(() => !hatKompetenzAdd.value);
	const manager = () => props.manager();

	const floskelgruppen = computed(() => manager().floskelgruppenById.values());
	const selectedFloskelgruppe = computed<Floskelgruppe | null>({
		get: (): Floskelgruppe | null => manager().floskelgruppenById.get(data.value.idFloskelgruppe ?? -1) ?? null,
		set: (value: Floskelgruppe | null) => {
			data.value.idFloskelgruppe = value?.id ?? null;
			const istNictFachbezogen = (Floskelgruppenart.data().getWertByIDOrNull(value?.idFloskelgruppenart ?? -1)?.name() !== 'FACH');
			if (istNictFachbezogen) {
				data.value.idFach = null;
			}
		},
	});

	const faecher = computed<FachDaten[]>(() => [...manager().faecherById.values()]);
	const selectedFach = computed<FachDaten | null>({
		get: () => manager().faecherById.get(data.value.idFach ?? -1) ?? null,
		set: (value: FachDaten | null) => data.value.idFach = value?.id ?? null,
	});
	const hatFloskelgruppeArtFach = computed<boolean>(() => {
		const fg = selectedFloskelgruppe.value;
		if (fg === null) {
			return false;
		}
		const eintragByID = Floskelgruppenart.data().getEintragByID(fg.idFloskelgruppenart ?? -1);
		return eintragByID?.schluessel === 'FACH';
	});

	const jahrgaenge = computed<JahrgangsDaten[]>(() => [...manager().jahrgaengeById.values()]);
	const selectedJahrgang = computed<JahrgangsDaten | null | undefined>({
		get: () => {
			const ids = data.value.idsJahrgaenge;
			if (ids === null || ids.isEmpty()) {
				return null;
			}
			return manager().jahrgaengeById.get(ids.get(0)) ?? null;
		},
		set: (value: JahrgangsDaten | null | undefined) => {
			const list = new ArrayList<number>();
			if (value !== null && value !== undefined) {
				list.add(value.id);
			}
			if (list.size() <= 1) {
				data.value.idsJahrgaenge = list;
			}
		},
	});

	const selectedNiveau = computed<number | null>({
		get: () => data.value.niveau,
		set: (value: number | null) => data.value.niveau = value,
	});

	// --- manager ---

	const floskelgruppenManager = new SelectManager({
		options: floskelgruppen,
		optionDisplayText: v => v.bezeichnung,
		selectionDisplayText: v => v.bezeichnung,
	});

	const faecherManager = new SelectManager<FachDaten>({
		options: faecher,
		optionDisplayText: (f: FachDaten) => f.bezeichnung,
		selectionDisplayText: (f: FachDaten) => f.bezeichnung,
	});

	const jahrgaengeManager = new SelectManager<JahrgangsDaten>({
		options: jahrgaenge,
		optionDisplayText: (jg: JahrgangsDaten) => jg.kuerzel ?? '',
		selectionDisplayText: (jg: JahrgangsDaten) => jg.kuerzel ?? '',
	});

	const niveauManager = new SelectManager<number>({
		options: manager().niveaus,
		optionDisplayText: String,
		selectionDisplayText: String,
	});


	// ---  validate  ---
	const fieldIsValid = (field: keyof Floskel): boolean => {
		switch (field) {
			case 'kuerzel':
				return kuerzelIsValid(data.value.kuerzel);
			case 'text':
				return textIsValid(data.value.text);
			case 'idFloskelgruppe':
				return (data.value.idFloskelgruppe !== null);
			case 'sortierung':
				return sortierungIsValid(data.value.sortierung);
			default:
				return true;
		}
	};

	const formIsValid = computed(() => {
		// alle Felder auf validity prüfen
		return Object.keys(data.value)
			.every((field: string) => fieldIsValid(field as keyof Floskel));
	});

	function kuerzelIsValid(value: string | null): boolean {
		return (mandatoryInputIsValid(value, 10) &&
			isUniqueInList(value, manager().liste.list(), "kuerzel"));
	}

	function textIsValid(value: string | null): boolean {
		return (value !== null) && (value !== "");
	}

	function sortierungIsValid(value: number | null): value is number {
		return !numberHasDecimals(value) && numberIsValid(value, true, 0, 32000);
	}

	// --- util ---

	async function addFloskel() {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = false;
		isLoading.value = true;
		const { id, ...partialData } = data.value;
		await props.add(partialData);
		isLoading.value = false;
	}

	async function cancel() {
		props.checkpoint.active = false;
		await props.goToDefaultView(null);
	}

	watch(() => data.value, async () => {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

</script>
