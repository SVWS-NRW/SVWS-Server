<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Bezeichnung" span="2"
						v-model="model.proxy.bezeichnung"
						:validation="() => model.getFehler('bezeichnung')"
						:min-len="1" :max-len="30" required :disabled="!hatKompetenzAdd" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="model.proxy.sortierung"
						:validation="() => model.getFehler('sortierung')"
						:min="0" :max="32000" :removable="false" :readonly="!hatKompetenzAdd" />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="model.proxy.istSichtbar" :disabled="!hatKompetenzAdd">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">
					Abbrechen
				</svws-ui-button>
				<svws-ui-button @click="addErzieherart" :disabled="!formIsValid">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import type { ErzieherartenNeuProps } from "~/components/schule/kataloge/erzieherarten/ErzieherartenNeuProps";
	import { computed, ref, watch } from "vue";
	import { BenutzerKompetenz, Erzieherart } from "@core";
	import { ErzieherartenModelProxy } from "~/components/schule/kataloge/erzieherarten/modelproxy/ErzieherartenModelProxy";

	const props = defineProps<ErzieherartenNeuProps>();
	const hatKompetenzAdd = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const initialData = ref<Erzieherart>(Object.assign(new Erzieherart(), { istSichtbar: true, sortierung: 32000 }));
	const model = new ErzieherartenModelProxy(() => initialData.value, () => props.manager());
	const formIsValid = computed(() => model.getAlleFehler().isEmpty());
	const isLoading = ref<boolean>(false);

	async function addErzieherart() {
		if (isLoading.value === true) {
			return;
		}
		isLoading.value = true;
		props.checkpoint.active = false;
		const { id, referenziertInAnderenTabellen, ...partialData } = model.proxy;
		await props.add(partialData);
		isLoading.value = false;
	}

	async function cancel() {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	watch(() => model.proxy, async () => {
		if (isLoading.value) {
			return;
		}
		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

</script>
