<template>
	<template v-if="klassenListeManager().hasDaten() || props.gruppenprozesseEnabled || props.creationModeEnabled">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<template v-if="props.gruppenprozesseEnabled">
						<h2 class="svws-headline">
							Gruppenprozesse
						</h2>
						<span class="svws-subline">{{ selectedKlassen }}</span>
					</template>
					<template v-else-if="props.creationModeEnabled">
						<h2 class="svws-headline">Anlegen einer neuen Klasse...</h2>
					</template>
					<template v-else>
						<h2 class="svws-headline">
							<span>
								{{
									klassenListeManager().daten().kuerzel ? 'Klasse ' + klassenListeManager().daten().kuerzel : '—'
								}}
								<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
									ID: {{ klassenListeManager().daten().id }}
								</svws-ui-badge>
							</span>
						</h2>
						<span class="svws-subline">
							{{ lehrerkuerzel }}
						</span>
					</template>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<svws-ui-router-tab-bar :tabs="props.tabs()" :hidden="props.tabsHidden" :tab="selectedTab" :set-tab="setSelectedTab">
			<router-view />
		</svws-ui-router-tab-bar>
	</template>
	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-team-line" />
	</div>
</template>

<script setup lang="ts">

	import type { KlassenAppProps } from "./SKlassenAppProps";
	import { computed } from "vue";

	const props = defineProps<KlassenAppProps>();

	const selectedKlassen = computed<string>(() => {
		const selectedKlassen = props.klassenListeManager().liste.auswahlSorted();
		let selectedKlassenStr = '';
		for (const klasse of selectedKlassen)
			selectedKlassenStr += (selectedKlassenStr.length > 0) ? `, ${klasse.kuerzel}` : klasse.kuerzel;
		return selectedKlassenStr;
	});

	const lehrerkuerzel = computed<string>(() => {
		if (!props.klassenListeManager().hasDaten())
			return '';
		let lehrerkuerzelStr = '';
		for (const lehrerId of props.klassenListeManager().daten().klassenLeitungen) {
			const lehrer = props.klassenListeManager().lehrer.get(lehrerId);
			if (lehrer === null)
				continue;
			lehrerkuerzelStr += (lehrerkuerzelStr.length > 0) ? `, ${lehrer.kuerzel}` : lehrer.kuerzel;
		}
		return lehrerkuerzelStr;
	});

</script>
