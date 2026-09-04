<template>
	<header class="svws-ui-header">
		<div class="svws-ui-header--title">
			<div class="svws-headline-wrapper">
				<h2 class="svws-headline">
					{{ schuleStammdaten.bezeichnung1 }}
					<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
						{{ schuleStammdaten.schulNr }}
					</svws-ui-badge>
				</h2>
				<span class="svws-subline">
					{{ schuleStammdaten.schulform }}
				</span>
			</div>
		</div>
		<div class="svws-ui-header--actions" />
	</header>

	<svws-ui-tab-bar :tab-manager :focus-switching-enabled :focus-help-visible>
		<router-view />
	</svws-ui-tab-bar>
</template>

<script setup lang="ts">

	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import type { StatistikAppProps } from "./StatistikAppProps";

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const props = defineProps<StatistikAppProps>();

</script>
