<template>
	<svws-ui-content-card>
		<svws-ui-table selectable :items="listBenutzergruppen" :disable-footer="true" :columns>
			<template #header>
				<div role="row" class="svws-ui-tr">
					<!--<div class="svws-ui-td svws-align-center" role="columnheader" aria-label="Alle auswählen">
						<svws-ui-checkbox :model-value="selected" />
					</div>-->
					<div class="svws-ui-td col-span-2" role="columnheader">
						Zugewiesene Benutzergruppen
					</div>
				</div>
			</template>
			<template #body>
				<div role="row" class="svws-ui-tr" v-for="(bgle, index) in listBenutzergruppen" :key="index">
					<s-benutzergruppen-listeneintrag :row="bgle" :add-benutzer-to-benutzergruppe :remove-benutzer-from-benutzergruppe :get-benutzer-manager :goto-benutzergruppe />
				</div>
			</template>
		</svws-ui-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { BenutzergruppeListeEintrag, BenutzerManager, List } from "@core";

	const props = defineProps<{
		listBenutzergruppen: List<BenutzergruppeListeEintrag>;
		getBenutzerManager: () => BenutzerManager;
		addBenutzerToBenutzergruppe : (bg_id : number) => Promise<void>;
		removeBenutzerFromBenutzergruppe	: (bg_id : number) => Promise<void>;
		gotoBenutzergruppe: (b_id: number) => Promise<void>;
	}>();

	const columns = [
		{key: 'bezeichnung', label: 'Bezeichnung'},
		{key: 'istAdmin', label: 'ist Admin'}
	]

</script>

<style scoped lang="postcss">
	.svws-ui-tr {
		grid-template-columns: minmax(3rem, 0.5fr) minmax(4rem, 10fr) var(--checkbox-width) !important;
	}
</style>
