<template>
	<div class="page--content">
		<div class="h-full overflow-y-scroll overflow-x-hidden pr-4">
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Bezeichnung" :model-value="stundenplanManager().getBezeichnungStundenplan()" @change="bezeichnungStundenplan=>patch({ bezeichnungStundenplan })" type="text" />
					<div :class="{'flex gap-2': showExtraWTM}">
						<svws-ui-select :items="[0,2,3,4,5]" :item-text="i=> ''+wochenTypModell[i]" :model-value="stundenplanManager().getWochenTypModell()" @update:model-value="modell => doPatch(modell)" ref="select" />
						<svws-ui-input-number v-if="showExtraWTM" placeholder="Wochentypmodell" :model-value="stundenplanManager().getWochenTypModell() < 5 ? 5 : stundenplanManager().getWochenTypModell()" @change="modell => doPatch(modell)" :min="5" :max="100" />
					</div>
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
			<s-card-stundenplan-warnung-wochentypmodell v-if="wtmOK === false" :wochen-typ-modell="newWTM" @change="ok => (wtmOK = ok) && doPatch(newWTM)" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { ComponentExposed } from "vue-component-type-helpers";
	import type { StundenplanDatenProps } from "./SStundenplanDatenProps";
	import type { DataTableColumn } from "@ui";
	import { SvwsUiSelect } from "@ui";
	import type { StundenplanRaum, Raum } from "@core";
	import { ArrayList } from "@core";

	const props = defineProps<StundenplanDatenProps>();
	const select = ref<ComponentExposed<typeof SvwsUiSelect<typeof wochenTypModell>> | null>(null);

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
		else
			await props.addJahrgang(id);
	}

	async function doPatch(wochenTypModell: number | null | undefined) {
		if ((wochenTypModell !== null) && (wochenTypModell !== undefined) && (wochenTypModell !== 1)) {
			if (props.stundenplanManager().stundenplanGetWochenTypModellSimulation(wochenTypModell) < 1 || wtmOK.value === true) {
				await props.patch({wochenTypModell});
				wtmOK.value === undefined;
			}
			else if (wtmOK.value === false)
				select.value?.reset(true);
			else {
				newWTM.value = wochenTypModell;
				wtmOK.value = false;
			}
		}
	}

	const wochenTypModell = ['keins', null, 'AB-Wochen', 'ABC-Wochen', 'ABCD-Wochen', 'weitere'];
	const showExtraWTM = computed(() => props.stundenplanManager().getWochenTypModell() > 4);
	const wtmOK = ref<boolean | undefined>(undefined);
	const newWTM = ref<number>(-1);

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
