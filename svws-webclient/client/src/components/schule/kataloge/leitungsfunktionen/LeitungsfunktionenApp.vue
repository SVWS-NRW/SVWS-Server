<template>
	<template v-if="(manager().hasDaten() && (activeViewType === ViewType.DEFAULT)) || (activeViewType !== ViewType.DEFAULT)">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<template v-if="activeViewType === ViewType.DEFAULT">
						<h2 class="svws-headline">
							<span>{{ manager().auswahl().bezeichnung }}</span>
							<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
								ID: {{ manager().auswahl().id }}
							</svws-ui-badge>
						</h2>
					</template>
					<template v-else-if="activeViewType === ViewType.HINZUFUEGEN">
						<h2 class="svws-headline">Anlegen einer neuen Leitungsfunktion...</h2>
					</template>
					<template v-else-if="activeViewType === ViewType.GRUPPENPROZESSE">
						<h2 class="svws-headline"> Gruppenprozesse </h2>
						<span class="svws-subline">{{ leitungsfunktionenSubline }}</span>
					</template>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<svws-ui-tab-bar :tab-manager :focus-switching-enabled :focus-help-visible>
			<router-view />
		</svws-ui-tab-bar>
	</template>
	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-archive-line" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { LeitungsfunktionenAppProps } from "./LeitungsfunktionenAppProps";
	import { ViewType } from "@ui/ui/nav/ViewType";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";

	const props = defineProps<LeitungsfunktionenAppProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const leitungsfunktionenSubline = computed(() => {
		const auswahl = props.manager().liste.auswahlSorted();
		if (auswahl.size() > 5) {
			return `${auswahl.size()} Leitungsfunktionen ausgewählt`;
		}
		return [...auswahl].map(l => l.bezeichnung).join(', ');
	});

</script>