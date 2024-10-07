<template>
	<svws-ui-secondary-menu>
		<template #headline><span class="select-none">Oberstufe</span></template>
		<template #abschnitt>
			<abschnitt-auswahl :daten="schuljahresabschnittsauswahl" />
		</template>
		<template #content>
			<svws-ui-table :clicked="auswahl" clickable :model-value="selected()" @update:model-value="setAuswahl" :selectable="hatUpdateKompetenz" :unselectable
				@update:clicked="gotoAbiturjahrgang" :items :columns :filter-open="false">
				<template #filterAdvanced>
					<div class="col-span-full flex flex-wrap gap-x-5">
						<svws-ui-checkbox type="toggle" :model-value="filterNurAktuelle()" @update:model-value="setFilterNurAktuelle">Nur Aktuelle Jahrgänge</svws-ui-checkbox>
					</div>
				</template>
				<template #cell(abiturjahr)="{ value }">
					<span v-if="value === -1" class="opacity-25"> — </span>
					<span v-else> {{ value }} </span>
					<svws-ui-spinner :spinning="(apiStatus.pending && value === (auswahl?.abiturjahr))" />
				</template>
				<template #cell(jahrgang)="{ value }">
					<span v-if="!value" class="opacity-25"> — </span>
					<span v-else> {{ value }} </span>
				</template>
				<template #actions v-if="hatUpdateKompetenz">
					<svws-ui-tooltip v-if="mapJahrgaengeOhneAbiJahrgang().size > 0" position="bottom">
						<svws-ui-button type="icon" @click="gotoCreationMode"> <span class="icon i-ri-add-line" /> </svws-ui-button>
						<template #content>Abiturjahr hinzufügen</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
			<router-view name="gost_child_auswahl" />
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { GostAuswahlProps } from "./SGostAuswahlProps";
	import type { GostJahrgang } from "@core";
	import { BenutzerKompetenz } from "@core";

	const props = defineProps<GostAuswahlProps>();

	const columns = [
		{ key: "bezeichnung", label: "Abiturjahrgang", sortable: true, span: 2 },
		{ key: "abiturjahr", label: "Jahr", sortable: true },
		{ key: "jahrgang", label: "Stufe", sortable: true }];

	const items = computed<GostJahrgang[]>(() => {
		const list = [...props.mapAbiturjahrgaenge().values()];
		const filtern = props.filterNurAktuelle();
		return list.filter(a => filtern && !a.istAbgeschlossen).sort((a, b) => (a.bezeichnung ?? "") < (b.bezeichnung ?? "") ? 1 : -1)
	});

	const hatUpdateKompetenz = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.OBERSTUFE_ABITURJAHRGAENGE_VERWALTEN));

	const unselectable = computed(() => {
		const set = new Set<GostJahrgang>();
		for (const j of items.value)
			if (j.abiturjahr < 0)
				set.add(j);
		return set;
	})

	async function setAuswahl(list : GostJahrgang[]) {
		props.setSelected(list);
		if (props.selected().length > 0)
			await props.gotoGruppenprozess(true);
		else {
			let auswahl;
			if (props.auswahl !== undefined)
				auswahl = props.auswahl;
			else
				[auswahl] = items.value;
			await props.gotoAbiturjahrgang(auswahl);
		}
	}

</script>
