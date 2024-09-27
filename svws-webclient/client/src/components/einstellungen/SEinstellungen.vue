<template>
	<div class="page--flex">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<h2 class="svws-headline">
						<span>Einstellungen</span>
					</h2>
					<span class="svws-subline">...</span>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
	</div>
</template>

<script setup lang="ts">

	import type { EinstellungenProps } from "./EinstellungenProps";

	const props = defineProps<EinstellungenProps>();

</script>
