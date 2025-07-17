<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Erzieherarten</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<div class="container">
				<svws-ui-table clickable :clicked="clickedEintrag" @update:clicked="eintrag => gotoDefaultView(eintrag.id)"
					:items="props.manager().filtered()" :columns selectable
					:model-value="[...props.manager().liste.auswahl()]" @update:model-value="items => setAuswahl(items)" scroll-into-view
					:focus-switching-enabled :focus-help-visible>
					<template #actions>
						<svws-ui-tooltip v-if="hatKompetenzAendern" position="bottom">
							<svws-ui-button :disabled="activeViewType === ViewType.HINZUFUEGEN" type="icon" @click="gotoHinzufuegenView(true)"
								:has-focus="manager().filtered().isEmpty()">
								<span class="icon i-ri-add-line" />
							</svws-ui-button>
							<template #content>
								Neue Erzieherart anlegen
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
	import type { Erzieherart } from "@core";
	import { BenutzerKompetenz } from "@core";
	import type { DataTableColumn } from "@ui";
	import { useRegionSwitch, ViewType } from "@ui";
	import type { ErzieherartenAuswahlProps } from "~/components/schule/kataloge/erzieherarten/SErzieherartenAuswahlProps";

	const props = defineProps<ErzieherartenAuswahlProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const columns : DataTableColumn[] = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, defaultSort: "asc" },
	];

	const hatKompetenzAendern = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));

	async function setAuswahl(items : Erzieherart[]) {
		props.manager().liste.auswahlClear();
		for (const item of items)
			if (props.manager().liste.hasValue(item))
				props.manager().liste.auswahlAdd(item);
		if (props.manager().liste.auswahlExists())
			await props.gotoGruppenprozessView(true);
		else
			await props.gotoDefaultView(props.manager().getVorherigeAuswahl()?.id);
	}

	const clickedEintrag = computed(() => {
		if ((props.activeViewType === ViewType.GRUPPENPROZESSE) || (props.activeViewType === ViewType.HINZUFUEGEN))
			return null;
		return (props.manager().hasDaten()) ? props.manager().auswahl() : null;
	});

</script>
