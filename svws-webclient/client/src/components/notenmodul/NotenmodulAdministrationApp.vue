<template>
	<template v-if="(manager().hasDaten() && (activeViewType === ViewType.DEFAULT)) || (activeViewType !== ViewType.DEFAULT)">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<template v-if="activeViewType === ViewType.DEFAULT">
						<h2 class="svws-headline">
							Administration
						</h2>
						<span class="svws-subline">{{ konfiguration.bezeichnung }}</span>
					</template>
					<template v-else-if="activeViewType === ViewType.HINZUFUEGEN">
						<h2 class="svws-headline">Anlegen einer neuen Verbindung...</h2>
					</template>
					<template v-else-if="activeViewType === ViewType.GRUPPENPROZESSE">
						<h2 class="svws-headline"> Gruppenprozesse </h2>
						<span class="svws-subline">{{ subline }}</span>
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
		<span class="icon i-ri-briefcase-line" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { useRegionSwitch, ViewType } from "@ui";
	import type { NotenmodulAdministrationAppProps } from "./NotenmodulAdministrationAppProps";

	const props = defineProps<NotenmodulAdministrationAppProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const konfiguration = computed(() => props.manager().auswahl());

	const subline = computed(() => {
		const list = props.manager().liste.auswahlSorted();
		if (list.size() > 5) {
			return `${list.size()} Verbindungen ausgewählt`;
		}
		return [...list].map(k => k.bezeichnung).join(', ');
	});
</script>
