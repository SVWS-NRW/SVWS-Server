<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Haltestellen</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table clickable :clicked="selectedEntry" @update:clicked="v => gotoDefaultView(v.id)" :items="props.manager().filtered()" :columns
				:model-value="[...props.manager().liste.auswahl()]" @update:model-value="v => setAuswahl(v)" selectable scroll-into-view
				:focus-switching-enabled :focus-help-visible>
				<template #actions>
					<svws-ui-tooltip position="bottom" v-if="ServerMode.DEV.checkServerMode(serverMode) && hatKompetenzAendern">
						<svws-ui-button :disabled="activeViewType === ViewType.HINZUFUEGEN" type="icon" @click="gotoHinzufuegenView(true)"
							:has-focus="manager().filtered().size() === 0">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neue Haltestelle anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { HaltestellenAuswahlProps } from "~/components/schule/allgemein/haltestellen/SHaltestellenAuswahlProps";
	import type { DataTableColumn } from "@ui";
	import { useRegionSwitch, ViewType } from "@ui";
	import { BenutzerKompetenz, ServerMode } from "@core";
	import type { Haltestelle } from "@core";
	import { computed } from "vue";

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const props = defineProps<HaltestellenAuswahlProps>();
	const hatKompetenzAendern = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const selectedEntry = computed(() => {
		if ((props.activeViewType === ViewType.GRUPPENPROZESSE) || (props.activeViewType === ViewType.HINZUFUEGEN))
			return null;
		return (props.manager().hasDaten()) ? props.manager().auswahl() : null;
	})
	const columns: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 2, defaultSort: 'asc' },
	];

	async function setAuswahl(haltestellen: Haltestelle[]) {
		props.manager().liste.auswahlClear();
		for (const haltestelle of haltestellen)
			if (props.manager().liste.hasValue(haltestelle))
				props.manager().liste.auswahlAdd(haltestelle);
		if (props.manager().liste.auswahlExists())
			await props.gotoGruppenprozessView(true);
		else
			await props.gotoDefaultView(props.manager().getVorherigeAuswahl()?.id)
	}

</script>
