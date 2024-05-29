<template>
	<div class="page--content">
		<div class="page--content-flex-column">
			<div class="content-card">
				<div class="content-card--header content-card--headline">Allgemein</div>
				<div class="content-card--content content-card--content--with-title input-wrapper grid-cols-2">
					<svws-ui-text-input placeholder="Bezeichnung" :model-value="stundenplanManager().getBezeichnungStundenplan()" @change="bezeichnungStundenplan=>patch({ bezeichnungStundenplan })" type="text" />
					<div :class="{'flex gap-2': showExtraWTM}">
						<svws-ui-select title="Wochentypmodell" :items="[0,2,3,4,5]" :item-text="i=> ''+wochenTypModell[i]" :model-value="stundenplanManager().getWochenTypModell()" @update:model-value="modell => doPatch(modell)" ref="select" />
						<svws-ui-input-number v-if="showExtraWTM" placeholder="Wochentypmodell" :model-value="stundenplanManager().getWochenTypModell() < 5 ? 5 : stundenplanManager().getWochenTypModell()" @change="modell => doPatch(modell)" :min="5" :max="100" />
					</div>
					<svws-ui-text-input placeholder="Gültig ab" :model-value="stundenplanManager().getGueltigAb()" @change="gueltigAb=>patch({ gueltigAb })" type="date" />
					<svws-ui-text-input placeholder="Gültig bis" :model-value="stundenplanManager().getGueltigBis()" @change="gueltigBis=>patch({ gueltigBis })" type="date" />
				</div>
			</div>
			<s-card-stundenplan-warnung-wochentypmodell v-if="wtmOK === false" :wochen-typ-modell="newWTM" @change="ok => (wtmOK = ok) && doPatch(newWTM)" :stundenplan-manager="stundenplanManager" />
			<svws-ui-action-button title="Jahrgänge" :is-active="actionJahrgaenge" @click="()=>actionJahrgaenge = !actionJahrgaenge" icon="i-ri-archive-line">
				<svws-ui-table :items="listJahrgaenge" :no-data="false" :columns="cols">
					<template #cell(id)="{value}">
						<svws-ui-checkbox type="toggle" :model-value="jahrgaenge.includes(value)" headless @update:model-value="updateJahrgaenge(value)" />
					</template>
				</svws-ui-table>
			</svws-ui-action-button>
			<svws-ui-action-button title="Räume" :is-active="actionRaeume" @click="()=>actionRaeume = !actionRaeume" icon="i-ri-archive-line">
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
						<svws-ui-button @click="gotoKatalog('raeume')" type="transparent" title="Räume im Katalog bearbeiten"><span class="icon i-ri-link" /> Katalog bearbeiten</svws-ui-button>
						<s-card-stundenplan-import-raeume-modal v-slot="{ openModal }" :import-raeume="importRaeume" :list-raeume="raeume">
							<svws-ui-button @click="openModal()" type="transparent" title="Räume importieren"><span class="icon i-ri-archive-line" /> Aus Katalog importieren</svws-ui-button>
						</s-card-stundenplan-import-raeume-modal>
						<svws-ui-button @click="removeRaeume(selected)" type="trash" :disabled="!selected.length" />
						<s-card-stundenplan-add-raum-modal v-slot="{ openModal }" :add-raum="addRaum">
							<svws-ui-button @click="openModal()" type="icon" title="Raum hinzufügen"> <span class="icon i-ri-add-line" /> </svws-ui-button>
						</s-card-stundenplan-add-raum-modal>
					</template>
				</svws-ui-table>
			</svws-ui-action-button>
		</div>
		<div class="page--content-flex-column">
			<svws-ui-action-button title="Pausenzeiten" :is-active="actionPausenzeiten" @click="()=>actionPausenzeiten = !actionPausenzeiten" icon="i-ri-archive-line">
				<svws-ui-table :columns="colsPausenzeiten" :items="itemsPausenzeit" v-model:clicked="zeit" selectable v-model="selectedPausenzeiten" count>
					<template #cell(wochentag)="{ rowData }">
						<svws-ui-select :model-value="Wochentag.fromIDorException(rowData.wochentag)" @update:model-value="wochentag => patchPausenzeit({wochentag: Number(wochentag?.id || -1)}, rowData.id)" :items="Wochentag.values()" :item-text="i=>i.beschreibung" headless />
					</template>
					<template #cell(beginn)="{ rowData }">
						<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(rowData.beginn ?? 0)" @change="beginn => patchPausenBeginn(beginn, rowData.id)" headless />
					</template>
					<template #cell(ende)="{ rowData }">
						<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(rowData.ende ?? 0)" @change="ende => patchPausenEnde(ende, rowData.id)" headless />
					</template>
					<template #cell(klassen)="{ rowData }">
						<svws-ui-multi-select :model-value="[...rowData.klassen]" @update:model-value="klassen => patchPausenKlassen(klassen, rowData.id)" title="Klassen" :items="[...stundenplanManager().klasseGetMengeSichtbarAsList()].map(k=>k.id)" :item-text="klasse => stundenplanManager().klasseGetByIdOrException(klasse).kuerzel" headless />
					</template>
					<template #actions>
						<svws-ui-button @click="gotoKatalog('pausenzeiten')" type="transparent" title="Pausenzeiten im Katalog bearbeiten"><span class="icon i-ri-link" /> Katalog bearbeiten</svws-ui-button>
						<s-card-stundenplan-import-pausenzeiten-modal v-slot="{ openModal }" :import-pausenzeiten="importPausenzeiten" :list-pausenzeiten="listPausenzeitenRest">
							<svws-ui-button @click="openModal()" type="transparent" title="Pausenzeiten importieren"><span class="icon i-ri-archive-line" /> Aus Katalog importieren</svws-ui-button>
						</s-card-stundenplan-import-pausenzeiten-modal>
						<svws-ui-button @click="removePausenzeiten(selectedPausenzeiten)" type="trash" :disabled="!selectedPausenzeiten.length" />
						<s-card-stundenplan-add-pausenzeit-modal v-slot="{ openModal }" :add-pausenzeit="addPausenzeit">
							<svws-ui-button @click="openModal()" type="icon" title="Pausenzeit hinzufügen"> <span class="icon i-ri-add-line" /> </svws-ui-button>
						</s-card-stundenplan-add-pausenzeit-modal>
					</template>
				</svws-ui-table>
			</svws-ui-action-button>
			<svws-ui-action-button title="Aufsichtsbereiche" :is-active="actionAufsichtsbereiche" @click="()=>actionAufsichtsbereiche = !actionAufsichtsbereiche" icon="i-ri-archive-line">
				<svws-ui-table :columns="colsAufsichtsbereiche" :items="listAufsichtsbereiche" v-model:clicked="bereich" selectable v-model="selectedAufsichtsbereiche" count>
					<template #cell(kuerzel)="{ rowData }">
						<svws-ui-text-input :model-value="rowData.kuerzel" @change="kuerzel=>patchAufsichtsbereich({kuerzel}, rowData.id)" headless />
					</template>
					<template #cell(beschreibung)="{ rowData }">
						<svws-ui-text-input :model-value="rowData.beschreibung" @change="beschreibung=>patchAufsichtsbereich({beschreibung}, rowData.id)" headless />
					</template>
					<template #actions>
						<svws-ui-button @click="gotoKatalog('aufsichtsbereiche')" type="transparent" title="Aufsichtsbereiche im Katalog bearbeiten"><span class="icon i-ri-link" /> Katalog bearbeiten</svws-ui-button>
						<s-card-stundenplan-import-aufsichtsbereiche-modal v-slot="{ openModal }" :list-aufsichtsbereiche="listAufsichtsbereicheRest" :import-aufsichtsbereiche="importAufsichtsbereiche">
							<svws-ui-button @click="openModal()" type="transparent" title="Aufsichtsbereiche importieren"><span class="icon i-ri-archive-line" /> Aus Katalog importieren</svws-ui-button>
						</s-card-stundenplan-import-aufsichtsbereiche-modal>
						<svws-ui-button @click="removeAufsichtsbereiche(selectedAufsichtsbereiche)" type="trash" :disabled="!selectedAufsichtsbereiche.length" />
						<s-card-stundenplan-add-aufsichtsbereich-modal v-slot="{ openModal }" :add-aufsichtsbereich="addAufsichtsbereich">
							<svws-ui-button @click="openModal()" type="icon" title="Aufsichtsbereich hinzufügen"> <span class="icon i-ri-add-line" /> </svws-ui-button>
						</s-card-stundenplan-add-aufsichtsbereich-modal>
					</template>
				</svws-ui-table>
			</svws-ui-action-button>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { ComponentExposed } from "vue-component-type-helpers";
	import type { StundenplanDatenProps } from "./SStundenplanDatenProps";
	import type { DataTableColumn } from "@ui";
	import { SvwsUiSelect } from "@ui";
	import type { StundenplanRaum, Raum, StundenplanAufsichtsbereich, StundenplanPausenzeit } from "@core";
	import { ArrayList, DateUtils, Wochentag } from "@core";

	const props = defineProps<StundenplanDatenProps>();
	const select = ref<ComponentExposed<typeof SvwsUiSelect<typeof wochenTypModell>> | null>(null);

	const cols: DataTableColumn[] = [
		{ key: "kuerzel", label: "Jahrgang", sortable: true, defaultSort: "asc", span: 0.25 },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true },
		{ key: "id", label: "Gültig", fixedWidth: 5, align: "center" },
	];

	const actionJahrgaenge = ref<boolean>(false);
	const actionRaeume = ref<boolean>(true);
	const actionPausenzeiten = ref<boolean>(false);
	const actionAufsichtsbereiche = ref<boolean>(true);

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
			if (((props.stundenplanManager().stundenplanGetWochenTypModellSimulation(wochenTypModell) < 1) && (wochenTypModell === 0)) || wtmOK.value === true) {
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
	const zeit = ref<StundenplanPausenzeit | undefined>();
	const selectedPausenzeiten = ref<StundenplanPausenzeit[]>([]);

	const itemsPausenzeit = computed(() => [...props.stundenplanManager().pausenzeitGetMengeAsList()]);

	const colsPausenzeiten = [
		{key: 'wochentag', label: 'Wochentag', span: 1},
		{key: 'beginn', label: 'Beginn', span: 1},
		{key: 'ende', label: 'Ende', span: 1},
		{key: 'klassen', label: 'Nur in Klassen', span: 2},
	]

	async function patchPausenBeginn(minuten: string, id: number) {
		const beginn = DateUtils.gibMinutenOfZeitAsString(minuten);
		await props.patchPausenzeit({beginn}, id);
	}

	async function patchPausenEnde(minuten: string, id: number) {
		const ende = DateUtils.gibMinutenOfZeitAsString(minuten);
		await props.patchPausenzeit({ende}, id);
	}

	async function patchPausenKlassen(ids: number[], id: number) {
		const klassen = new ArrayList<number>();
		for (const klassenID of ids)
			klassen.add(klassenID);
		await props.patchPausenzeit({klassen}, id);
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
		{key: 'beschreibung', label: 'Beschreibung', span: 3},
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
		@apply overflow-y-hidden overflow-x-auto h-full pb-3 pt-6 flex flex-row
	}

	.page--content-flex-column {
		@apply h-full overflow-y-auto w-full xl:w-1/2 flex flex-col gap-8
	}

</style>
