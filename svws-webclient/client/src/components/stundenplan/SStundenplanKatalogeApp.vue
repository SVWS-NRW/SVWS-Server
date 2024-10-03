<template>
	<header class="svws-ui-header">
		<div class="svws-ui-header--title">
			<div class="svws-headline-wrapper">
				<h2 class="svws-headline">
					Allgemein / Vorlage
				</h2>
				<span class="svws-subline">Kataloge für die Nutzung in Stundenplänen</span>
			</div>
		</div>
		<div class="svws-ui-header--actions" />
	</header>
	<svws-ui-tab-bar :tab-manager>
		<div class="page--content page--content--flex-row">
			<router-view name="eintraege" />
			<router-view />
		</div>
	</svws-ui-tab-bar>
</template>

<script setup lang="ts">

	import type { StundenplanKatalogeAppProps } from './SStundenplanKatalogeAppProps';

	const props = defineProps<StundenplanKatalogeAppProps>();

</script>
