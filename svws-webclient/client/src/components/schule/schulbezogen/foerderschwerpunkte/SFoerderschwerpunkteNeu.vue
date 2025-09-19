<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper>
				<svws-ui-select label="ASD-Statistik-Förderschwerpunkt" span="full" :items="props.manager().getAvailableFoerderschwerpunkte()" v-model="selectedFoerderschwerpunkt"
					:valid="fieldIsValid('kuerzelStatistik')" :item-text="v => v.daten(props.manager().getSchuljahr())?.text?? ''" statistics :disabled />
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Kürzel" :min-len="1" :max-len="50" v-model="data.kuerzel" :disabled :valid="fieldIsValid('kuerzel')" />
					<svws-ui-text-input placeholder="Statistik-Kürzel" readonly :model-value="data.kuerzelStatistik" statistics />
					<div class="flex flex-col my-auto space-y-1">
						<div v-if="!isUniqueInList(data.kuerzel, props.manager().liste.list(), 'kuerzel')" class="flex items-center">
							<span class="icon i-ri-alert-line mx-0.5 mr-1" />
							<p>Dieses Kürzel wird bereits verwendet.</p>
						</div>
						<div v-if="data.kuerzel.length > 50" class="flex items-center">
							<span class="icon i-ri-alert-line mx-0.5 mr-1" />
							<p>Dieses Kürzel verwendet zu viele Zeichen.</p>
						</div>
					</div>
					<div />
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

	import { computed, ref, watch} from "vue";
	import type { FoerderschwerpunkteNeuProps } from "~/components/schule/schulbezogen/foerderschwerpunkte/SFoerderschwerpunkteNeuProps";
	import { BenutzerKompetenz, Foerderschwerpunkt, FoerderschwerpunktEintrag, JavaString } from "@core";
	import { isUniqueInList, mandatoryInputIsValid } from "~/util/validation/Validation";

	const props = defineProps<FoerderschwerpunkteNeuProps>();
	const data = ref<FoerderschwerpunktEintrag>(Object.assign( new FoerderschwerpunktEintrag(), { istSichtbar: true }));
	const isLoading = ref<boolean>(false);
	const hatKompetenzAdd = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const disabled = computed(() => !hatKompetenzAdd.value);

	const selectedFoerderschwerpunkt = computed({
		get: () => Foerderschwerpunkt.data().getWertBySchluessel(data.value.kuerzelStatistik),
		set: (v: Foerderschwerpunkt | null) => data.value.kuerzelStatistik = v?.daten(props.manager().getSchuljahr())?.schluessel ?? "",
	})

	function fieldIsValid(field: keyof FoerderschwerpunktEintrag | null) : (v: string | null) => boolean {
		return (v: string | null) => {
			switch (field) {
				case 'kuerzelStatistik':
					return foerderschwerpunktIsValid();
				case 'kuerzel':
					return kuerzelIsValid(data.value.kuerzel);
				default:
					return true;
			}
		}
	}

	function foerderschwerpunktIsValid() {
		return (!JavaString.isBlank(data.value.kuerzelStatistik));
	}

	function kuerzelIsValid(value: string | null) {
		if (!mandatoryInputIsValid(value, 50))
			return false;

		return isUniqueInList(value ,props.manager().liste.list(), "kuerzel");
	}


	const formIsValid = computed(() => {
		// alle Felder auf validity prüfen
		return Object.keys(data.value).every(field => {
			const validateField = fieldIsValid(field as keyof FoerderschwerpunktEintrag);
			const fieldValue = data.value[field as keyof FoerderschwerpunktEintrag] as string | null;
			return validateField(fieldValue);
		})
	})

	async function add() {
		if (isLoading.value)
			return;

		props.checkpoint.active = false;
		isLoading.value = true;
		const { id, ...partialData } = data.value;
		await props.addFoerderschwerpunkt(partialData);
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
