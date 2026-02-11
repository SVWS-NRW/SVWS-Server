<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<ui-select label="Konfession ASD-Kürzel"
						v-model="selectedKonfession"
						:manager="konfessionKuerzelSelectManager"
						:valid="fieldIsValid('kuerzel')" searchable statistics :disabled="!hatKompetenzUpdate" required :removable="false" />
					<ui-select label="Konfession ASD-Text"
						v-model="selectedKonfession"
						:manager="konfessionTextSelectManager"
						:valid="fieldIsValid('kuerzel')" searchable statistics :disabled="!hatKompetenzUpdate" required :removable="false" />
					<svws-ui-text-input placeholder="Interne Bezeichnung"
						v-model="data.bezeichnung"
						:valid="() => fieldIsValid('bezeichnung')" :min-len="1" :max-len="30" :disabled="!hatKompetenzUpdate" required />
					<svws-ui-text-input placeholder="Zeugnisbezeichnung"
						v-model="data.bezeichnungZeugnis"
						:valid="() => fieldIsValid('bezeichnungZeugnis')" :max-len="50" :disabled="!hatKompetenzUpdate" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="data.sortierung"
						:valid="() => fieldIsValid('sortierung')" :min="0" :max="32000" :disabled="!hatKompetenzUpdate" :removable="false" />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="data.istSichtbar" :disabled="!hatKompetenzUpdate">
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
	import type { CoreTypeData } from "@core";
	import { BenutzerKompetenz, Religion, ReligionEintrag } from "@core";
	import { isUniqueInList, mandatoryInputIsValid, numberHasDecimals, numberIsValid, optionalInputIsValid } from "~/util/validation/Validation";
	import { CoreTypeSelectManager } from "@ui";

	const props = defineProps<KonfessionenNeuProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const schuljahr = computed<number>(() => props.manager().getSchuljahr());
	const isLoading = ref<boolean>(false);
	const data = ref<ReligionEintrag>(Object.assign(new ReligionEintrag(), { istSichtbar: true, sortierung: 32000 }));

	const konfessionKuerzelSelectManager = new CoreTypeSelectManager({
		clazz: Religion.class,
		schuljahr: schuljahr.value,
		schulformen: props.schulform,
		optionDisplayText: "kuerzel",
		selectionDisplayText: "kuerzel",
	});

	const konfessionTextSelectManager = new CoreTypeSelectManager({
		clazz: Religion.class,
		schuljahr: schuljahr.value,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const selectedKonfession = computed<CoreTypeData | null>({
		get: () => Religion.data().getEintragBySchuljahrUndSchluessel(schuljahr.value, data.value.kuerzel ?? ""),
		set: (value: CoreTypeData | null) => data.value.kuerzel = value?.schluessel ?? null,
	});

	// ---validate---

	const formIsValid = computed(() => {
		return Object.keys(data.value)
			.every((field: string) => fieldIsValid(field as keyof ReligionEintrag));
	});

	const fieldIsValid = (field: keyof ReligionEintrag): boolean => {
		switch (field) {
			case 'kuerzel':
				return (data.value.kuerzel !== null) && (data.value.kuerzel !== "");
			case 'bezeichnung':
				return bezeichnungIsValid();
			case 'bezeichnungZeugnis':
				return bezeichnungZeugnisIsValid();
			case 'sortierung':
				return sortierungIsValid(data.value.sortierung);
			default:
				return true;
		}
	};

	function bezeichnungIsValid() {
		return mandatoryInputIsValid(data.value.bezeichnung, 30)
			&& isUniqueInList(data.value.bezeichnung, props.manager().liste.list(), 'bezeichnung');
	}

	function bezeichnungZeugnisIsValid() {
		return optionalInputIsValid(data.value.bezeichnungZeugnis, 50);
	}

	function sortierungIsValid(sortierung: number): boolean {
		return !numberHasDecimals(sortierung)
			&& numberIsValid(sortierung, true, 0, 32000);
	}

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
		const { id, referenziertInAnderenTabellen, ...partialData } = data.value;
		await props.add(partialData);
		isLoading.value = false;
	}

	watch(() => data.value, async () => {
		if (isLoading.value) {
			return;
		}
		props.checkpoint.active = true;
	}, { immediate: false, deep: true });


</script>
