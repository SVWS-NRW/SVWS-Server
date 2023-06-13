<template>
	<svws-ui-content-card title="Zugewiesene Benutzergruppen">
		<svws-ui-data-table selectable :items="listBenutzergruppen" :disable-footer="true">
			<template #header>
				<div role="row" class="data-table__tr data-table__thead__tr">
					<div role="checkbox" aria-label="Alle auswählen"
						class="data-table__th data-table__thead__th data-table__cell-select">
						<svws-ui-checkbox class="data-table__cell-checkbox"
							v-model="selected" />
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th">
						<div class="data-table__th-wrapper">
							<span class="data-table__th-title">
								Bezeichnung
							</span>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th">
						<div class="data-table__th-wrapper">
							<span class="data-table__th-title font-mono">
								ID
							</span>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__th__align-center data-table__thead__th">
						<span class="data-table__th-title" title="Administrative Gruppe">
							<i-ri-shield-star-line />
						</span>
					</div>
				</div>
			</template>
			<template #body>
				<div role="row"
					class="data-table__tr data-table__tbody__tr"
					v-for="(bgle, index) in listBenutzergruppen"
					:key="index">
					<s-benutzergruppen-listeneintrag :row="bgle"
						:add-benutzer-to-benutzergruppe="addBenutzerToBenutzergruppe"
						:remove-benutzer-from-benutzergruppe="removeBenutzerFromBenutzergruppe"
						:get-benutzer-manager="getBenutzerManager"
						:goto-benutzergruppe="gotoBenutzergruppe" />
				</div>
			</template>
		</svws-ui-data-table>
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

</script>

<style scoped lang="postcss">
	.data-table__tr {
		grid-template-columns: var(--checkbox-width) minmax(4rem, 10fr) minmax(4rem, 0.25fr) var(--checkbox-width) !important;
	}
</style>
