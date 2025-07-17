<template>
	<div v-if="(manager().hasDaten() && (activeViewType === ViewType.DEFAULT)) || (activeViewType !== ViewType.DEFAULT)" class="w-full h-full overflow-hidden">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<template v-if="activeViewType === ViewType.DEFAULT">
						<h2 class="svws-headline">
							<span>{{ fach.bezeichnung }}</span>
							<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
								ID: {{ fach.id }}
							</svws-ui-badge>
						</h2>
						<span class="svws-subline">{{ fach.kuerzel }}</span>
					</template>
					<template v-else-if="activeViewType === ViewType.HINZUFUEGEN">
						<h2 class="svws-headline">Anlegen eines neuen Faches...</h2>
					</template>
					<template v-else-if="activeViewType === ViewType.GRUPPENPROZESSE">
						<h2 class="svws-headline"> Gruppenprozesse </h2>
						<span class="svws-subline">{{ faecherSubline }}</span>
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
		<span class="icon i-ri-archive-line" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { FaecherAppProps } from "./SFaecherAppProps";
	import type { FaecherListeEintrag } from "@core";
	import { useRegionSwitch, ViewType } from "@ui";

	const props = defineProps<FaecherAppProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const fach = computed<FaecherListeEintrag>(() => props.manager().auswahl());

	const faecherSubline = computed(() => {
		const auswahlFaecherList = props.manager().liste.auswahlSorted();
		if (auswahlFaecherList.size() > 5)
			return `${auswahlFaecherList.size()} Fächer ausgewählt`;
		return [...auswahlFaecherList].map(k => k.kuerzel).join(', ');
	});

</script>
