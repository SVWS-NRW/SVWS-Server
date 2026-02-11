<template>
	<template v-if="(manager().hasDaten() && (activeViewType === ViewType.DEFAULT)) || (activeViewType !== ViewType.DEFAULT)">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<template v-if="activeViewType === ViewType.DEFAULT">
						<h2 class="svws-headline">
							<span>
								{{ manager().auswahl().name }}
							</span>
							<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
								ID: {{ manager().auswahl().id }}
							</svws-ui-badge>
						</h2>
					</template>
					<template v-else-if="activeViewType === ViewType.HINZUFUEGEN">
						<h2 class="svws-headline">Anlegen eines neuen Betriebs</h2>
					</template>
					<template v-else-if="activeViewType === ViewType.GRUPPENPROZESSE">
						<h2 class="svws-headline"> Gruppenprozesse </h2>
						<span class="svws-subline">{{ betriebeSubline }}</span>
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

	import { useRegionSwitch, ViewType } from "@ui";
	import { computed } from "vue";
	import type { BetriebeAppProps } from "~/components/schule/kataloge/betriebe/BetriebeAppProps";

	const props = defineProps<BetriebeAppProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const betriebeSubline = computed(() => {
		const betriebe = props.manager().liste.auswahlSorted();
		if (betriebe.size() > 5) {
			return `${betriebe.size()} Betriebe ausgewählt`;
		}
		return [...betriebe].map(k => k.name).join(', ');
	});

</script>
