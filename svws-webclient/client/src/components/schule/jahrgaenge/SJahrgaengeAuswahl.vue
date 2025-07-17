<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Jahrgänge</h1>
			<div><abschnitt-auswahl :daten="schuljahresabschnittsauswahl" /></div>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table :clickable="!manager().liste.auswahlExists()" :clicked="clickedEintrag" @update:clicked="jahrgangsdaten => gotoDefaultView(jahrgangsdaten.id)" :items="manager().filtered()"
				:model-value="[...manager().liste.auswahl()]" @update:model-value="items => setAuswahl(items)" :columns :filter-open="true" selectable count scroll-into-view scroll allow-arrow-key-selection
				:focus-switching-enabled :focus-help-visible>
				<template #cell(bezeichnung)="{ value, rowData }">
					{{ value }}
					<svws-ui-tooltip position="bottom" v-if="rowData.anzahlRestabschnitte === null" autosize>
						<span class="icon i-ri-alert-line" />
						<template #content>
							Der Jahrgang hat keine Anzahl für die Restabschnitte definiert.
						</template>
					</svws-ui-tooltip>
				</template>
				<template #actions>
					<svws-ui-tooltip position="bottom" v-if="ServerMode.DEV.checkServerMode(serverMode) && hatKompetenzAendern">
						<svws-ui-button :disabled="activeViewType === ViewType.HINZUFUEGEN" type="icon" @click="gotoHinzufuegenView(true)" :has-focus="manager().filtered().size() === 0">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neuen Jahrgang anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { JahrgaengeAuswahlProps } from "./SJahrgaengeAuswahlProps";
	import { computed } from "vue";
	import { useRegionSwitch, ViewType } from "@ui";
	import { BenutzerKompetenz, type JahrgangsDaten, ServerMode } from "@core";

	const props = defineProps<JahrgaengeAuswahlProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const hatKompetenzAendern = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));

	const clickedEintrag = computed(() => {
		if ((props.activeViewType === ViewType.GRUPPENPROZESSE) || (props.activeViewType === ViewType.HINZUFUEGEN))
			return null;
		return props.manager().hasDaten() ? props.manager().auswahl() : null;
	});

	async function setAuswahl(items : JahrgangsDaten[]) {
		props.manager().liste.auswahlClear();
		for (const item of items)
			if (props.manager().liste.hasValue(item))
				props.manager().liste.auswahlAdd(item);
		if (props.manager().liste.auswahlExists())
			await props.gotoGruppenprozessView(true);
		else
			await props.gotoDefaultView(props.manager().getVorherigeAuswahl()?.id);
	}

	const columns = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc" },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 3 },
	];

</script>
