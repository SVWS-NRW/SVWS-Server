<template>
	<div class="page--content">
		<div class="h-full overflow-y-scroll overflow-x-hidden pr-4">
			<svws-ui-content-card title="Pausenzeiten" class="ml-1">
				<svws-ui-table :columns="colsPausenzeiten" :items="items" v-model:clicked="zeit" selectable v-model="selectedPausenzeiten" count>
					<template #cell(wochentag)="{ rowData }">
						<svws-ui-select :model-value="Wochentag.fromIDorException(rowData.wochentag)" @update:model-value="wochentag => patchPausenzeit({wochentag: Number(wochentag?.id || -1)}, rowData.id)" :items="Wochentag.values()" :item-text="i=>i.beschreibung" headless />
					</template>
					<template #cell(beginn)="{ rowData }">
						<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(rowData.beginn ?? 0)" @change="beginn => patchPausenBeginn(beginn, rowData.id)" headless />
					</template>
					<template #cell(ende)="{ rowData }">
						<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(rowData.ende ?? 0)" @change="ende => patchPausenEnde(ende, rowData.id)" headless />
					</template>
					<template #actions>
						<svws-ui-button @click="gotoKatalog('pausenzeiten')" type="transparent" title="Aufsichtsbereiche importieren"><i-ri-link /> Katalog bearbeiten</svws-ui-button>
						<s-card-stundenplan-import-pausenzeiten-modal v-slot="{ openModal }" :import-pausenzeiten="importPausenzeiten" :list-pausenzeiten="listPausenzeitenRest">
							<svws-ui-button @click="openModal()" type="transparent" title="Pausenzeiten importieren"><i-ri-archive-line /> Aus Katalog importieren</svws-ui-button>
						</s-card-stundenplan-import-pausenzeiten-modal>
						<svws-ui-button @click="removePausenzeiten(selectedPausenzeiten)" type="trash" :disabled="!selectedPausenzeiten.length" />
						<s-card-stundenplan-add-pausenzeit-modal v-slot="{ openModal }" :add-pausenzeit="addPausenzeit">
							<svws-ui-button @click="openModal()" type="icon" title="Pausenzeit hinzufügen"> <i-ri-add-line /> </svws-ui-button>
						</s-card-stundenplan-add-pausenzeit-modal>
					</template>
				</svws-ui-table>
			</svws-ui-content-card>
		</div>
		<div class="h-full overflow-y-scroll overflow-x-hidden pr-4">
			<svws-ui-content-card title="Aufsichtsbereiche" class="ml-1">
				<svws-ui-table :columns="colsAufsichtsbereiche" :items="listAufsichtsbereiche" v-model:clicked="bereich" selectable v-model="selectedAufsichtsbereiche" count>
					<template #cell(kuerzel)="{ rowData }">
						<svws-ui-text-input :model-value="rowData.kuerzel" @change="kuerzel=>patchAufsichtsbereich({kuerzel}, rowData.id)" headless />
					</template>
					<template #cell(beschreibung)="{ rowData }">
						<svws-ui-text-input :model-value="rowData.beschreibung" @change="beschreibung=>patchAufsichtsbereich({beschreibung}, rowData.id)" headless />
					</template>
					<template #actions>
						<svws-ui-button @click="gotoKatalog('aufsichtsbereiche')" type="transparent" title="Aufsichtsbereiche importieren"><i-ri-link /> Katalog bearbeiten</svws-ui-button>
						<s-card-stundenplan-import-aufsichtsbereiche-modal v-slot="{ openModal }" :list-aufsichtsbereiche="listAufsichtsbereicheRest" :import-aufsichtsbereiche="importAufsichtsbereiche">
							<svws-ui-button @click="openModal()" type="transparent" title="Aufsichtsbereiche importieren"><i-ri-archive-line /> Aus Katalog importieren</svws-ui-button>
						</s-card-stundenplan-import-aufsichtsbereiche-modal>
						<svws-ui-button @click="removeAufsichtsbereiche(selectedAufsichtsbereiche)" type="trash" :disabled="!selectedAufsichtsbereiche.length" />
						<s-card-stundenplan-add-aufsichtsbereich-modal v-slot="{ openModal }" :add-aufsichtsbereich="addAufsichtsbereich">
							<svws-ui-button @click="openModal()" type="icon" title="Aufsichtsbereich hinzufügen"> <i-ri-add-line /> </svws-ui-button>
						</s-card-stundenplan-add-aufsichtsbereich-modal>
					</template>
				</svws-ui-table>
			</svws-ui-content-card>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import { type StundenplanAufsichtsbereich, type StundenplanPausenzeit, ArrayList, Wochentag, DateUtils } from "@core";
	import type { StundenplanPausenProps } from "./StundenplanPausenProps";

	const props = defineProps<StundenplanPausenProps>();

	const zeit = ref<StundenplanPausenzeit | undefined>();
	const selectedPausenzeiten = ref<StundenplanPausenzeit[]>([]);

	const items = computed(() => [...props.stundenplanManager().pausenzeitGetMengeAsList()]);

	const colsPausenzeiten = [
		{key: 'wochentag', label: 'Wochentag', span: 1},
		{key: 'beginn', label: 'Beginn', span: 1}, {key: 'ende', label: 'Ende', span: 1}
	]

	async function patchPausenBeginn(minuten: string, id: number) {
		const beginn = DateUtils.gibMinutenOfZeitAsString(minuten);
		await props.patchPausenzeit({beginn}, id);
	}

	async function patchPausenEnde(minuten: string, id: number) {
		const ende = DateUtils.gibMinutenOfZeitAsString(minuten);
		await props.patchPausenzeit({ende}, id);
	}

	const listPausenzeitenRest = computed(() => {
		const moeglich = new ArrayList<StundenplanPausenzeit>();
		for (const e of props.listPausenzeiten())
			if (!props.stundenplanManager().pausenzeitExistsByWochentagAndBeginnAndEnde(e.wochentag, e.beginn, e.ende))
				moeglich.add(e);
		return moeglich;
	})

	const bereich = ref<StundenplanAufsichtsbereich | undefined>();
	const selectedAufsichtsbereiche = ref<StundenplanAufsichtsbereich[]>([]);

	const listAufsichtsbereiche = computed(() => [...props.stundenplanManager().aufsichtsbereichGetMengeAsList()]);

	const colsAufsichtsbereiche = [
		{key: 'kuerzel', label: 'Kürzel', span: 1},
		{key: 'beschreibung', label: 'Beschreibung', span: 3}
	]

	const listAufsichtsbereicheRest = computed(() => {
		const moeglich = new ArrayList<StundenplanAufsichtsbereich>();
		for (const e of props.listAufsichtsbereiche())
			if (!props.stundenplanManager().aufsichtsbereichExistsByKuerzel(e.kuerzel))
				moeglich.add(e);
		return moeglich;
	})

</script>

<style lang="postcss" scoped>

	.page--content {
		@apply grid overflow-y-hidden overflow-x-auto h-full pb-3 pt-6 lg:gap-x-8;
		grid-auto-rows: 100%;
		grid-template-columns: 1fr 1fr;
		grid-auto-columns: max-content;
	}

</style>
