<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-y-16 lg:gap-y-20">
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-wrapper>
						<svws-ui-select v-model="religion" title="Statistikkürzel" :items="religionKatalogList" :item-text="i => i.kuerzel || '--- bitte auswählen ---'" required />
					</svws-ui-input-wrapper>
					<svws-ui-text-input v-model="data.bezeichnung" placeholder="Bezeichnung" />
					<svws-ui-text-input v-model="data.bezeichnungZeugnis" placeholder="Zeugnisbezeichnung" />
				</svws-ui-input-wrapper>
				<div class="mt-7 flex flex-row gap-4 justify-end">
					<svws-ui-button type="secondary" @click="cancel" :disabled="isLoading">Abbrechen</svws-ui-button>
					<svws-ui-button @click="addReligion()" :disabled="!isValid || isLoading">
						Speichern <svws-ui-spinner :spinning="isLoading" />
					</svws-ui-button>
				</div>
			</svws-ui-content-card>
		</div>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">
	import { computed, ref, watch } from "vue";
	import type { KatalogReligionNeuProps } from "./SKatalogReligionNeuProps";
	import { ArrayList, type List, Religion, ReligionEintrag } from "@core";

	const props = defineProps<KatalogReligionNeuProps>();

	const isLoading = ref<boolean>(false);
	const isValid = ref<boolean>(false);
	const data = ref<ReligionEintrag>(new ReligionEintrag());

	watch(() => data.value, async () => {
		if (isLoading.value)
			return;

		props.checkpoint.active = true;
		validateAll();
	}, { immediate: false, deep: true });

	const religionKatalogList = computed<List<ReligionEintrag>>(() => {
		const religionList = Religion.data().getEintraegeBySchuljahr(props.religionListeManager().getSchuljahr());
		const religionKatalogList = new ArrayList<ReligionEintrag>();
		for (const religion of religionList){
			const religionEintrag = new ReligionEintrag();
			religionEintrag.kuerzel = religion.kuerzel;
			religionEintrag.bezeichnung = religion.text;
			religionEintrag.bezeichnungZeugnis = religion.text;
			religionKatalogList.add(religionEintrag);
		}
		return religionKatalogList;
	});

	const religion = computed<ReligionEintrag>({
		get: () => data.value,
		set: (value: ReligionEintrag) => data.value = value,
	});

	const validateAll = () => isValid.value = validateStatistikKuerzel(data.value.kuerzel) && validateBezeichnung(data.value.bezeichnung);
	const validateStatistikKuerzel = (kuerzel: string | null): boolean => kuerzel !== null;
	const validateBezeichnung = (bezeichnung: string | null): boolean => {
		if (bezeichnung === null)
			return false;

		for (const religion of props.religionListeManager().liste.list())
			if (religion.bezeichnung === bezeichnung)
				return false;

		return true;
	}

	async function cancel() {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	async function addReligion() {
		if (isLoading.value === true)
			return;

		isLoading.value = true;
		props.checkpoint.active = false;
		await props.add(data.value);
		isLoading.value = false;
	}



</script>
