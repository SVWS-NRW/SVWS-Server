<template>
	<svws-ui-content-card>
		<svws-ui-table selectable :items="listBenutzergruppen" :disable-footer="true" :columns="cols">
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
					<s-benutzergruppen-listeneintrag :row="bgle"
						:add-benutzer-to-benutzergruppe="addBenutzerToBenutzergruppe"
						:remove-benutzer-from-benutzergruppe="removeBenutzerFromBenutzergruppe"
						:get-benutzer-manager="getBenutzerManager"
						:goto-benutzergruppe="gotoBenutzergruppe" />
				</div>
			</template>
		</svws-ui-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { BenutzergruppeListeEintrag, BenutzerManager, List } from "@core";
	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";

	const props = defineProps<{
		listBenutzergruppen: List<BenutzergruppeListeEintrag>;
		getBenutzerManager: () => BenutzerManager;
		addBenutzerToBenutzergruppe : (bg_id : number) => Promise<void>;
		removeBenutzerFromBenutzergruppe	: (bg_id : number) => Promise<void>;
		gotoBenutzergruppe: (b_id: number) => Promise<void>;
	}>();

	const selected: WritableComputedRef<boolean> = computed({
		get: () => props.listBenutzergruppen.size() === props.getBenutzerManager().anzahlGruppen() ?  true : false,
		set: (value) => {
			if (value)
				void props.addBenutzerToBenutzergruppe(-1);
			else
				void props.removeBenutzerFromBenutzergruppe(-1);
		}
	});

	const cols = [
		{key: 'bezeichnung', label: 'Bezeichnung'},
		{key: 'istAdmin', label: 'ist Admin'}
	]

</script>

<style scoped lang="postcss">
	.svws-ui-tr {
		grid-template-columns: minmax(3rem, 0.5fr) minmax(4rem, 10fr) var(--checkbox-width) minmax(4rem, 0.25fr) !important;
	}
</style>
