<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Server</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table :clickable="!manager().liste.auswahlExists()" :clicked="clickedEintrag" @update:clicked="server => gotoDefaultView(server.id)" :items="manager().filtered()"
				:model-value="[...props.manager().liste.auswahl()]" @update:model-value="items => setAuswahl(items)" :columns :filter-open="true" count scroll-into-view scroll allow-arrow-key-selection
				:focus-switching-enabled :focus-help-visible selectable :unselectable="new Set([manager().getKonfigurationLokal()])">
				<template #cell(id)="{ rowData }">
					<span class="icon i-ri-checkbox-circle-fill mr-3 icon-ui-success" v-if="(manager().getConnectionResponse(rowData.id).success) || (rowData.id === -1)" />
					<span class="icon i-ri-alert-fill mr-3 icon-ui-danger" v-else-if="manager().getConnectionResponse(rowData.id).success === false" />
				</template>
				<template #actions>
					<svws-ui-tooltip position="bottom">
						<svws-ui-button :disabled="activeViewType === ViewType.HINZUFUEGEN" type="icon" @click="gotoHinzufuegenView(true)" :has-focus="manager().filtered().size() === 0">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neues Verbindung anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed } from 'vue';
	import { useRegionSwitch, ViewType } from '@ui';
	import type { NotenmodulAdministrationAuswahlProps } from './NotenmodulAdministrationAuswahlProps';
	import type { ENMServerConnection } from '@core';

	const props = defineProps<NotenmodulAdministrationAuswahlProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const columns = [{ key: "bezeichnung", label: "Bezeichnung" }, { key: "id", label: "Status" }];

	const clickedEintrag = computed(() => {
		if ((props.activeViewType === ViewType.GRUPPENPROZESSE) || (props.activeViewType === ViewType.HINZUFUEGEN)) {
			return null;
		}
		return props.manager().hasDaten() ? props.manager().auswahl() : null;
	});

	async function setAuswahl(items: ENMServerConnection[]) {
		props.manager().liste.auswahlClear();
		for (const item of items) {
			if (props.manager().liste.hasValue(item)) {
				props.manager().liste.auswahlAdd(item);
			}
		}
		if (props.manager().liste.auswahlExists()) {
			await props.gotoGruppenprozessView(true);
		} else {
			await props.gotoDefaultView(props.manager().getVorherigeAuswahl()?.id);
		}
	}

</script>
