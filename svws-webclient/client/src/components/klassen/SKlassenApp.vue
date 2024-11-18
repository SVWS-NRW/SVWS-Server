<template>
	<div v-if="(manager().hasDaten() && (activeViewType === ViewType.DEFAULT)) || (activeViewType !== ViewType.DEFAULT)" class="page--flex">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<template v-if="activeViewType === ViewType.DEFAULT">
						<h2 class="svws-headline">
							<span>
								{{ manager().daten().kuerzel ? 'Klasse ' + manager().daten().kuerzel : '—' }}
								<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
									ID: {{ manager().daten().id }}
								</svws-ui-badge>
							</span>
						</h2>
						<span class="svws-subline">
							{{ lehrerkuerzel }}
						</span>
					</template>
					<template v-else-if="activeViewType === ViewType.HINZUFUEGEN">
						<h2 class="svws-headline">Anlegen einer neuen Klasse...</h2>
					</template>
					<template v-else-if="activeViewType === ViewType.GRUPPENPROZESSE">
						<h2 class="svws-headline"> Gruppenprozesse </h2>
						<span class="svws-subline">{{ klassenSubline }}</span>
					</template>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>

		<svws-ui-tab-bar :tab-manager enable-focus-switching>
			<router-view />
		</svws-ui-tab-bar>
	</div>
	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-team-line" />
	</div>
</template>

<script setup lang="ts">

	import type { KlassenAppProps } from "./SKlassenAppProps";
	import { computed } from "vue";
	import { ViewType } from "@ui";

	const props = defineProps<KlassenAppProps>();

	const klassenSubline = computed(() => {
		const auswahlKlassenList = props.manager().liste.auswahlSorted();
		if (auswahlKlassenList.size() > 5)
			return `${auswahlKlassenList.size()} Klassen ausgewählt`;
		return [...auswahlKlassenList].map(k => k.kuerzel).join(', ');
	})

	const lehrerkuerzel = computed<string>(() => {
		if (!props.manager().hasDaten())
			return '';
		let lehrerkuerzelStr = '';
		for (const lehrerId of props.manager().daten().klassenLeitungen) {
			const lehrer = props.manager().lehrer.get(lehrerId);
			if (lehrer === null)
				continue;
			lehrerkuerzelStr += (lehrerkuerzelStr.length > 0) ? `, ${lehrer.kuerzel}` : lehrer.kuerzel;
		}
		return lehrerkuerzelStr;
	});

</script>
