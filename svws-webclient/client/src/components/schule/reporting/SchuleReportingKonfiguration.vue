<template>
	<div class="flex flex-col w-full h-full overflow-hidden">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<h2 class="svws-headline">
						<span>Reporting - Einstellungen</span>
					</h2>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<div class="page page-flex-row">
			<svws-ui-todo>Einstellungen für das Reporting</svws-ui-todo>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { SchuleReportingProps } from "./SchuleReportingProps";

	const props = defineProps<SchuleReportingProps>();

</script>
