<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Entlassgrund anlegen">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Bezeichnung" :min-len="1" :max-len="30" v-model="data.bezeichnung" required :disabled
					:valid="fieldIsValid('bezeichnung')" />
				<svws-ui-input-number placeholder="Sortierung" v-model="data.sortierung" :disabled="!bezeichnungIsValid || !hatKompetenzAdd" />
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

	import type { EntlassgruendeNeuProps } from "~/components/schule/kataloge/entlassgruende/SEntlassgruendeNeuProps";
	import { BenutzerKompetenz, JavaString, KatalogEntlassgrund} from "@core";
	import { ref, computed, watch } from "vue";

	const props = defineProps<EntlassgruendeNeuProps>();
	const data = ref<KatalogEntlassgrund>(new KatalogEntlassgrund());
	const isLoading = ref<boolean>(false);
	const hatKompetenzAdd = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const disabled = computed(() => !hatKompetenzAdd.value);

	function fieldIsValid(field: keyof KatalogEntlassgrund | null) : (v: string | null) => boolean {
		return (v: string | null) => {
			switch (field) {
				case 'bezeichnung':
					return bezeichnungIsValid();
				default:
					return true;
			}
		}
	}

	function bezeichnungIsValid() {
		return (!JavaString.isBlank(data.value.bezeichnung)) && (data.value.bezeichnung.length <= 30);
	}

	const formIsValid = computed(() => {
		// alle Felder auf validity prüfen
		return Object.keys(data.value).every(field => {
			const validateField = fieldIsValid(field as keyof KatalogEntlassgrund);
			const fieldValue = data.value[field as keyof KatalogEntlassgrund] as string | null;
			return validateField(fieldValue);
		})
	})

	async function add() {
		if (isLoading.value)
			return;

		props.checkpoint.active = false;
		isLoading.value = true;
		const { id, ...partialData } = data.value;
		await props.addEntlassgrund(partialData);
		isLoading.value = false;
	}

	function cancel() {
		props.checkpoint.active = false;
		void props.goToDefaultView(null);
	}

	watch(() => data.value, async() => {
		if (isLoading.value)
			return;
		props.checkpoint.active = true;
	}, {immediate: false, deep: true});

</script>
