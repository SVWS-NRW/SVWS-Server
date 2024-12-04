<template>
	<div v-if="visible" class="page--flex">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<h2 class="svws-headline">
						<span>{{ anzeigename }}</span>
						<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
							ID: {{ id }}
						</svws-ui-badge>
					</h2>
					<span class="svws-subline">
						{{ name }}
					</span>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<svws-ui-tab-bar :tab-manager :focus-switching-enabled :focus-help-visible>
			<router-view />
		</svws-ui-tab-bar>
	</div>
	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-school-line" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { BenutzerAppProps } from "./SBenutzerAppProps";
	import { useRegionSwitch } from "~/components/useRegionSwitch";

	const props = defineProps<BenutzerAppProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const id = computed<number | string>(() => props.auswahl()?.id ?? "?");
	const anzeigename = computed<string>(() => props.auswahl()?.anzeigename ?? "---");
	const name = computed<string>(() => props.auswahl()?.name ?? "---");

	const visible = computed<boolean>(() => props.auswahl() !== undefined);

</script>
