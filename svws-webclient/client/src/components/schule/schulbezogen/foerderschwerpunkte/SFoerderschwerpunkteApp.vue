<template>
	<div v-if="(manager().hasDaten() && (activeViewType === ViewType.DEFAULT)) || (activeViewType !== ViewType.DEFAULT)"
		class="flex flex-col w-full h-full overflow-hidden">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<template v-if="activeViewType === ViewType.DEFAULT">
						<h2 class="svws-headline">
							<span>
								{{ manager().auswahl().text }}
							</span>
							<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
								ID: {{ manager().daten().id }}
							</svws-ui-badge>
						</h2>
					</template>
					<template v-else-if="activeViewType === ViewType.HINZUFUEGEN">
						<h2 class="svws-headline">Anlegen eines neuen Förderschwerpunktes</h2>
					</template>
					<template v-else-if="activeViewType === ViewType.GRUPPENPROZESSE">
						<h2 class="svws-headline">Gruppenprozesse</h2>
						<span class="svws-subline">{{ foerderschwerpunkteSubline }}</span>
					</template>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<svws-ui-tab-bar :tab-manager :focus-switching-enabled :focus-help-visible>
			<router-view />
		</svws-ui-tab-bar>
	</div>
	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-team-line" />
	</div>
</template>

<script setup lang="ts">

	import type { FoerderschwerpunkteAppProps } from "~/components/schule/schulbezogen/foerderschwerpunkte/SFoerderschwerpunkteAppProps";
	import { useRegionSwitch, ViewType } from "@ui";
	import { computed } from "vue";

	const props = defineProps<FoerderschwerpunkteAppProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const foerderschwerpunkteSubline = computed(() => {
		const list = props.manager().liste.auswahlSorted();
		if (list.size() > 5)
			return `${list.size()} Förderschwerpunkte ausgewählt`;
		return [...list].map(k => k.text).join(', ');
	});

</script>
