<template>
	<div v-if="(manager().hasDaten() && (activeViewType === ViewType.DEFAULT)) || (activeViewType !== ViewType.DEFAULT)"
		class="flex flex-col w-full h-full overflow-hidden">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<template v-if="activeViewType === ViewType.DEFAULT">
						<h2 class="svws-headline">
							<span>
								{{ manager().auswahl().bezeichnung }}
							</span>
							<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
								ID: {{ manager().daten().id }}
							</svws-ui-badge>
						</h2>
					</template>
					<template v-else-if="activeViewType === ViewType.HINZUFUEGEN">
						<h2 class="svws-headline">Anlegen einer neuen Haltestelle</h2>
					</template>
					<template v-else-if="activeViewType === ViewType.GRUPPENPROZESSE">
						<h2 class="svws-headline">Gruppenprozesse</h2>
						<span class="svws-subline">{{ haltestellenSubline }}</span>
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

	import type { HaltestellenAppProps } from "~/components/schule/kataloge/haltestellen/SHaltestellenAppProps";
	import { useRegionSwitch, ViewType } from "@ui";
	import { computed } from "vue";

	const props = defineProps<HaltestellenAppProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const haltestellenSubline = computed(() => {
		const list = props.manager().liste.auswahlSorted();
		if (list.size() > 5)
			return `${list.size()} Haltestellen ausgewählt`;
		return [...list].map(k => k.bezeichnung).join(', ');
	});

</script>
