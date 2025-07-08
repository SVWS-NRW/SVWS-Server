<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Fahrschülerart anlegen">
			<svws-ui-input-wrapper :grid="1">
				<svws-ui-text-input placeholder="Bezeichnung" :max-len="30" :min-len="1" v-model="data.bezeichnung" :disabled :valid="fieldIsValid('bezeichnung')" />
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung" v-model="data.sortierung" :disabled :min="0" :max="32000" />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="data.istSichtbar" :disabled>
						Sichtbar
					</svws-ui-checkbox>
					<div class="mt-7 flex flex-row gap-4 justify end">
						<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
						<svws-ui-button @click="add" :disabled="!formIsValid || !hatKompetenzAdd">Speichern</svws-ui-button>
					</div>
				</svws-ui-input-wrapper>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import type { FahrschuelerartenNeuProps } from "~/components/schule/kataloge/fahrschuelerarten/SFahrschuelerartenNeuProps";
	import { BenutzerKompetenz, JavaString, Fahrschuelerart } from "@core";
	import { computed, ref, watch } from "vue";

	const props = defineProps<FahrschuelerartenNeuProps>();
	const data = ref<Fahrschuelerart>(Object.assign( new Fahrschuelerart(), {istSichtbar: true, sortierung: 1}));
	const isLoading = ref<boolean>(false);
	const hatKompetenzAdd = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const disabled = computed(() => !hatKompetenzAdd.value);

	function fieldIsValid(field: keyof Fahrschuelerart | null) : (v: string | null) => boolean {
		return (v: string | null) => {
			switch (field) {
				case 'text':
					return bezeichnungIsValid(data.value.bezeichnung);
				default:
					return true;
			}
		}
	}

	const formIsValid = computed(() => {
		// alle Felder auf validity prüfen
		return Object.keys(data.value).every(field => {
			const validateField = fieldIsValid(field as keyof Fahrschuelerart);
			const fieldValue = data.value[field as keyof Fahrschuelerart] as string | null;
			return validateField(fieldValue);
		})
	});

	function bezeichnungIsValid(v: string | null) {
		if ((v === null) || JavaString.isBlank(v) || (v.length > 30))
			return false;
		for (const fahrschuelerart of props.manager().liste.list()) {
			if (JavaString.equalsIgnoreCase(v, fahrschuelerart.bezeichnung))
				return false;
		}
		return true;
	}

	async function add() {
		if (isLoading.value)
			return;

		props.checkpoint.active = false;
		isLoading.value = true;
		const { id, ...partialData } = data.value;
		await props.addFahrschuelerart(partialData);
		isLoading.value = false;
	}

	async function cancel() {
		props.checkpoint.active = false;
		await props.goToDefaultView(null);
	}

	watch(() => data.value, async() => {
		if (isLoading.value)
			return;
		props.checkpoint.active = true;
	}, {immediate: false, deep: true});

</script>
