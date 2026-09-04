<template>
	<template v-if="(manager().hasDaten() && (activeViewType === ViewType.DEFAULT)) || (activeViewType !== ViewType.DEFAULT)">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<template v-if="activeViewType === ViewType.DEFAULT">
						<h2 class="svws-headline">
							<span>
								{{ manager().auswahl().kuerzel }}
							</span>

							<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
								ID: {{ manager().daten().id }}
							</svws-ui-badge>
						</h2>
						<span v-if="floskelgruppe !== null" class="svws-subline">{{ floskelgruppe.bezeichnung }}</span>
					</template>
					<template v-else-if="activeViewType === ViewType.HINZUFUEGEN">
						<h2 class="svws-headline">Anlegen einer neuen Floskel</h2>
					</template>
					<template v-else-if="activeViewType === ViewType.GRUPPENPROZESSE">
						<h2 class="svws-headline">Gruppenprozesse</h2>
						<span class="svws-subline">{{ floskelnSubline }}</span>
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

	import { computed } from "vue";
	import type { FloskelnAppProps } from "./FloskelnAppProps";
	import type { Floskel } from "@core/core/data/schule/Floskel";
	import type { Floskelgruppe } from "@core/core/data/schule/Floskelgruppe";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import { ViewType } from "@ui/ui/nav/ViewType";

	const props = defineProps<FloskelnAppProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const floskelgruppe = computed<Floskelgruppe | null>(() =>
		props.manager().floskelgruppenById.get(props.manager().auswahl().idFloskelgruppe ?? -1) ?? null
	);

	const floskelnSubline = computed(() => {
		const list = props.manager().liste.auswahlSorted();
		if (list.size() > 5) {
			return `${list.size()} Floskeln ausgewählt`;
		}
		return [...list].map((f: Floskel) => f.kuerzel).join(', ');
	});

</script>
