<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Beschäftigungsart anlegen">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Bezeichnung" :min-len="1" :max-len="100" v-model="data.bezeichnung" required :disabled
					:valid="fieldIsValid('bezeichnung')" />
				<svws-ui-input-number placeholder="Sortierung" v-model="data.sortierung" :disabled="!bezeichnungIsValid || !hatKompetenzAdd" :min="0" :max="32000" />
				<svws-ui-spacing />
				<svws-ui-checkbox v-model="data.istSichtbar" :disabled="!bezeichnungIsValid || !hatKompetenzAdd">
					Sichtbar
				</svws-ui-checkbox>
				<div class="mt-7 flex flex-row gap-4 justify end">
					<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
					<svws-ui-button @click="add" :disabled="!formIsValid || !hatKompetenzAdd">Speichern</svws-ui-button>
				</div>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import type { BeschaeftigungsartenNeuProps } from "~/components/schule/kataloge/beschaeftigungsarten/SBeschaeftigungsartenNeuProps";
	import { BenutzerKompetenz, Beschaeftigungsart, JavaString } from "@core";
	import { computed, ref, watch } from "vue";

	const props = defineProps<BeschaeftigungsartenNeuProps>();
	const data = ref<Beschaeftigungsart>(new Beschaeftigungsart());
	const isLoading = ref<boolean>(false);
	const hatKompetenzAdd = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const disabled = computed(() => !hatKompetenzAdd.value);

	function fieldIsValid(field: keyof Beschaeftigungsart | null) : (v: string | null) => boolean {
		return (v: string | null) => {
			switch (field) {
				case 'bezeichnung':
					return bezeichnungIsValid(data.value.bezeichnung);
				default:
					return true;
			}
		}
	}

	function bezeichnungIsValid(v: string | null) {
		if ((v === null) || JavaString.isBlank(v) || (v.length > 100))
			return false;

		for (const beschaeftigungsart of props.manager().liste.list()) {
			if (JavaString.equalsIgnoreCase(v, beschaeftigungsart.bezeichnung))
				return false;
		}

		return true;
	}

	const formIsValid = computed(() => {
		// alle Felder auf validity prüfen
		return Object.keys(data.value).every(field => {
			const validateField = fieldIsValid(field as keyof Beschaeftigungsart);
			const fieldValue = data.value[field as keyof Beschaeftigungsart] as string | null;
			return validateField(fieldValue);
		})
	})

	async function add() {
		if (isLoading.value)
			return;

		props.checkpoint.active = false;
		isLoading.value = true;
		const { id, ...partialData } = data.value;
		await props.addBeschaeftigungsart(partialData);
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
