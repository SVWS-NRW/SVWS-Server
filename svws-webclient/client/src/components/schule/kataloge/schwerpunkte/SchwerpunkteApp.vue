<template>
	<template v-if="dataLoaded">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<template v-if="activeViewType === ViewType.DEFAULT">
						<h2 class="svws-headline">
							<span>
								{{ manager().daten().bezeichnung }}
							</span>
							<svws-ui-badge type="light"
								title="ID"
								class="font-mono"
								size="small">
								ID: {{ manager().daten().id }}
							</svws-ui-badge>
						</h2>
					</template>
					<template v-else-if="activeViewType === ViewType.HINZUFUEGEN">
						<h2 class="svws-headline">Anlegen eines neuen Schwerpunktes</h2>
					</template>
					<template v-else-if="activeViewType === ViewType.GRUPPENPROZESSE">
						<h2 class="svws-headline"> Gruppenprozesse </h2>
						<span class="svws-subline">{{ schwerpunkteSubline }}</span>
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
		<span class="icon i-ri-team-line" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from 'vue';
	import type { SchwerpunkteAppProps } from './SchwerpunkteAppProps';
	import { useRegionSwitch } from '@ui/ui/composables/useRegionSwitch';
	import { ViewType } from '@ui/ui/nav/ViewType';

	const props = defineProps<SchwerpunkteAppProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const dataLoaded = computed(() =>
		props.activeViewType !== ViewType.DEFAULT || props.manager().hasDaten()
	);

	const schwerpunkteSubline = computed(() => {
		const list = props.manager().liste.auswahlSorted();
		if (list.size() > 5) {
			return `${list.size()} Schwerpunkte ausgewählt`;
		}
		return [...list].map(k => k.bezeichnung).join(', ');
	});
</script>
