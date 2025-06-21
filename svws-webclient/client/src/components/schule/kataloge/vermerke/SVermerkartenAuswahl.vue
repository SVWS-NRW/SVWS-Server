<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Vermerkarten</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<div class="container">
				<svws-ui-table clickable :clicked="clickedEintrag" @update:clicked="eintrag => gotoDefaultView(eintrag.id)" :items="props.manager().filtered()" :columns selectable
					:model-value="[...props.manager().liste.auswahl()]" @update:model-value="items => setAuswahl(items)" scroll-into-view :focus-switching-enabled :focus-help-visible>
					<template #filterAdvanced>
						<svws-ui-checkbox type="toggle" v-model="filterNurSichtbare">Nur Sichtbare</svws-ui-checkbox>
					</template>
					<template #cell(anzahlVermerke)="{ value, rowData }">
						<div class="inline-flex min-h-5">
							<div v-if="isRemovable(rowData)" class="inline-flex">
								<span class="icon i-ri-alert-line mx-0.5 mr-1" />
								<p>verwendet</p>
							</div>
							<p class="w-8"> {{ value }} </p>
						</div>
					</template>
					<template #actions>
						<svws-ui-tooltip v-if="hatKompetenzAendern" position="bottom">
							<svws-ui-button :disabled="activeViewType === ViewType.HINZUFUEGEN" type="icon" @click="gotoHinzufuegenView(true)" :has-focus="manager().filtered().isEmpty()">
								<span class="icon i-ri-add-line" />
							</svws-ui-button>
							<template #content>
								Neue Vermerkart anlegen
							</template>
						</svws-ui-tooltip>
					</template>
				</svws-ui-table>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { BenutzerKompetenz, type VermerkartEintrag } from "@core";
	import type { DataTableColumn} from "@ui";
	import { useRegionSwitch, ViewType } from "@ui";
	import type { VermerkeAuswahlProps } from "./SVermerkartenAuswahlProps";

	const props = defineProps<VermerkeAuswahlProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const columns : DataTableColumn[] = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, defaultSort: "asc", span: 2},
		{ key: "anzahlVermerke", label: "Anzahl", sortable: true, defaultSort: "asc", span: 1, align: "right"},
	];

	const hatKompetenzAendern = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));

	const filterNurSichtbare = computed<boolean>({
		get: () => props.manager().filterNurSichtbar(),
		set: (value) => {
			props.manager().setFilterNurSichtbar(value);
			void props.setFilter();
		},
	});

	async function setAuswahl(items : VermerkartEintrag[]) {
		props.manager().liste.auswahlClear();
		for (const item of items)
			if (props.manager().liste.hasValue(item) === true)
				props.manager().liste.auswahlAdd(item);
		if (props.manager().liste.auswahlExists() === true)
			await props.gotoGruppenprozessView(true);
		else
			await props.gotoDefaultView(props.manager().getVorherigeAuswahl()?.id);
	}

	const clickedEintrag = computed(() => {
		if ((props.activeViewType === ViewType.GRUPPENPROZESSE) || (props.activeViewType === ViewType.HINZUFUEGEN))
			return null;
		return (props.manager().hasDaten() === true) ? props.manager().auswahl() : null;
	});

	function isRemovable(rowData: VermerkartEintrag) {
		return [... props.manager().liste.auswahl()].includes(rowData) && (rowData.anzahlVermerke > 0);
	}

</script>
