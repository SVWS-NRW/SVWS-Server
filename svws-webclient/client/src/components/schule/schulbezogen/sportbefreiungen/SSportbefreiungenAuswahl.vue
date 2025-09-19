<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Sportbefreiungen</h1>
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
							Neue Sportbefreiung anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { DataTableColumn } from "@ui";
	import type { SportbefreiungenAuswahlProps } from "~/components/schule/schulbezogen/sportbefreiungen/SSportbefreiungenAuswahlProps";
	import { useRegionSwitch, ViewType } from "@ui";
	import { BenutzerKompetenz, ServerMode } from "@core";
	import type { Sportbefreiung } from "@core";
	import { computed } from "vue";

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const props = defineProps<SportbefreiungenAuswahlProps>();
	const hatKompetenzAendern = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const selectedEntry = computed(() => {
		if ((props.activeViewType === ViewType.GRUPPENPROZESSE) || (props.activeViewType === ViewType.HINZUFUEGEN))
			return null;
		return (props.manager().hasDaten()) ? props.manager().auswahl() : null;
	})
	const columns: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, defaultSort: "asc"},
	];

	async function setAuswahl(sportbefreiungen: Sportbefreiung[]) {
		props.manager().liste.auswahlClear();
		for (const sportbefreiung of sportbefreiungen)
			if (props.manager().liste.hasValue(sportbefreiung))
				props.manager().liste.auswahlAdd(sportbefreiung);
		if (props.manager().liste.auswahlExists())
			await props.gotoGruppenprozessView(true);
		else
			await props.gotoDefaultView(props.manager().getVorherigeAuswahl()?.id);
	}

</script>
