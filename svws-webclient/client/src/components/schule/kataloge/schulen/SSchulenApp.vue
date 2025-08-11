<template>
	<template v-if="(manager().hasDaten() && (activeViewType === ViewType.DEFAULT)) || (activeViewType !== ViewType.DEFAULT)">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<template v-if="activeViewType === ViewType.DEFAULT">
						<div class="svws-headline-wrapper">
							<h2 class="svws-headline">
								{{ manager().auswahl().kuerzel }}
								<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
									Schulnummer: {{ manager().auswahl().schulnummerStatistik }}
								</svws-ui-badge>
							</h2>
							<span class="svws-subline">{{ manager().daten().name }}</span>
						</div>
					</template>
					<template v-else-if="activeViewType === ViewType.HINZUFUEGEN">
						<h2 class="svws-headline">Anlegen einer neuen Schule...</h2>
					</template>
					<template v-else-if="activeViewType === ViewType.GRUPPENPROZESSE">
						<h2 class="svws-headline"> Gruppenprozesse </h2>
						<span class="svws-subline">{{ schulenSubline }}</span>
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

	import type { KatalogSchulenAppProps } from "./SSchulenAppProps";
	import { useRegionSwitch, ViewType } from "@ui";
	import { computed } from "vue";

	const props = defineProps<KatalogSchulenAppProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const schulenSubline = computed(() => {
		const auswahlSchulenList = props.manager().liste.auswahlSorted();
		if (auswahlSchulenList.size() > 5)
			return `${auswahlSchulenList.size()} Schulen ausgewählt`;
		return [...auswahlSchulenList].map(k => k.schulnummerStatistik).join(', ');
	})

</script>
