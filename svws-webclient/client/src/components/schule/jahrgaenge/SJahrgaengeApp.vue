<template>
	<template v-if="(jahrgangListeManager().hasDaten() && (activeViewType === ViewType.DEFAULT)) || (activeViewType !== ViewType.DEFAULT)">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<template v-if="activeViewType === ViewType.DEFAULT">
						<h2 class="svws-headline">
							<span>{{ jahrgangListeManager().auswahl().bezeichnung }}</span>
							<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
								ID: {{ jahrgangListeManager().auswahl().id }}
							</svws-ui-badge>
						</h2>
						<span class="svws-subline">{{ jahrgangListeManager().auswahl().kuerzel }}</span>
					</template>
					<template v-else-if="activeViewType === ViewType.HINZUFUEGEN">
						<h2 class="svws-headline">Anlegen eines neuen Jahrgangs...</h2>
					</template>
					<template v-else-if="activeViewType === ViewType.GRUPPENPROZESSE">
						<h2 class="svws-headline"> Gruppenprozesse </h2>
						<span class="svws-subline">{{ jahrgaengeSubline }}</span>
					</template>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<svws-ui-tab-bar :tab-manager enable-focus-switching>
			<router-view />
		</svws-ui-tab-bar>
	</template>
	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-archive-line" />
	</div>
</template>

<script setup lang="ts">

	import type { JahrgaengeAppProps } from "./SJahrgaengeAppProps";
	import { ViewType } from "@ui";
	import { computed } from "vue";

	const props = defineProps<JahrgaengeAppProps>();

	const jahrgaengeSubline = computed(() => {
		const auswahlJahrgaengeList = props.jahrgangListeManager().liste.auswahlSorted();
		if (auswahlJahrgaengeList.size() > 5)
			return `${auswahlJahrgaengeList.size()} Jahrgänge ausgewählt`;
		return [...auswahlJahrgaengeList].map(k => k.kuerzel).join(', ');
	})

</script>
