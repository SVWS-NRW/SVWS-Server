<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<span>Jahrgänge</span>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :daten="schuljahresabschnittsauswahl" />
		</template>
		<template #header />
		<template #content>
			<svws-ui-table :clickable="!jahrgangListeManager().liste.auswahlExists()" :clicked="clickedEintrag" @update:clicked="jahrgangsdaten => gotoDefaultView(jahrgangsdaten.id)" :items="jahrgangListeManager().filtered()"
				:model-value="[...jahrgangListeManager().liste.auswahl()]" @update:model-value="items => setAuswahl(items)" :columns :filter-open="true" selectable count scroll-into-view scroll allow-arrow-key-selection>
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
						<svws-ui-button :disabled="activeViewType === ViewType.HINZUFUEGEN" type="icon" @click="gotoHinzufuegenView(true)" :has-focus="jahrgangListeManager().filtered().size() === 0">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neuen Jahrgang anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { JahrgaengeAuswahlProps } from "./SJahrgaengeAuswahlProps";
	import { computed } from "vue";
	import { ViewType } from "@ui";
	import { BenutzerKompetenz, type JahrgangsDaten, ServerMode } from "@core";

	const props = defineProps<JahrgaengeAuswahlProps>();

	const hatKompetenzAendern = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));

	const clickedEintrag = computed(() => {
		if ((props.activeViewType === ViewType.GRUPPENPROZESSE) || (props.activeViewType === ViewType.HINZUFUEGEN))
			return null;
		return props.jahrgangListeManager().hasDaten() ? props.jahrgangListeManager().auswahl() : null;
	});

	async function setAuswahl(items : JahrgangsDaten[]) {
		props.jahrgangListeManager().liste.auswahlClear();
		for (const item of items)
			if (props.jahrgangListeManager().liste.hasValue(item))
				props.jahrgangListeManager().liste.auswahlAdd(item);
		if (props.jahrgangListeManager().liste.auswahlExists())
			await props.gotoGruppenprozessView(true);
		else
			await props.gotoDefaultView(props.jahrgangListeManager().getVorherigeAuswahl()?.id);
	}

	const columns = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc" },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 3 }
	];

</script>
