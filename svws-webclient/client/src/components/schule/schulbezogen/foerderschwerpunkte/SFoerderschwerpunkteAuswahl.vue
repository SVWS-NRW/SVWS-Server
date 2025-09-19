<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Förderschwerpunkte</h1>
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
							Neuen Förderschwerpunkt anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { FoerderschwerpunkteAuswahlProps } from "~/components/schule/schulbezogen/foerderschwerpunkte/SFoerderschwerpunkteAuswahlProps";
	import type { DataTableColumn } from "@ui";
	import type { FoerderschwerpunktEintrag } from "@core";
	import { BenutzerKompetenz, ServerMode } from "@core";
	import { useRegionSwitch, ViewType } from "@ui";
	import { computed } from "vue";

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const props = defineProps<FoerderschwerpunkteAuswahlProps>();
	const hatKompetenzAendern = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const selectedEntry = computed(() => {
		if ((props.activeViewType === ViewType.GRUPPENPROZESSE) || (props.activeViewType === ViewType.HINZUFUEGEN))
			return null;
		return (props.manager().hasDaten()) ? props.manager().auswahl() : null;
	})
	const columns: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc' },
		{ key: "text", label: "Bezeichnung", sortable: true, span: 4 },
	];

	async function setAuswahl(foerderschwerpunkte: FoerderschwerpunktEintrag[]) {
		props.manager().liste.auswahlClear();
		for (const foerderschwerpunkt of foerderschwerpunkte)
			if (props.manager().liste.hasValue(foerderschwerpunkt))
				props.manager().liste.auswahlAdd(foerderschwerpunkt);
		if (props.manager().liste.auswahlExists())
			await props.gotoGruppenprozessView(true);
		else
			await props.gotoDefaultView(props.manager().getVorherigeAuswahl()?.id)
	}

</script>
