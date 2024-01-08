<template>
	<div class="page--content">
		<div class="h-full overflow-y-scroll overflow-x-hidden pr-4">
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Bezeichnung" :model-value="stundenplanManager().getBezeichnungStundenplan()" @change="bezeichnungStundenplan=>patch({ bezeichnungStundenplan })" type="text" />
					<svws-ui-input-number placeholder="Wochentypmodell" :model-value="stundenplanManager().getWochenTypModell() || 1" @change="wochenTypModell => doPatch(wochenTypModell)" :min="1" />
					<svws-ui-text-input placeholder="Gültig ab" :model-value="stundenplanManager().getGueltigAb()" @change="gueltigAb=>patch({ gueltigAb })" type="date" />
					<svws-ui-text-input placeholder="Gültig bis" :model-value="stundenplanManager().getGueltigBis()" @change="gueltigBis=>patch({ gueltigBis })" type="date" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Jahrgänge" class="ml-1">
				<div class="col-span-full">
					<svws-ui-table :items="listJahrgaenge" :no-data="false" :columns="cols">
						<template #cell(id)="{value}">
							<svws-ui-checkbox type="toggle" :model-value="jahrgaenge.includes(value)" headless @update:model-value="updateJahrgaenge(value)" />
						</template>
					</svws-ui-table>
				</div>
			</svws-ui-content-card>
		</div>
		<div class="h-full overflow-y-scroll overflow-x-hidden pr-4">
			<svws-ui-content-card title="Räume" class="ml-1">
				<svws-ui-table :columns="colsRaeume" :items="items" v-model:clicked="raum" selectable v-model="selected" count>
					<template #cell(kuerzel)="{ rowData }">
						<svws-ui-text-input :model-value="rowData.kuerzel" @change="kuerzel => patchRaum({kuerzel}, rowData.id)" headless required />
					</template>
					<template #cell(groesse)="{ rowData }">
						<svws-ui-input-number :model-value="rowData.groesse" @change="groesse => groesse && patchRaum({groesse}, rowData.id)" headless required />
					</template>
					<template #cell(beschreibung)="{ rowData }">
						<svws-ui-text-input :model-value="rowData.beschreibung" @change="beschreibung => patchRaum({beschreibung}, rowData.id)" headless />
					</template>
					<template #actions>
						<svws-ui-button @click="gotoKatalog('raeume')" type="transparent" title="Aufsichtsbereiche importieren"><i-ri-link /> Katalog bearbeiten</svws-ui-button>
						<s-card-stundenplan-import-raeume-modal v-slot="{ openModal }" :import-raeume="importRaeume" :list-raeume="raeume">
							<svws-ui-button @click="openModal()" type="transparent" title="Räume importieren"><i-ri-archive-line /> Aus Katalog importieren</svws-ui-button>
						</s-card-stundenplan-import-raeume-modal>
						<svws-ui-button @click="removeRaeume(selected)" type="trash" :disabled="!selected.length" />
						<s-card-stundenplan-add-raum-modal v-slot="{ openModal }" :add-raum="addRaum">
							<svws-ui-button @click="openModal()" type="icon" title="Raum hinzufügen"> <i-ri-add-line /> </svws-ui-button>
						</s-card-stundenplan-add-raum-modal>
					</template>
				</svws-ui-table>
			</svws-ui-content-card>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import { type StundenplanRaum, type Raum, ArrayList } from "@core";
	import type { DataTableColumn } from "@ui";
	import type { StundenplanDatenProps } from "./SStundenplanDatenProps";

	const props = defineProps<StundenplanDatenProps>();

	const cols: DataTableColumn[] = [
		{ key: "kuerzel", label: "Jahrgang", sortable: true, defaultSort: "asc", span: 0.25 },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true },
		{ key: "id", label: "Gültig", fixedWidth: 5, align: "center" },
	];

	const jahrgaenge = computed(()=> {
		const list = props.stundenplanManager().jahrgangGetMengeAsList();
		const a = [];
		for (const j of list)
			a.push(j.id);
		return a;
	})

	async function updateJahrgaenge(id: number) {
		if (jahrgaenge.value.includes(id))
			await props.removeJahrgang(id);
		else {
			await props.removeJahrgang(id);
		}
	}

	async function doPatch(wochenTypModell: number | null) {
		if (wochenTypModell === null || wochenTypModell < 1)
			return;
		// 1 ist nicht zulässig, wird zu 0 konvertiert, allgemein
		await props.patch({wochenTypModell: wochenTypModell === 1 ? 0 : wochenTypModell});
	}



	const raum = ref<StundenplanRaum | undefined>();
	const selected = ref<StundenplanRaum[]>([]);
	const items = computed(()=>[...props.stundenplanManager().raumGetMengeAsList()]);

	const colsRaeume = [
		{key: 'kuerzel', label: 'Kürzel', span: 1},
		{key: 'beschreibung', label: 'Beschreibung', span: 3},
		{key: 'groesse', label: 'Größe', span: 1},
	]

	const raeume = computed(() => {
		const moeglich = new ArrayList<Raum>();
		for (const e of props.listRaeume())
			if (!props.stundenplanManager().raumExistsByKuerzel(e.kuerzel))
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
