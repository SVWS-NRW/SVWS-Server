<template>
	<div class="page--flex">
		<header class="svws-ui-header max-w-[140rem]">
			<div class="svws-ui-header--title gap-x-8 lg:gap-x-16 w-full">
				<div class="svws-headline-wrapper flex-[2]">
					<h2 class="svws-headline">
						<span>Konfiguration des SVWS-Servers</span>
					</h2>
					<span class="svws-subline">
						<span> Version: TODO </span>
					</span>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<div class="page--content">
			Diese Seite befindet sich im Aufbau und wird Anpassungen an einigen Konfigurationseinstellungen des SVWS-Servers ermöglichen.
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { ConfigAppProps } from "./SConfigAppProps";

	const props = defineProps<ConfigAppProps>();

	const info = computed(() => props.schuleInfo())

</script>
